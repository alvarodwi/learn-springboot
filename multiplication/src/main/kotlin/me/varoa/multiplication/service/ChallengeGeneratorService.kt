package me.varoa.multiplication.service

import me.varoa.multiplication.domain.Challenge

interface ChallengeGeneratorService {
    fun randomChallenge(): Challenge
}
