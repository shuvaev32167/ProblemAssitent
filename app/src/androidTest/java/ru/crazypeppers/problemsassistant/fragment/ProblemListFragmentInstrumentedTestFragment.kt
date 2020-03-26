package ru.crazypeppers.problemsassistant.fragment

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf.allOf
import org.junit.Test
import org.junit.runner.RunWith
import ru.crazypeppers.problemsassistant.FragmentIntegrationTestParent
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.testUtils.Matchers.withListSize


@RunWith(AndroidJUnit4::class)
class ProblemListFragmentInstrumentedTestFragment : FragmentIntegrationTestParent() {
    @Test
    fun testListProblem() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(android.R.id.text1)).check(matches(withText("test")))
        onView(withId(R.id.problemPoint)).check(matches(withText("0.00"))).perform(click())
        onView(withText(R.string.informationTitle))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withId(android.R.id.button3)).check(matches(withText(R.string.okButton)))
            .perform(click())
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun testAddProblem() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onData(anything()).atPosition(0).onChildView(withId(R.id.problemPoint))
            .check(matches(withText("0.00")))
        onData(anything()).atPosition(0).onChildView(withId(android.R.id.text1))
            .check(matches(withText("test")))
        createNewProblem()
        onView(withId(android.R.id.list)).check(matches(withListSize(2)))
        onData(anything()).atPosition(0).onChildView(withId(R.id.problemPoint))
            .check(matches(withText("0.00")))
        onData(anything()).atPosition(0).onChildView(withId(android.R.id.text1))
            .check(matches(withText("test")))
        onData(anything()).atPosition(1).onChildView(withId(R.id.problemPoint))
            .check(matches(withText("0.00")))
        onData(anything()).atPosition(1).onChildView(withId(android.R.id.text1))
            .check(matches(withText("espressoProblem")))
    }

    @Test
    fun testAnimateButtonWithScroll() {
        onData(anything()).atPosition(0).perform(swipeUp(), swipeUp(), swipeUp(), swipeUp())
        onView(withId(R.id.inputAdd)).check(matches(not(isCompletelyDisplayed())))
    }

    private fun createNewProblem(problemName: String = "espressoProblem") {
        onView(withId(R.id.inputAdd)).perform(click()).check(matches(isClickable()))
        onView(withId(R.id.problemName)).check(matches(withHint(R.string.label_edit_problem_name)))
        onView(withId(R.id.problemName)).check(matches(withText("")))
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.problem_new_fragment_label)))

        onView(withId(R.id.problemName)).perform(replaceText(problemName))
        onView(withId(R.id.saveButton)).perform(click())
    }

    @Test
    fun testActivityTitle() {
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.problem_list_fragment_label)))
    }

    @Test
    fun testFloatingButton() {
        onView(withId(R.id.inputAdd)).check(matches(isDisplayed()))
    }
}