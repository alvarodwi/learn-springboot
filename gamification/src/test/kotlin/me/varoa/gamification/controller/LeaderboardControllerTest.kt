package me.varoa.gamification.controller

import me.varoa.gamification.domain.LeaderboardRow
import me.varoa.gamification.service.LeaderboardService
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
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import kotlin.jvm.Throws

@ExtendWith(SpringExtension::class)
@AutoConfigureJsonTesters
@WebMvcTest(LeaderboardController::class)
class LeaderboardControllerTest {
    @MockBean
    private lateinit var service: LeaderboardService

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var json: JacksonTester<List<LeaderboardRow>>

    @Test
    @Throws(Exception::class)
    fun getLeaderboardTest() {
        val row1 = LeaderboardRow(1L, 400L)
        val row2 = LeaderboardRow(1L, 340L)
        val leaderboard = listOf(row1, row2)

        given(service.getCurrentLeaderboard()).willReturn(leaderboard)

        val response =
            mvc
                .perform(
                    get("/leaderboard")
                        .accept(MediaType.APPLICATION_JSON),
                ).andReturn()
                .response

        then(response.status).isEqualTo(HttpStatus.OK.value())
        then(response.contentAsString).isEqualTo(json.write(leaderboard).json)
    }
}
