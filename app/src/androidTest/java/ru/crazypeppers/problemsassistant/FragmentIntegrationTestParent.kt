package ru.crazypeppers.problemsassistant

import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.dto.Problem
import java.io.File

abstract class FragmentIntegrationTestParent {
    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @After
    fun clearData() {
        val data = (activityRule.activity.application as DataApplication).data
        data.clearData()
        val problem = Problem(
            "test",
            mutableListOf()
        )
        val card = Card(
            "ttest",
            mutableListOf()
        )
        problem.add(card)
        data.add(problem)
        data.actualize()
        val listView = activityRule.activity.findViewById<ListView>(android.R.id.list)
        if (listView != null) {
            activityRule.activity.runOnUiThread {
                (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            }
        }
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            val file = File("/data/data/ru.crazypeppers.problemsassistant/files/data.json")
            val file2 = File("/data/data/ru.crazypeppers.problemsassistant/files/data1.json")
            file.renameTo(file2)
        }

        @JvmStatic
        @AfterClass
        fun afterClass() {
            val file = File("/data/data/ru.crazypeppers.problemsassistant/files/data1.json")
            val file2 = File("/data/data/ru.crazypeppers.problemsassistant/files/data.json")
            file.renameTo(file2)
        }
    }
}