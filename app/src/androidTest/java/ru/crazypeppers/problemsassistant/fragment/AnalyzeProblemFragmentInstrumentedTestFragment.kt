package ru.crazypeppers.problemsassistant.fragment

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.FragmentIntegrationTestParent
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.data.DATE_FORMAT
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.testUtils.Matchers.withTextColor
import ru.crazypeppers.problemsassistant.util.toStringRound
import java.util.*

@RunWith(AndroidJUnit4::class)
class AnalyzeProblemFragmentInstrumentedTestFragment : FragmentIntegrationTestParent() {
    @Before
    fun comeInFragment() {
        val data = (activityRule.activity.application as DataApplication).data
        (data[0][0] as LinearCard).add(Point(5))
        data[0].add(LinearCard("1234", "4321", listOf(Point(-5)), data[0]))
        onView(withId(android.R.id.text1)).perform(longClick())
        onView(withText(R.string.popupAnalyze))
            .inRoot(isPlatformPopup()).perform(click())
    }

    @Test
    fun testMotivationList() {
        onView(withText(R.string.analyzeLinearProblemTabAdvantages)).perform(click())
        onView(withText("example")).check(matches(withTextColor(0xFF00FA00.toInt())))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun testAnchorList() {
        onView(withText(R.string.analyzeLinearProblemTabAdvantages)).perform(swipeLeft())
        onView(withText(R.string.analyzeLinearProblemTabDisadvantages)).perform(click())
        onView(withText("1234")).check(matches(withTextColor(0xFFFA0000.toInt())))
            .check(matches(isCompletelyDisplayed()))
        onView(withText("4321")).check(matches(withTextColor(0xFFFA0000.toInt())))
            .check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun testSummeryInformation() {
        onView(withId(R.id.summeryInformation)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.avgPointsLabel
                        ), "example", 0f.toStringRound(2)
                    )
                )
            )
        ).check(matches(isCompletelyDisplayed()))

        onView(withId(R.id.countMotivation)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.countMotivationLabel
                        ), "1"
                    )
                )
            )
        ).check(matches(isCompletelyDisplayed()))

        onView(withId(R.id.countAnchor)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.countAnchorLabel
                        ), "1"
                    )
                )
            )
        ).check(matches(isCompletelyDisplayed()))

        onView(withId(R.id.summeryCountCard)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.summaryCountCardLabel
                        ), 2
                    )
                )
            )
        ).check(matches(isCompletelyDisplayed()))

        onView(withId(R.id.dateOfFirstProblemAssessment)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.dateOfFirstProblemAssessmentLabel
                        ), DATE_FORMAT.format(Date())
                    )
                )
            )
        ).check(matches(isCompletelyDisplayed()))

        onView(withId(R.id.dateOfLastProblemAssessment)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.dateOfLastProblemAssessmentLabel
                        ), DATE_FORMAT.format(Date())
                    )
                )
            )
        ).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun testTitle() {
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.analyzeProblemFragmentLabel)))
    }
}