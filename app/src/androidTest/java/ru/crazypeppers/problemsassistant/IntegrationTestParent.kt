package ru.crazypeppers.problemsassistant

import org.junit.AfterClass
import org.junit.BeforeClass
import java.io.File

open class IntegrationTestParent {
    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            val file = File("/data/data/ru.crazypeppers.problemsassistant/files/data.json")
            val file2 = File("/data/data/ru.crazypeppers.problemsassistant/files/data1.json")
            file.renameTo(file2)
            file.delete()
        }

        @JvmStatic
        @AfterClass
        fun afterClass() {
            val file = File("/data/data/ru.crazypeppers.problemsassistant/files/data1.json")
            val file2 = File("/data/data/ru.crazypeppers.problemsassistant/files/data.json")
            file.renameTo(file2)
            file.delete()
        }
    }
}