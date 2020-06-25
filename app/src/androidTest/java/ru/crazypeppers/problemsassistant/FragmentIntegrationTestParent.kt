package ru.crazypeppers.problemsassistant

import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.util.createProblemStub
import java.io.File

abstract class FragmentIntegrationTestParent {
    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @After
    fun clearData() {
        val data = (activityRule.activity.application as DataApplication).data
        data.clearData()
        data.add(createProblemStub())
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            val file =
                File("/data/data/ru.crazypeppers.problemsassistant/files/data.problemAssistant")
            val file2 =
                File("/data/data/ru.crazypeppers.problemsassistant/files/data1.problemAssistant")
            if (file.exists() && !file2.exists())
                file.renameTo(file2)
        }

        @JvmStatic
        @AfterClass
        fun afterClass() {
            val file =
                File("/data/data/ru.crazypeppers.problemsassistant/files/data1.problemAssistant")
            val file2 =
                File("/data/data/ru.crazypeppers.problemsassistant/files/data.problemAssistant")
            if (file.exists())
                file.renameTo(file2)
        }
    }
}