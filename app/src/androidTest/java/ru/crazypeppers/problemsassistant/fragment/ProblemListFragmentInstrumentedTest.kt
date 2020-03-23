package ru.crazypeppers.problemsassistant.fragment

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf.allOf
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.testUtils.Matchers.withListSize
import java.io.File


@RunWith(AndroidJUnit4::class)
class ProblemListFragmentInstrumentedTest {
    @get:Rule
    var activityRule = ActivityTestRule(
        MainActivity::class.java
    )

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

    @Test
    fun testListProblem() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(R.id.problemPoint)).perform(click())
        onView(withText(R.string.informationTitle))
            .inRoot(isDialog()) // <---
            .check(matches(isDisplayed()))
        onView(withId(android.R.id.button3)).check(matches(withText(R.string.okButton))).perform(
            click()
        )
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onData(anything()).atPosition(0)
            .perform(swipeUp(), swipeUp(), swipeUp(), swipeUp(), swipeUp())
        onView(withId(R.id.inputAdd)).check(matches(not(isCompletelyDisplayed())))
    }

    @Test
    fun testActivityTitle() {
        val toolbarTitle =
            getInstrumentation().targetContext.getString(R.string.problem_list_fragment_label)
        onView(
            allOf(
                isAssignableFrom(TextView::class.java), withParent(
                    isAssignableFrom(
                        Toolbar::class.java
                    )
                )
            )
        ).check(matches(withText(toolbarTitle)))
    }

    @Test
    fun testFloatingButton() {
        onView(withId(R.id.inputAdd)).check(matches(isDisplayed()))
    }
}