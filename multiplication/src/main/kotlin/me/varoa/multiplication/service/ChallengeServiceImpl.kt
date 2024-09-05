package me.varoa.multiplication.service

import jakarta.transaction.Transactional
import me.varoa.multiplication.domain.Account
import me.varoa.multiplication.domain.ChallengeAttempt
import me.varoa.multiplication.domain.dto.ChallengeAttemptDTO
import me.varoa.multiplication.publisher.ChallengeEventPub
import me.varoa.multiplication.repository.AccountRepository
import me.varoa.multiplication.repository.ChallengeAttemptRepository
import me.varoa.multiplication.utils.lazyLogger
import org.springframework.stereotype.Service

@Service
class ChallengeServiceImpl(
    private val accountRepository: AccountRepository,
    private val attemptRepository: ChallengeAttemptRepository,
    private val challengeEventPub: ChallengeEventPub,
) : ChallengeService {
    private val log by lazyLogger()

    @Transactional
    override fun verifyAttempt(attempt: ChallengeAttemptDTO): ChallengeAttempt {
        val account =
            accountRepository
                .findByAlias(attempt.alias)
                .orElseGet {
                    log.info("Creating new account with alias ${attempt.alias}")
                    return@orElseGet accountRepository.save(Account(alias = attempt.alias))
                }

        val isCorrect = attempt.guess == (attempt.factorA * attempt.factorB)
        val checkedAttempt =
            ChallengeAttempt(
                account = account,
                factorA = attempt.factorA,
                factorB = attempt.factorB,
                resultAttempt = attempt.guess,
                correct = isCorrect,
            )
        val storedAttempt = attemptRepository.save(checkedAttempt)
        challengeEventPub.challengeSolved(storedAttempt)

        return storedAttempt
    }

    override fun getStatsForUser(userAlias: String): List<ChallengeAttempt> =
        attemptRepository.findTop10ByAccountAliasOrderByIdDesc(userAlias)
}
