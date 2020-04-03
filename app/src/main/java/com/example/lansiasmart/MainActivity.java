package com.example.lansiasmart;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements UserDataInterface {

    private UserData userData;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_screening:
                    showTopLevelFragment(new ScreeningFragment(), "SCREENING");
                    return true;
                case R.id.navigation_edukasi:
                    showTopLevelFragment(new EdukasiFragment(), "EDUKASI");
                    return true;
                case R.id.navigation_konseling:
                    showTopLevelFragment(new KonselingFragment(), "KONSELING");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve a reference to the BottomNavigationView and listen for click events.
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Show the first TopLevelFragment by default.
        showTopLevelFragment(new ScreeningFragment(), "SCREENING");

        // Get the ViewModel.
        userData = new ViewModelProvider(this).get(UserData.class);

        // Create the observer which updates the UI.
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                ScreeningFragment screeningFragment = (ScreeningFragment)getSupportFragmentManager().findFragmentByTag("SCREENING");
                if (screeningFragment != null && screeningFragment.isVisible()) {
                    screeningFragment.onUserDataChanged(userData);
                }
                Log.i("Observer triggered", "test");
            }
        };

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.white, null)));

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        userData.getName().observe(this, nameObserver);
    }

    @Override
    public void showTopLevelFragment(Fragment fragment, String TAG) {
        // Use the fragment manager to dynamically change the fragment displayed in the FrameLayout.
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_host, fragment, TAG)
                .commit();
    }

    @Override
    public void setUserData(String name, int age, String gender) {
        userData.setUserData(name, age, gender);
        System.out.println(this.userData.getAge());
    }

    @Override
    public UserData getUserData() {
        return this.userData;
    }

    @Override
    public void removeUserData() {
        this.userData.removeUserData();
    }
}
