package com.mattg.wahuntingregs.data

data class ResultEntry(
    val generalSeasonDates: List<SeasonDate>,
    val lateGeneralDates: List<SeasonDate>,
    val youthGeneralDates: List<SeasonDate>,
    val specialSeasonDates: List<SeasonDate>,
    val restrictions: List<String>,
    val specialInformation: List<String>
)