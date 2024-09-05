package me.varoa.multiplication.service

import me.varoa.multiplication.domain.Account
import me.varoa.multiplication.domain.ChallengeAttempt
import me.varoa.multiplication.domain.dto.ChallengeAttemptDTO
import me.varoa.multiplication.publisher.ChallengeEventPub
import me.varoa.multiplication.repository.AccountRepository
import me.varoa.multiplication.repository.ChallengeAttemptRepository
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ChallengeServiceTest {
    private lateinit var service: ChallengeService

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var attemptRepository: ChallengeAttemptRepository

    @Mock
    private lateinit var challengeEventPub: ChallengeEventPub

    @BeforeEach
    fun setUp() {
        service = ChallengeServiceImpl(accountRepository, attemptRepository, challengeEventPub)
        // problem of converting java code to kotlin, damn domain classes is nullable in java but technically shouldn't
        // so when testing it, i need to mock the repository too for all the functions (hence placed in setUp)
        Mockito.lenient().`when`(accountRepository.save(any())).then(returnsFirstArg<Account>())
        Mockito.lenient().`when`(attemptRepository.save(any())).then(returnsFirstArg<ChallengeAttempt>())
    }

    @Test
    fun checkCorrectAttemptTest() {
        val attempt = ChallengeAttemptDTO(50, 60, "john_doe", 3000)
        val resultAttempt = service.verifyAttempt(attempt)

        then(resultAttempt.correct).isTrue()

        verify(accountRepository).save(Account(alias = "john_doe"))
        verify(attemptRepository).save(resultAttempt)
        verify(challengeEventPub).challengeSolved(resultAttempt)
    }

    @Test
    fun checkWrongAttemptTest() {
        val attempt = ChallengeAttemptDTO(50, 60, "john_doe", 2000)
        val resultAttempt = service.verifyAttempt(attempt)
        then(resultAttempt.correct).isFalse()
        verify(accountRepository).save(Account(alias = "john_doe"))
        verify(attemptRepository).save(resultAttempt)
        verify(challengeEventPub).challengeSolved(resultAttempt)
    }

    @Test
    fun checkExistingUserTest() {
        val account = Account(1L, "john_doe")
        given(accountRepository.findByAlias("john_doe")).willReturn(Optional.of(account))

        val attempt = ChallengeAttemptDTO(50, 60, "john_doe", 3000)
        val resultAttempt = service.verifyAttempt(attempt)
        then(resultAttempt.correct).isTrue()
        then(resultAttempt.account).isEqualTo(account)
        verify(accountRepository, never()).save(any())
        verify(attemptRepository).save(resultAttempt)
        verify(challengeEventPub).challengeSolved(resultAttempt)
    }

    @Test
    fun retrieveStatTest() {
        val account = Account(alias = "john_doe")
        val attempt1 = ChallengeAttempt(1L, account, 50, 60, 3010, false)
        val attempt2 = ChallengeAttempt(2L, account, 50, 60, 3000, true)
        val lastAttempts = listOf(attempt1, attempt2)

        given(attemptRepository.findTop10ByAccountAliasOrderByIdDesc("john_doe")).willReturn(lastAttempts)

        val lastAttemptsResult = service.getStatsForUser("john_doe")
        then(lastAttemptsResult).isEqualTo(lastAttempts)
    }
}
