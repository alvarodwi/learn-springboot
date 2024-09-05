package me.varoa.gamification.domain

enum class BadgeType(
    val description: String,
) {
    BRONZE("Bronze"),
    SILVER("Silver"),
    GOLD("Gold"),

    // other badges
    FIRST_WON("First time"),
    LUCKY_NUMBER("Lucky number"),
}
