package me.varoa.gamification.service

import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import java.lang.IllegalStateException

@ExtendWith(MockitoExtension::class)
class GameEventHandlerTest {
    private lateinit var eventHandler: GameEventHandler

    @Mock
    private lateinit var service: GameService

    @BeforeEach
    fun setUp() {
        eventHandler = GameEventHandler(service)
    }

    @Test
    fun eventReceived() {
        val event = createTestEvent()

        eventHandler.handleMultiplicationSolved(event)
        verify(service).newAttemptForUser(event)
    }

    @Test
    fun eventFailed() {
        val event = createTestEvent()
        given(service.newAttemptForUser(event)).willThrow(IllegalStateException("something malicious is brewing"))

        assertThatThrownBy { eventHandler.handleMultiplicationSolved(event) }
            .isInstanceOf(AmqpRejectAndDontRequeueException::class.java)
    }

    private fun createTestEvent() =
        ChallengeSolvedEvent(
            1L,
            1L,
            "John",
            30,
            40,
            true,
        )
}
