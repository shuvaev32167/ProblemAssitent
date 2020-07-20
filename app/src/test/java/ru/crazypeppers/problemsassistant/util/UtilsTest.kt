package ru.crazypeppers.problemsassistant.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.dto.Point

class UtilsTest {
    @Test
    fun testCreateProblemStub() {
        val problem = createProblemStub()
        assertNotNull(problem)
        assertNotNull(problem.name)
        assertNotNull(problem.cards)
        assertTrue { problem.cards.isNotEmpty() }
        val linearCard = problem[0] as LinearCard
        assertNotNull(linearCard)
        assertNotNull(linearCard.name)
        assertNotNull(linearCard.description)
        assertNotNull(linearCard.parent)
        assertSame(problem, linearCard.parent)
        assertNotNull(linearCard.points)
        assertTrue { linearCard.points.isEmpty() }
        linearCard.add(Point(5))
        assertNotNull(linearCard[0])
        assertNotNull(linearCard[0].score)
        assertNotNull(linearCard[0].cdate)
        assertNotNull(linearCard[0].parent)
        assertSame(linearCard, linearCard[0].parent)
    }
}