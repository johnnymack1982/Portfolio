package com.mack.john.famly_androidedition;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mack.john.famly_androidedition.fragments.CheckinFragment;
import com.mack.john.famly_androidedition.fragments.ParentPermissionFragment;
import com.mack.john.famly_androidedition.fragments.newsfeed.NewsFeedFragment;
import com.mack.john.famly_androidedition.fragments.ChildPermissionFragment;
import com.mack.john.famly_androidedition.fragments.ProfileFragment;
import com.mack.john.famly_androidedition.fragments.family_profile.FamilyProfileFragment;
import com.mack.john.famly_androidedition.objects.account.profile.Child;
import com.mack.john.famly_androidedition.objects.account.profile.Parent;
import com.mack.john.famly_androidedition.utils.AccountUtils;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    // Class properties
    public static final String ACTION_LEAVE_PROFILE = "action_leave_profile";



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Set bottom navigation listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // If returning from editing family profile, set the navigation bar to the correct selection
        try {
            if(getIntent().getAction().equals(FamilyProfileFragment.ACTION_EDIT_PROFILE)) {
                loadFragment(new ProfileFragment());

                bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
            }
        }

        catch (Exception e) {
            e.printStackTrace();

            loadFragment(new NewsFeedFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;

        switch (menuItem.getItemId()) {

            // Populate newsfeed fragment
            case R.id.navigation_newsfeed:
                getIntent().setAction(ACTION_LEAVE_PROFILE);

                fragment = new NewsFeedFragment();
                loadFragment(fragment);
                return true;

            // Populate permission fragment
            case R.id.navigation_permission:
                getIntent().setAction(ACTION_LEAVE_PROFILE);

                if(AccountUtils.loadProfile(this) instanceof Child) {
                    fragment = new ChildPermissionFragment();
                    loadFragment(fragment);
                }

                else if(AccountUtils.loadProfile(this) instanceof Parent) {
                    fragment = new ParentPermissionFragment();
                    loadFragment(fragment);
                }

                return true;

            // Populate checkin fragment
            case R.id.navigation_checkin:
                getIntent().setAction(ACTION_LEAVE_PROFILE);

                fragment = new CheckinFragment();
                loadFragment(fragment);
                return true;

            // Populate profile fragment
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                loadFragment(fragment);
                return true;
        }

        return false;
    }



    // System generated methods
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_navigation, fragment)
                .addToBackStack(null)
                .commit();
    }
}
