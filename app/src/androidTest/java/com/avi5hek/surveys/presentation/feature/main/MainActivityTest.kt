package com.avi5hek.surveys.presentation.feature.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.avi5hek.surveys.R
import com.avi5hek.surveys.presentation.utils.LoadingDialogInstruction
import com.azimolabs.conditionwatcher.ConditionWatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by "Avishek" on 8/7/2020.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

  @get:Rule
  val activityRule = ActivityTestRule(MainActivity::class.java)

  @Test
  fun appLunchesSuccessfully() {
    ActivityScenario.launch(MainActivity::class.java)
  }

  @Test
  fun whenAppIsLaunched_CheckLoadingDialogDisplayed() {
    onView(withId(R.id.progress_bar))
      .check(matches(isDisplayed()))
  }

  @Test
  fun whenLoadingIsFinished_CheckSurveyListFragmentIsDisplayed() {
    ConditionWatcher.setTimeoutLimit(30000)
    ConditionWatcher.waitForCondition(LoadingDialogInstruction(activityRule.activity))

    onView(withId(R.id.pager))
      .check(matches(isDisplayed()))
    onView(withId(R.id.button_take_survey))
      .check(matches(isDisplayed()))
    onView(withId(R.id.text_toolbar_title))
      .check(matches(isDisplayed()))
    onView(withId(R.id.image_refresh))
      .check(matches(isDisplayed()))
  }

  @Test
  fun whenTakeSurveyButtonIsClicked_NavigateToSurveyDetails() {
    ConditionWatcher.setTimeoutLimit(30000)
    ConditionWatcher.waitForCondition(LoadingDialogInstruction(activityRule.activity))

    onView(withId(R.id.button_take_survey))
      .perform(click())

    onView(withId(R.id.text_message))
      .check(matches(isDisplayed()))
  }

}
