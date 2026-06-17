package com.mattg.wahuntingregs.data

data class HuntingSchema(
    val tagTypes: List<String>,
    val speciesTypes: List<String>,
    val gmus: List<Gmu>
)