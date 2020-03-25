package ru.crazypeppers.problemsassistant.fragment

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.Matchers
import org.hamcrest.Matchers.anything
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.crazypeppers.problemsassistant.FragmentIntegrationTestParent
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.testUtils.Matchers.setProgress
import ru.crazypeppers.problemsassistant.testUtils.Matchers.withListSize
import ru.crazypeppers.problemsassistant.testUtils.Matchers.withTextColor

@RunWith(AndroidJUnit4::class)
class CardListFragmentInstrumentedTestFragment : FragmentIntegrationTestParent() {

    @Before
    fun comeInFragment() {
        onData(anything()).atPosition(0).perform(click())
    }

    @Test
    fun testAnimateButtonWithScroll() {
        onData(anything()).atPosition(0).perform(swipeUp(), swipeUp(), swipeUp(), swipeUp())
        onView(withId(R.id.inputAdd)).check(matches(Matchers.not(isCompletelyDisplayed())))
    }

    @Test
    fun testListCard() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(android.R.id.text1)).check(matches(withText("ttest"))).check(
            matches(
                withTextColor(0xFF000000.toInt())
            )
        )
    }

    @Test
    fun testAddCard() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(android.R.id.text1)).check(matches(withText("ttest"))).check(
            matches(
                withTextColor(0xFF000000.toInt())
            )
        )
        createNewCard()
        onView(withId(android.R.id.list)).check(matches(withListSize(2)))
        onData(anything()).atPosition(1).onChildView(withId(android.R.id.text1)).check(
            matches(
                withText("espressoCard")
            )
        ).check(matches(withTextColor(0xFF00BE00.toInt())))
        onData(anything()).atPosition(1).onChildView(withId(android.R.id.text2)).check(
            matches(
                withText("espressoCardDescription")
            )
        ).check(matches(withTextColor(0xFF00BE00.toInt())))
    }

    private fun createNewCard(
        cardName: String = "espressoCard",
        cardDescription: String = "espressoCardDescription"
    ) {
        onView(withId(R.id.inputAdd)).perform(click()).check(matches(isClickable()))
        onView(withId(R.id.cardName)).check(matches(withHint(R.string.label_edit_card_name)))
            .check(matches(withText("")))
        onView(withId(R.id.cardDescription)).check(matches(withHint(R.string.label_edit_card_description)))
            .check(matches(withText("")))

        onView(withId(R.id.seekBarVariants)).perform(setProgress(0))
        onView(withId(R.id.scoreSeekBar)).check(matches(withText("-5")))
        onView(withId(R.id.seekBarVariants)).perform(setProgress(10))
        onView(withId(R.id.scoreSeekBar)).check(matches(withText("5")))

        onView(withId(R.id.cardName)).perform(replaceText(cardName))
        onView(withId(R.id.cardDescription)).perform(replaceText(cardDescription))
        onView(withId(R.id.saveButton)).perform(click())
    }

    @Test
    fun testFloatingButton() {
        onView(withId(R.id.inputAdd))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testActivityTitle() {
        val toolbarTitle =
            getInstrumentation().targetContext.getString(R.string.card_list_fragment_label)
        onView(
            AllOf.allOf(
                isAssignableFrom(TextView::class.java), withParent(
                    isAssignableFrom(
                        Toolbar::class.java
                    )
                )
            )
        ).check(matches(withText(toolbarTitle)))
    }

}