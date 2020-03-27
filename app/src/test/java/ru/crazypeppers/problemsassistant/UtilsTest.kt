package ru.crazypeppers.problemsassistant

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.Calendar.*

class UtilsTest {
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

    @Test
    fun withoutTime() {
        val calendar = getInstance()
        calendar.set(HOUR, 5)
        assertEquals(5, calendar.get(HOUR))
        calendar.withoutTime()
        assertEquals(0, calendar.get(HOUR))
        assertEquals(0, calendar.get(MINUTE))
        assertEquals(0, calendar.get(SECOND))
        assertEquals(0, calendar.get(MILLISECOND))
    }

    @Test
    fun diffDay() {
        val calendar1 = getInstance()
        val calendar2 = getInstance()
        calendar2.timeInMillis = calendar1.timeInMillis
        calendar2.add(DATE, 1)
        assertEquals(-1, calendar1.diffDay(calendar2))
        assertEquals(1, calendar2.diffDay(calendar1))
        calendar2.add(DATE, -2)
        assertEquals(1, calendar1.diffDay(calendar2))
        assertEquals(-1, calendar2.diffDay(calendar1))
    }

    @Test
    fun addDayAsNewInstance() {
        val calendar1 = getInstance()
        val calendar2 = calendar1.addDayAsNewInstance(1)
        assertNotSame(calendar1, calendar2)
        assertEquals(1, calendar2.diffDay(calendar1))
    }

    @Test
    fun testCreateProblemStub() {
        val problem = createProblemStub()
        assertNotNull(problem)
        assertNotNull(problem.name)
        assertNotNull(problem.cards)
        assertTrue { problem.cards.isNotEmpty() }
        assertNotNull(problem[0])
        assertNotNull(problem[0].name)
        assertNotNull(problem[0].description)
        assertNotNull(problem[0].parent)
        assertSame(problem, problem[0].parent)
        assertNotNull(problem[0].points)
        assertTrue { problem[0].points.isNotEmpty() }
        assertNotNull(problem[0][0])
        assertNotNull(problem[0][0].score)
        assertNotNull(problem[0][0].cdate)
        assertNotNull(problem[0][0].parent)
        assertSame(problem[0], problem[0][0].parent)
    }
}