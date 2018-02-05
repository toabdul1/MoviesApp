package com.sky.movieapp;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.AutoCompleteTextView;

import com.sky.movieapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestMovies {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new
            ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void checkSearchView() {
        onView(withId(R.id.mSearch)).perform(click());
        onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText("hi"));
        //onView(withId(R.id.mSearch).perform(typeText("hi"));
    }

}