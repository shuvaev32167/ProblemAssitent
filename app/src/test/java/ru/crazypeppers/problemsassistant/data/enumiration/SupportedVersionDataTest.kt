package ru.crazypeppers.problemsassistant.data.enumiration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData.Companion.compare
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData.Companion.inc
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData.ONE

internal class SupportedVersionDataTest {

    @Test
    fun testGetCode() {
        assertEquals(1, ONE.code)
    }

    @Test
    fun testLastVersion() {
        assertEquals(ONE, SupportedVersionData.lastVersion())
    }

    @Test
    fun testInc() {
        var supportedVersionData: SupportedVersionData? = null

        assertEquals(ONE, supportedVersionData.inc())
        assertEquals(null, supportedVersionData)
        supportedVersionData = supportedVersionData.inc()
        assertEquals(ONE, supportedVersionData.inc())

    }

    @Test
    fun testCompare() {
        val supportedVersionData: SupportedVersionData? = null

        assertEquals(0, supportedVersionData.compare(null))
        assertEquals(-1, supportedVersionData.compare(ONE))
        assertEquals(1, ONE.compare(null))
        assertEquals(0, ONE.compare(ONE))

    }
}