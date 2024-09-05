package me.varoa.multiplication.service

import me.varoa.multiplication.domain.Challenge
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class ChallengeGeneratorServiceImpl(
    private val random: Random = Random,
) : ChallengeGeneratorService {
    companion object {
        const val MINIMUM_FACTOR = 11
        const val MAXIMUM_FACTOR = 100
    }

    private fun next(): Int = random.nextInt(MAXIMUM_FACTOR - MINIMUM_FACTOR) + MINIMUM_FACTOR

    override fun randomChallenge(): Challenge = Challenge(next(), next())
}
