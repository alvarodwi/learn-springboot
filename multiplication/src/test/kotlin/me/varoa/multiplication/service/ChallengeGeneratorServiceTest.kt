package me.varoa.multiplication.service

import me.varoa.multiplication.domain.Challenge
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.random.Random

@ExtendWith(MockitoExtension::class)
class ChallengeGeneratorServiceTest {
    private lateinit var service: ChallengeGeneratorService

    @Spy
    private lateinit var random: Random

    @BeforeEach
    fun setUp() {
        service = ChallengeGeneratorServiceImpl(random)
    }

    @Test
    fun generateRandomFactorIsBetweenExpectedLimits() {
        // 89 is max - min range
        given(random.nextInt(89)).willReturn(20, 30)
        val challenge = service.randomChallenge()
        then(challenge).isEqualTo(Challenge(31, 41))
    }
}
