package me.varoa.multiplication.domain.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class ChallengeAttemptDTO(
    @get:Min(1) @get:Max(100)
    val factorA: Int,
    @Min(1) @get:Max(100)
    val factorB: Int,
    @get:NotBlank
    val alias: String,
    @get:Positive(message = "How could you possibly get a negative result here? Try again.")
    val guess: Int,
)
