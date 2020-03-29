package ru.crazypeppers.problemsassistant.fragment

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.crazypeppers.problemsassistant.FragmentIntegrationTestParent
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.data.DATE_FORMAT
import ru.crazypeppers.problemsassistant.testUtils.Matchers
import ru.crazypeppers.problemsassistant.toStringRound
import java.util.*

@RunWith(AndroidJUnit4::class)
class AnalyzeProblemFragmentInstrumentedTestFragment : FragmentIntegrationTestParent() {
    @Before
    fun comeInFragment() {
        onView(withId(android.R.id.text1)).perform(longClick())
        onView(withText(R.string.popupAnalyze))
            .inRoot(isPlatformPopup()).perform(click())
    }

//    @Test
    fun testMotivationList(){
        onView(withText(R.string.analyzeProblemTab2)).perform(click())
    // Почему-то 2 листа...
        onView(withId(android.R.id.list)).check(matches(Matchers.withListSize(0)))
    }

//    @Test
    fun testAnchorList(){
        onView(withText(R.string.analyzeProblemTab3)).perform(click())
    // Почему-то 2 листа...
        onView(withId(android.R.id.list)).check(matches(Matchers.withListSize(0)))
    }

    @Test
    fun testSummeryInformation() {
        onView(withId(R.id.summeryInformation)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.avgPointsLabel
                        ), "test", 0f.toStringRound(2)
                    )
                )
            )
        )

        onView(withId(R.id.countMotivation)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.countMotivationLabel
                        ), "0"
                    )
                )
            )
        )

        onView(withId(R.id.countAnchor)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.countAnchorLabel
                        ), "0"
                    )
                )
            )
        )

        onView(withId(R.id.summeryCountCard)).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(
                            R.string.summaryCountCardLabel
                        ), "1"
                    )
                )
            )
        )

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
        )

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
        )
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