package me.varoa.gamification.repository

import me.varoa.gamification.domain.LeaderboardRow
import me.varoa.gamification.domain.ScoreCard
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.Optional

interface ScoreRepository : CrudRepository<ScoreCard, Long> {
    @Query(
        """select sum(s.score)
           from ScoreCard s
           where s.accountId = :accountId
           group by s.accountId""",
    )
    fun getTotalScoreFromAccount(
        @Param("accountId") accountId: Long,
    ): Optional<Int>

    @Query(
        """select new me.varoa.gamification.domain.LeaderboardRow(s.accountId, sum(s.score))
           from ScoreCard s
           group by s.accountId
           order by sum(s.score) desc""",
    )
    fun findFirst10(): List<LeaderboardRow>

    fun findByAccountIdOrderByScoreTimestampDesc(accountId: Long): List<ScoreCard>
}
