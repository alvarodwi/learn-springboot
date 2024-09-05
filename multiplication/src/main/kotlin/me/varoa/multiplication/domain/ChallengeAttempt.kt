package me.varoa.multiplication.domain

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class ChallengeAttempt(
    @Id @GeneratedValue
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    val account: Account,
    val factorA: Int,
    val factorB: Int,
    val resultAttempt: Int,
    val correct: Boolean,
)
