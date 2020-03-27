package ru.crazypeppers.problemsassistant.fragment

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.anything
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.crazypeppers.problemsassistant.FragmentIntegrationTestParent
import ru.crazypeppers.problemsassistant.R

@RunWith(AndroidJUnit4::class)
class AnalyzeCardFragmentInstrumentedTestFragment : FragmentIntegrationTestParent() {
    @Before
    fun comeInFragment() {
        onData(anything()).atPosition(0).perform(click())
        onView(withId(android.R.id.text1)).perform(longClick())
        onView(withText(R.string.graphScoreChanged)).inRoot(isPlatformPopup()).perform(click())
    }

    @Test
    fun testLabel() {
        val textGraphLabel = String.format(
            activityRule.activity.getString(R.string.analyzeCardFragmentGraphLabel),
            "ttest"
        )
        onView(withId(R.id.graphLabel)).check(matches(withText(textGraphLabel)))
    }

    @Test
    fun testTitle() {
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.analyzeCardFragmentLabel)))
    }
}