package me.varoa.multiplication.controller

import me.varoa.multiplication.domain.Account
import me.varoa.multiplication.domain.ChallengeAttempt
import me.varoa.multiplication.domain.dto.ChallengeAttemptDTO
import me.varoa.multiplication.service.ChallengeService
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@ExtendWith(SpringExtension::class)
@AutoConfigureJsonTesters
@WebMvcTest(ChallengeAttemptController::class)
class ChallengeAttemptControllerTest {
    @MockBean
    private lateinit var service: ChallengeService

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var jsonRequestAttempt: JacksonTester<ChallengeAttemptDTO>

    @Autowired
    private lateinit var jsonResponseAttempt: JacksonTester<ChallengeAttempt>

    @Test
    @Throws(Exception::class)
    fun postValidResult() {
        val account = Account(1L, "John")
        val attemptId = 5L
        val attemptDTO = ChallengeAttemptDTO(50, 70, "John", 3500)

        val expected = ChallengeAttempt(attemptId, account, 50, 70, 3500, true)
        given(
            service.verifyAttempt(attemptDTO),
        ).willReturn(expected)

        val response: MockHttpServletResponse =
            mvc
                .perform(
                    post("/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAttempt.write(attemptDTO).json),
                ).andReturn()
                .response
        then(response.status).isEqualTo(HttpStatus.OK.value())
        then(response.contentAsString).isEqualTo(
            jsonResponseAttempt
                .write(
                    expected,
                ).json,
        )
    }

    @Test
    @Throws(Exception::class)
    fun postInvalidResult() {
        val attemptDTO = ChallengeAttemptDTO(2000, -70, "John", 1)

        val response =
            mvc
                .perform(
                    post("/attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestAttempt.write(attemptDTO).json),
                ).andReturn()
                .response
        then(response.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
    }
}
