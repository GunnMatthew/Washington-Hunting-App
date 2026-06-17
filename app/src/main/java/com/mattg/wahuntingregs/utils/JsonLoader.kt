package com.mattg.wahuntingregs.utils

import android.content.Context
import com.mattg.wahuntingregs.data.Gmu
import com.mattg.wahuntingregs.data.HuntingSchema
import com.mattg.wahuntingregs.data.ResultEntry
import org.json.JSONObject

// JSON Loader
fun loadHuntingSchema(context: Context): HuntingSchema {
    val jsonString = context.assets.open("HuntingSchema26.json")
        .bufferedReader()
        .use { it.readText() }
    val jsonObject = JSONObject(jsonString)

    // JSON Arrays -> Kotlin lists
    val tagTypesJson = jsonObject.getJSONArray("tagTypes")
    val tagTypes = mutableListOf<String>()

    for (i in 0 until tagTypesJson.length()) {
        tagTypes.add(tagTypesJson.getString(i))
    }

    val speciesJson = jsonObject.getJSONArray("speciesTypes")
    val speciesTypes = mutableListOf<String>()
    for (i in 0 until speciesJson.length()) {
        speciesTypes.add(speciesJson.getString(i))
    }

    val gmusJson = jsonObject.getJSONArray("gmus")
    val gmus = mutableListOf<Gmu>()
    for (i in 0 until gmusJson.length()) {
        val gmuObject = gmusJson.getJSONObject(i)
        gmus.add(
            Gmu(
                number = gmuObject.getInt("number"),
                name = gmuObject.getString("name")
            )
        )
    }

    return HuntingSchema(
        tagTypes = tagTypes,
        speciesTypes = speciesTypes,
        gmus = gmus
    )
}

// Data loading
fun loadResultEntry(
    context: Context,
    tagType: String,
    species: String,
    gmu: String
): ResultEntry? {
    val jsonString = context.assets.open("HuntingSchema26.json")
        .bufferedReader()
        .use { it.readText() }
    val jsonObject = JSONObject(jsonString)
    val seasonData = jsonObject.getJSONArray("seasonData")
    val selectedGmuNumber = extractGmuNumber(gmu)

    for (i in 0 until seasonData.length()) {
        val entry = seasonData.getJSONObject(i)

        val entryTagType = entry.getString("tagType")
        val entrySpecies = entry.getString("species")
        val entryGmu = entry.getJSONObject("gmu")
        val entryGmuNumber = entryGmu.get("number").toString()

        if (entryTagType == tagType && entrySpecies == species && entryGmuNumber == selectedGmuNumber) {
            return ResultEntry(
                generalSeasonDates = jsonArrayToSeasonDateList(entry.optJSONArray("generalSeasonDates")),
                lateGeneralDates = jsonArrayToSeasonDateList(entry.optJSONArray("lateGeneralDates")),
                youthGeneralDates = jsonArrayToSeasonDateList(entry.optJSONArray("youthGeneralDates")),
                specialSeasonDates = jsonArrayToSeasonDateList(entry.optJSONArray("specialSeasonDates")),
                restrictions = jsonArrayToStringList(entry.optJSONArray("restrictions")),
                specialInformation = jsonArrayToStringList(entry.optJSONArray("specialInformation"))
            )
        }
    }

    return null
}