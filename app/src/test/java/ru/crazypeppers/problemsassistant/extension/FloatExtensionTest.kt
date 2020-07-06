package ru.crazypeppers.problemsassistant.extension

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FloatExtensionTest {

    @Test
    fun roundTo() {
        val sourceFloat = 123.123456789f
        assertEquals(123.12346f, sourceFloat.roundTo(6))
        assertEquals(123.12346f, sourceFloat.roundTo(5))
        assertEquals(123.1235f, sourceFloat.roundTo(4))
        assertEquals(123.124f, sourceFloat.roundTo(3))
        assertEquals(123.13f, sourceFloat.roundTo(2))
        assertEquals(123.2f, sourceFloat.roundTo(1))
        assertEquals(124f, sourceFloat.roundTo(0))
    }

    @Test
    fun toStringRound() {
        val sourceFloat = 123.123456789f
        assertEquals("123.123460", sourceFloat.toStringRound(6))
        assertEquals("123.12346", sourceFloat.toStringRound(5))
        assertEquals("123.1235", sourceFloat.toStringRound(4))
        assertEquals("123.124", sourceFloat.toStringRound(3))
        assertEquals("123.13", sourceFloat.toStringRound(2))
        assertEquals("123.2", sourceFloat.toStringRound(1))
        assertEquals("124", sourceFloat.toStringRound(0))
    }
}