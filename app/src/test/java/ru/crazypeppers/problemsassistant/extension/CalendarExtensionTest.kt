package ru.crazypeppers.problemsassistant.extension

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Test
import java.util.*

internal class CalendarExtensionTest {

    @Test
    fun withoutTime() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 5)
        assertEquals(5, calendar.get(Calendar.HOUR))
        calendar.withoutTime()
        assertEquals(0, calendar.get(Calendar.HOUR))
        assertEquals(0, calendar.get(Calendar.MINUTE))
        assertEquals(0, calendar.get(Calendar.SECOND))
        assertEquals(0, calendar.get(Calendar.MILLISECOND))
    }

    @Test
    fun diffDay() {
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = calendar1.timeInMillis
        calendar2.add(Calendar.DATE, 1)
        assertEquals(-1, calendar1.diffDay(calendar2))
        assertEquals(1, calendar2.diffDay(calendar1))
        calendar2.add(Calendar.DATE, -2)
        assertEquals(1, calendar1.diffDay(calendar2))
        assertEquals(-1, calendar2.diffDay(calendar1))
    }

    @Test
    fun addDayAsNewInstance() {
        val calendar1 = Calendar.getInstance()
        val calendar2 = calendar1.addDayAsNewInstance(1)
        assertNotSame(calendar1, calendar2)
        assertEquals(1, calendar2.diffDay(calendar1))
    }
}