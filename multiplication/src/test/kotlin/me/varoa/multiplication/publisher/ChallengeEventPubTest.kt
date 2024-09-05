package me.varoa.multiplication.publisher

import me.varoa.multiplication.domain.Account
import me.varoa.multiplication.domain.ChallengeAttempt
import me.varoa.multiplication.domain.dto.ChallengeSolvedEvent
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.springframework.amqp.core.AmqpTemplate

@ExtendWith(MockitoExtension::class)
class ChallengeEventPubTest {
    private lateinit var challengeEventPub: ChallengeEventPub

    @Mock
    private lateinit var amqpTemplate: AmqpTemplate

    @BeforeEach
    fun setUp() {
        challengeEventPub = ChallengeEventPub(amqpTemplate, "test.topic")
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun sendAttempt(correct: Boolean) {
        val attempt = createTestAttempt(correct)

        challengeEventPub.challengeSolved(attempt)

        val exchangeCaptor = ArgumentCaptor.forClass(String::class.java)
        val routingKeyCaptor = ArgumentCaptor.forClass(String::class.java)
        val eventCaptor = ArgumentCaptor.forClass(ChallengeSolvedEvent::class.java)
        verify(amqpTemplate).convertAndSend(exchangeCaptor.capture(), routingKeyCaptor.capture(), eventCaptor.capture())

        then(exchangeCaptor.value).isEqualTo("test.topic")
        then(routingKeyCaptor.value).isEqualTo("attempt.${if (correct) "correct" else "wrong"}")
        then(eventCaptor.value).isEqualTo(solvedEvent(correct))
    }

    private fun createTestAttempt(correct: Boolean) =
        ChallengeAttempt(1L, Account(1L, "John"), 30, 40, if (correct) 1200 else 1400, correct)

    private fun solvedEvent(correct: Boolean) = ChallengeSolvedEvent(1L, 1L, "John", 30, 40, correct)
}
