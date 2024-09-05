package me.varoa.gamification.service

import jakarta.transaction.Transactional
import me.varoa.gamification.domain.BadgeCard
import me.varoa.gamification.domain.BadgeType
import me.varoa.gamification.domain.ScoreCard
import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.domain.processor.BadgeProcessor
import me.varoa.gamification.repository.BadgeRepository
import me.varoa.gamification.repository.ScoreRepository
import me.varoa.gamification.utils.lazyLogger
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Transactional
@Service
class GameServiceImpl(
    private val scoreRepository: ScoreRepository,
    private val badgeRepository: BadgeRepository,
    private val badgeProcessors: List<BadgeProcessor>,
) : GameService {
    private val log by lazyLogger()

    override fun newAttemptForUser(challenge: ChallengeSolvedEvent): GameService.GameResult {
        if (challenge.correct) {
            val scoreCard =
                ScoreCard(
                    accountId = challenge.accountId,
                    attemptId = challenge.attemptId,
                )
            scoreRepository.save(scoreCard)
            log.info("Account ${challenge.accountAlias} scored ${scoreCard.score} points for attempt id ${challenge.attemptId}")

            val badgeCards = processForBadges(challenge)
            return GameService.GameResult(score = scoreCard.score, badges = badgeCards.map { it.badgeType })
        } else {
            log.info("Attempt id ${challenge.attemptId} is not correct. Account ${challenge.accountAlias} doesn't get score.")
            return GameService.GameResult(0, emptyList())
        }
    }

    private fun processForBadges(solvedChallenge: ChallengeSolvedEvent): List<BadgeCard> {
        val optTotalScore = scoreRepository.getTotalScoreFromAccount(solvedChallenge.accountId)

        if (optTotalScore.isEmpty) return emptyList()
        val totalScore = optTotalScore.get()
        val scoreCardList = scoreRepository.findByAccountIdOrderByScoreTimestampDesc(solvedChallenge.accountId)
        val alreadyGotBadges: Set<BadgeType> =
            badgeRepository
                .findByAccountIdOrderByBadgeTimestampDesc(solvedChallenge.accountId)
                .stream()
                .map { it.badgeType }
                .collect(Collectors.toSet())

        val newBadgeCards: List<BadgeCard> =
            badgeProcessors
                .stream()
                .filter { bp ->
                    !alreadyGotBadges.contains(bp.badgeType())
                }.map { bp ->
                    bp.processOptionalBadge(totalScore, scoreCardList, solvedChallenge)
                }.flatMap { it.stream() }
                .map { badgeType ->
                    BadgeCard(accountId = solvedChallenge.accountId, badgeType = badgeType)
                }.collect(Collectors.toList())

        badgeRepository.saveAll(newBadgeCards)

        return newBadgeCards
    }
}
