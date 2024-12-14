package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Fragment homeNavHost, recordNavHost, trainingNavHost, settingNavHost;
    private FragmentManager fragmentManager;
    private Fragment activeFragment;
    private Toolbar toolbar;
    public void navigateToHomeAndOpenNewRun() {
        if (activeFragment != homeNavHost) {
            fragmentManager.beginTransaction()
                    .hide(activeFragment)
                    .show(homeNavHost)
                    .commit();
            activeFragment = homeNavHost;

            // 切换到 Home 的 NavController 并导航到 NewRunFragment
            NavController navController = ((NavHostFragment) homeNavHost).getNavController();
            navController.navigate(R.id.action_homeFragment_to_newRunFragment);
        } else {
            // 如果已经在 Home，直接导航到 NewRunFragment
            NavController navController = ((NavHostFragment) homeNavHost).getNavController();
            navController.navigate(R.id.action_homeFragment_to_newRunFragment);
        }

        // 更新 BottomNavigationView 的选中状态
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Toolbar
        toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        homeNavHost = NavHostFragment.create(R.navigation.home_nav_graph);
        recordNavHost = NavHostFragment.create(R.navigation.record_nav_graph);
        trainingNavHost = NavHostFragment.create(R.navigation.training_nav_graph);
        settingNavHost = NavHostFragment.create(R.navigation.setting_nav_graph);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, homeNavHost, "HOME").hide(homeNavHost);
        transaction.add(R.id.container, recordNavHost, "RECORD").hide(recordNavHost);
        transaction.add(R.id.container, trainingNavHost, "TRAINING").hide(trainingNavHost);
        transaction.add(R.id.container, settingNavHost, "SETTING").hide(settingNavHost);
        transaction.show(homeNavHost); // 显示默认Tab的内容
        activeFragment = homeNavHost; // 设置默认活动Fragment
        transaction.commit();

        // 设置 BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedNavHost = null;
            NavController navController = null;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                selectedNavHost = homeNavHost;
                navController = ((NavHostFragment) homeNavHost).getNavController();
            } else if (itemId == R.id.navigation_record) {
                selectedNavHost = recordNavHost;
                navController = ((NavHostFragment) recordNavHost).getNavController();
            } else if (itemId == R.id.navigation_training) {
                selectedNavHost = trainingNavHost;
                navController = ((NavHostFragment) trainingNavHost).getNavController();
            } else {
                selectedNavHost = settingNavHost;
                navController = ((NavHostFragment) settingNavHost).getNavController();
            }
            if (selectedNavHost != null && selectedNavHost != activeFragment) {
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(selectedNavHost)
                        .commit();
                activeFragment = selectedNavHost;

                // Link the Toolbar with the NavController for the selected tab
                if (navController != null) {
                    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
                    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

                    // Add a listener to hide Bottom Navigation on non-top-level destinations
                    navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

                        if (appBarConfiguration.getTopLevelDestinations().contains(destination.getId())) {
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        } else {
                            bottomNavigationView.setVisibility(View.GONE);
                        }
                    });
                }
            }



            return true;
        });



    }

}