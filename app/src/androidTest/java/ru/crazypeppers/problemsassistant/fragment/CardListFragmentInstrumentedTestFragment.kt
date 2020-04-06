package ru.crazypeppers.problemsassistant.fragment

import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.crazypeppers.problemsassistant.FragmentIntegrationTestParent
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.testUtils.Matchers.correctNumberOfItems
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
    fun testEditCard() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(R.id.cardName)).check(matches(withText("ttest"))).perform(longClick())

        onView(isAssignableFrom(ListView::class.java)).check(matches(correctNumberOfItems(3)))

        onView(withText(R.string.popupEdit)).inRoot(isPlatformPopup())
            .check(matches(isDisplayed())).perform(click())

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.advantage_disadvantageEditLabel)))
        onView(withId(R.id.cardName)).check(matches(withHint(R.string.label_edit_card_name)))
            .check(matches(withText("ttest"))).perform(replaceText("123"))
        onView(withId(R.id.cardDescription)).check(matches(withHint(R.string.label_edit_card_description)))
            .check(matches(withText(""))).perform(replaceText("321"))

        onView(withId(R.id.cancelButton)).check(matches(withText(R.string.cancelButton)))
        onView(withId(R.id.saveButton)).check(matches(withText(R.string.saveButton)))
            .perform(click())

        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(R.id.cardName)).check(matches(withText("123")))
        onView(withId(R.id.cardDescription)).check(matches(withText("321")))
    }

    @Test
    fun testRemoveElementList() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(R.id.cardName)).check(matches(withText("ttest"))).perform(longClick())

        onView(isAssignableFrom(ListView::class.java)).check(matches(correctNumberOfItems(3)))

        onView(withText(R.string.popupDelete)).inRoot(isPlatformPopup())
            .check(matches(isDisplayed())).perform(click())

        onView(withText(R.string.removeItemConfirm))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button1)).check(matches(withText(R.string.yesButton)))
        onView(withId(android.R.id.button2)).check(matches(withText(R.string.noButton)))
            .perform(click())

        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(R.id.cardName)).check(matches(withText("ttest"))).perform(longClick())
        onView(withText(R.string.popupDelete)).inRoot(isPlatformPopup())
            .check(matches(isDisplayed())).perform(click())

        onView(withText(R.string.removeItemConfirm))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button1)).check(matches(withText(R.string.yesButton)))
            .perform(click())

        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(0)))
    }

    @Test
    fun testAssessmentCard() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onData(anything()).atPosition(0).onChildView(withId(R.id.cardName))
            .check(matches(withText("ttest"))).check(matches(withTextColor(0xFF000000.toInt())))
            .perform(click())

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.advantage_disadvantageFragmentLabel)))

        onView(withId(R.id.seekBarVariants)).check(matches(not(isDisplayed())))
        onView(withId(R.id.scoreSeekBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.saveButton)).check(matches(not(isDisplayed())))
        onView(withId(R.id.cancelButton)).check(matches(not(isDisplayed())))

        onView(withId(R.id.cardName)).check(matches(withText("ttest")))
            .check(matches(withTextColor(0xFF000000.toInt())))
        onView(withId(R.id.cardDescription)).check(matches(not(isDisplayed())))

        onView(withId(R.id.informationText)).check(matches(isDisplayed())).check(
            matches(
                withText(
                    String.format(
                        activityRule.activity.getString(R.string.informationTextNotScore),
                        "ttest"
                    )
                )
            )
        )

        onView(withId(R.id.confirmButton)).check(matches(not(isDisplayed())))
        onView(withId(R.id.makeNewScoreButton)).check(matches(isDisplayed()))
            .check(matches(withText(R.string.makeScoreButton))).perform(click())
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.informationText)).check(matches(not(isDisplayed())))

        onView(withId(R.id.seekBarVariants)).perform(setProgress(0))
        onView(withId(R.id.scoreSeekBar)).check(matches(withText("-5")))

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.disadvantageFragmentLabel)))

        onView(withId(R.id.seekBarVariants)).perform(setProgress(10))
        onView(withId(R.id.scoreSeekBar)).check(matches(withText("5")))

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.advantageFragmentLabel)))

        onView(withId(R.id.saveButton)).perform(click())

        Espresso.pressBack()

        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onData(anything()).atPosition(0).onChildView(withId(R.id.cardName))
            .check(matches(withText("ttest"))).check(matches(withTextColor(0xFF00FA00.toInt())))

        Espresso.pressBack()
        onView(withId(R.id.problemPoint)).check(matches(withText("5.00")))
    }

    @Test
    fun testListCard() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(R.id.cardName)).check(matches(withText("ttest"))).check(
            matches(withTextColor(0xFF000000.toInt()))
        )
    }

    @Test
    fun testAddCard() {
        onView(withId(android.R.id.list)).check(matches(isDisplayed()))
        onView(withId(android.R.id.list)).check(matches(withListSize(1)))
        onView(withId(R.id.cardName)).check(matches(withText("ttest"))).check(
            matches(withTextColor(0xFF000000.toInt()))
        )
        createNewCard()
        onView(withId(android.R.id.list)).check(matches(withListSize(2)))
        onData(anything()).atPosition(1).onChildView(withId(R.id.cardName)).check(
            matches(withText("espressoCard"))
        ).check(matches(withTextColor(0xFF00FA00.toInt())))
        onData(anything()).atPosition(1).onChildView(withId(R.id.cardDescription)).check(
            matches(withText("espressoCardDescription"))
        ).check(matches(withTextColor(0xFF00FA00.toInt())))

        Espresso.pressBack()
        onView(withId(R.id.problemPoint)).check(matches(withText("5.00")))
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

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.advantage_disadvantageNewLabel)))

        onView(withId(R.id.seekBarVariants)).perform(setProgress(0))
        onView(withId(R.id.scoreSeekBar)).check(matches(withText("-5")))

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.disadvantageNewLabel)))

        onView(withId(R.id.seekBarVariants)).perform(setProgress(10))
        onView(withId(R.id.scoreSeekBar)).check(matches(withText("5")))

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(R.string.advantageNewLabel)))

        onView(withId(R.id.inputAdd)).check(matches(not(isDisplayed())))

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
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
            )
        ).check(matches(withText(toolbarTitle)))
    }

}