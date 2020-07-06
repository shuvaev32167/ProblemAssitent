package ru.crazypeppers.problemsassistant.extension

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class ListExtensionTest {

    private lateinit var list: List<Int>

    @BeforeEach
    fun setUp() {
        list = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    }

    @Test
    fun removeAt() {
        assertEquals(10, list.size)
        assertEquals(1, list.first())
        list.removeAt(0)
        assertEquals(9, list.size)
        assertEquals(2, list.first())

        list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        list.removeAt(0)
        assertEquals(10, list.size)
        assertEquals(1, list.first())
    }

    @Test
    fun add() {
        assertEquals(10, list.size)
        assertEquals(10, list.last())
        list.add(11)
        assertEquals(11, list.size)
        assertEquals(11, list.last())

        list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        list.add(11)
        assertEquals(10, list.size)
        assertEquals(10, list.last())
    }

    @Test
    fun clear() {
        assertEquals(10, list.size)
        list.clear()
        assertEquals(0, list.size)

        list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        list.clear()
        assertEquals(10, list.size)
    }

    @Test
    fun addAll() {
        assertEquals(10, list.size)
        assertEquals(1, list.first())
        assertEquals(10, list.last())
        list.addAll(list)
        assertEquals(20, list.size)
        assertEquals(1, list.first())
        assertEquals(10, list.last())

        list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        assertEquals(10, list.size)
        assertEquals(1, list.first())
        assertEquals(10, list.last())
        list.addAll(list)
        assertEquals(10, list.size)
        assertEquals(1, list.first())
        assertEquals(10, list.last())
    }

    @Test
    fun isMutable() {
        assertTrue(list.isMutable())
        list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        assertFalse(list.isMutable())

        assertTrue(LinkedList<Int>().also { it.add(1) }.isMutable())
    }
}