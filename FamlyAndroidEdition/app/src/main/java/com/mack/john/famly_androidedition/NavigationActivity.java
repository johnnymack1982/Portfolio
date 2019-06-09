package com.mack.john.famly_androidedition;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mack.john.famly_androidedition.fragments.CheckinFragment;
import com.mack.john.famly_androidedition.fragments.newsfeed.NewsFeedFragment;
import com.mack.john.famly_androidedition.fragments.PermissionFragment;
import com.mack.john.famly_androidedition.fragments.ProfileFragment;
import com.mack.john.famly_androidedition.fragments.family_profile.FamilyProfileFragment;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    // Class properties
    public static final String ACTION_LEAVE_PROFILE = "action_leave_profile";



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

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
            case R.id.navigation_newsfeed:
                getIntent().setAction(ACTION_LEAVE_PROFILE);

                fragment = new NewsFeedFragment();
                loadFragment(fragment);
                return true;

            case R.id.navigation_permission:
                getIntent().setAction(ACTION_LEAVE_PROFILE);

                fragment = new PermissionFragment();
                loadFragment(fragment);
                return true;

            case R.id.navigation_checkin:
                getIntent().setAction(ACTION_LEAVE_PROFILE);

                fragment = new CheckinFragment();
                loadFragment(fragment);
                return true;

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
