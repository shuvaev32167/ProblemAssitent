package ru.crazypeppers.problemsassistant.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData

internal class DataTest {
    @Test
    fun removeAt() {
        val problem = Problem("")
        var data = Data(listOf(problem))
        assertEquals(1, data.problems.size)
        data.removeAt(0)
        assertEquals(1, data.problems.size)

        data = Data(mutableListOf(problem))
        assertEquals(1, data.problems.size)
        data.removeAt(0)
        assertEquals(0, data.problems.size)

        assertThrows(IndexOutOfBoundsException::class.java) { data.removeAt(0) }
    }

    @Test
    fun get() {
        val problem = Problem("")
        val data = Data(listOf(problem))
        assertSame(problem, data[0])
        assertSame(problem, data.problems[0])
    }

    @Test
    fun add() {
        val problem = Problem("")
        var data = Data(listOf(problem))
        assertEquals(1, data.problems.size)
        data.add(Problem(""))
        data.add(problem)
        assertEquals(1, data.problems.size)
        assertSame(problem, data[0])

        data = Data(mutableListOf(problem))
        assertEquals(1, data.problems.size)
        data.add(Problem(""))
        data.add(problem)
        assertEquals(3, data.problems.size)
        assertSame(problem, data[0])
        assertSame(problem, data[2])
    }

    @Test
    fun hasProblemWithName() {
        val problem = Problem("")
        val data = Data(listOf(problem))
        assertTrue(data.hasProblemWithName(""))
        assertFalse(data.hasProblemWithName("1"))
        assertFalse(data.hasProblemWithName("0", problem))
    }

    @Test
    fun clearData() {
        val problem = Problem("")
        var data = Data(listOf(problem))
        assertEquals(1, data.problems.size)
        data.clearData()
        assertEquals(1, data.problems.size)
        assertSame(problem, data[0])

        data = Data(mutableListOf(problem))
        assertEquals(1, data.problems.size)
        data.clearData()
        assertEquals(0, data.problems.size)
    }

    @Test
    fun actualize() {
        val problem = Problem("")
        val data = Data(listOf(problem))
        assertNull(data.version)
        data.actualize()
        assertEquals(SupportedVersionData.ONE, data.version)
    }
}