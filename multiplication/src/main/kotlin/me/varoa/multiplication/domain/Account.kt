package me.varoa.multiplication.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class Account(
    @Id @GeneratedValue
    val id: Long = 0,
    val alias: String,
)
