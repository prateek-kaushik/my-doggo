package com.prateek.dogengine;

import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 26)
public class MainActivityTest {

    private MainActivity mMainActivity;

    @Before
    public void setup() {
        mMainActivity = Robolectric.buildActivity(MainActivity.class).create().resume().visible()
                .get();
    }

    @Test
    public void validateToolbarTitle() {
        ActionBar actionBar = mMainActivity.getSupportActionBar();
        Assert.assertEquals(actionBar.getTitle(),
                mMainActivity.getResources().getString(R.string.app_name));
    }

    @Test
    public void validateBreedListFragmentIsPresent() {
        FragmentManager fragmentManager = mMainActivity.getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager
                .findFragmentById(R.id.nav_host_fragment);
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        Assert.assertEquals(fragment.getClass().getSimpleName(),
                BreedsListFragment.class.getSimpleName());
    }

    @Test
    public void validateWelcomeMessage() {
        FragmentManager fragmentManager = mMainActivity.getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager
                .findFragmentById(R.id.nav_host_fragment);
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        TextView label = fragment.getView().findViewById(R.id.default_error);
        Assert.assertEquals(label.getText(),
                mMainActivity.getResources().getString(R.string.welcome_message));
    }

    @Test
    public void verifySearchViewInput() {
        Toolbar toolbar = mMainActivity.findViewById(R.id.toolbar);
        MenuItem searchMenu = toolbar.getMenu().findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setQuery("test", false);
        Assert.assertEquals(searchView.getQuery().toString(), "test");
    }
}
