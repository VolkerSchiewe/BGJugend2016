package de.schiewe.volker.bgjugend2016

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.schiewe.volker.bgjugend2016.activities.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InfoFragmentEspressoTest {
    @Rule
    @JvmField
    val activity = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun openInfoFragment(){
        onView(withId(R.id.navigation_info)).perform(click())
        onView(withId(R.id.fragment_info)).check(matches(isDisplayed()))
    }

    @Test
    fun testYouthWorkers(){
        onView(withId(R.id.list_youth_worker)).check(matches(isDisplayed()))
//        onData(withId(R.id.youth_worker_name))
//                .inAdapterView(withId(R.id.list_youth_worker))
//                .atPosition(0)
//                .check(matches(isDisplayed()))
//        onView(withId(R.id.youth_worker_name)).check(matches(isDisplayed())) // TODO Match multiple nodes
//        onView(withId(R.id.youth_team)).check(matches(withText("Bla")))  // TODO check text is displayed

    }
}