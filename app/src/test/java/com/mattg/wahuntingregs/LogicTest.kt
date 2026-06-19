package com.mattg.wahuntingregs

import com.mattg.wahuntingregs.utils.extractGmuNumber
import com.mattg.wahuntingregs.utils.isHarvestRestriction
import org.junit.Assert.*
import org.junit.Test

class LogicTest {

    // Verify correct GMU # is extracted from string
    @Test
    fun extractGmuNumber() {
        val result = extractGmuNumber("101 - Sherman")

        assertEquals("101", result)
    }

    // Verify harvest restrictions "Any buck"
    @Test
    fun isHarvestRestrictionAnyBuck() {
        val result = isHarvestRestriction("Any Buck")

        assertTrue(result)
    }

    // Verify general restrictions "Closed in GMU"
    @Test
    fun isHarvestRestriction_returns_false_for_general_gmu_note() {
        val result = isHarvestRestriction("Closed in certain GMUs")

        assertTrue(result)
    }
}