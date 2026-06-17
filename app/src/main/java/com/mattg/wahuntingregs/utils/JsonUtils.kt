package com.mattg.wahuntingregs.utils

import com.mattg.wahuntingregs.data.SeasonDate

// GMU By number
fun extractGmuNumber(gmu: String): String {
    return gmu.substringBefore("-").trim()
}

// JSON Array (Strings) to Kotlin List
fun jsonArrayToStringList(array: org.json.JSONArray?): List<String> {
    if (array == null) return emptyList()

    val list = mutableListOf<String>()

    for (i in 0 until array.length()) {
        list.add(array.getString(i))
    }

    return list
}

// JSON Array (Objects) to Kotlin List
fun jsonArrayToSeasonDateList(array: org.json.JSONArray?): List<SeasonDate> {
    if (array == null) return emptyList()

    val list = mutableListOf<SeasonDate>()

    for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        list.add(
            SeasonDate(
                dateRange = obj.optString("dateRange"),
                restriction = obj.optString("restriction"),
                seasonLabel = obj.optString("seasonLabel")
            )
        )
    }

    return list
}