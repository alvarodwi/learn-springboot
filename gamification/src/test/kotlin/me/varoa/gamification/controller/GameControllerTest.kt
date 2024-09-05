package me.varoa.gamification.controller

import me.varoa.gamification.domain.dto.ChallengeSolvedEvent
import me.varoa.gamification.service.GameService
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import kotlin.jvm.Throws

@ExtendWith(SpringExtension::class)
@AutoConfigureJsonTesters
@WebMvcTest(GameController::class)
class GameControllerTest {
    @MockBean
    private lateinit var service: GameService

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var jsonRequest: JacksonTester<ChallengeSolvedEvent>

    @Test
    @Throws(Exception::class)
    fun postNewAttempt() {
        val challenge =
            ChallengeSolvedEvent(
                accountId = 1L,
                attemptId = 1L,
                accountAlias = "John",
                factorA = 10,
                factorB = 10,
                correct = true,
            )

        val response =
            mvc
                .perform(
                    post("/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.write(challenge).json),
                ).andReturn()
                .response

        verify(service).newAttemptForUser(challenge)
        then(response.status).isEqualTo(HttpStatus.OK.value())
    }
}
