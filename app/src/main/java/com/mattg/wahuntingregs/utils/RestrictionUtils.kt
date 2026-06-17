package com.mattg.wahuntingregs.utils

// Differentiate between restrictions.  Using to prevent displaying harvest restrictions vs GMU restrictions in incorrect places
fun isHarvestRestriction(text: String): Boolean {
    val value = text.lowercase()

    return value.contains("any deer") ||
            value.contains("any buck") ||
            value.contains("antlerless") ||
            value.contains("3 pt") ||
            value.contains("3-point") ||
            value.contains("4 pt") ||
            value.contains("4-point") ||
            value.contains("spike")
}