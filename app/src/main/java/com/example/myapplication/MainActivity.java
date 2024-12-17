package com.example.myapplication;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.dataBase.DataBaseHelper;
import com.example.myapplication.dataBase.RunningRecord;
import com.example.myapplication.ui.setting.Theme;
import com.example.myapplication.ui.setting.ThemeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
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
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ThemeFragment.ThemeChangeListener {

    private int backdays = 0;

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

        // Apply the saved theme before the layout is loaded
        String theme = getSharedPreferences("theme_prefs", MODE_PRIVATE).getString("theme", "light");
        Log.d("TAG", "onCreate: Theme: " + theme);
        if ("dark".equals(theme)) {
            setTheme(R.style.AppTheme_Dark);
            Log.d("TAG", "onCreate: Dark theme applied");
        } else {
            setTheme(R.style.AppTheme_Light);
            Log.d("TAG", "onCreate: Light theme applied");
        }
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

    @Override
    public void onThemeChange(Theme theme) {
        if (theme == Theme.LIGHT) {
            getSharedPreferences("theme_prefs", MODE_PRIVATE)
                    .edit()
                    .putString("theme", "light")
                    .commit();
            Log.d("TAG", "onThemeChange: Light theme applied");
//            setTheme(R.style.AppTheme_Light);
        } else {
            getSharedPreferences("theme_prefs", MODE_PRIVATE)
                    .edit()
                    .putString("theme", "dark")
                    .commit();
            Log.d("TAG", "onThemeChange: Dark theme applied");
//            setTheme(R.style.AppTheme_Dark);
        }
        if (activeFragment != homeNavHost) {
            fragmentManager.beginTransaction().remove(activeFragment).commit();
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
//        recreate();
        restartApp();


        // Toggle theme preference
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        boolean isDarkMode = prefs.getBoolean(KEY_THEME, false);
//
//        prefs.edit().putBoolean(KEY_THEME, !isDarkMode).apply();
//
//        // Restart the activity to apply the new theme
//        recreate();
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish the current activity
        System.exit(0); // Kill the process to ensure a clean restart
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_button) {
            // Handle button click
            Toast.makeText(this, "Add a record", Toast.LENGTH_SHORT).show();
            addRecord();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addRecord() {

        String date = getLastWeekStartDate();
        backdays += 1;
        Random random = new Random();
        int randomDistance = random.nextInt(16);


        // Handle button click
        Map<String, String> record = new HashMap<>();
        record.put("id", "0");
        record.put("timestamp", date);
        record.put("day", "16");
        record.put("month", "12");
        record.put("year", "2024");
        record.put("place", "Park");
        record.put("trainingDuration", "01:00:00");
        record.put("calories", "100");
        record.put("distance", Integer.toString(randomDistance));
        record.put("averageSpeed", "5");
        record.put("detailKms", "[6,6.5,6,6.5,5]");

        record.put("mapInfo", "[{\"lat\":\"46.0054\",\"lng\":\"8.9556\"},{\"lat\":\"46.0048\",\"lng\":\"8.9556\"},{\"lat\":\"46.0039\",\"lng\":\"8.9556\"},{\"lat\":\"46.0036\",\"lng\":\"8.9537\"},{\"lat\":\"46.0035\",\"lng\":\"8.9526\"},{\"lat\":\"46.0028\",\"lng\":\"8.9507\"},{\"lat\":\"46.0016\",\"lng\":\"8.9500\"},{\"lat\":\"46.0003\",\"lng\":\"8.9496\"},{\"lat\":\"45.9994\",\"lng\":\"8.9495\"},{\"lat\":\"45.9986\",\"lng\":\"8.9486\"},{\"lat\":\"45.9980\",\"lng\":\"8.9479\"},{\"lat\":\"45.9973\",\"lng\":\"8.9476\"},{\"lat\":\"45.9965\",\"lng\":\"8.9472\"},{\"lat\":\"45.9953\",\"lng\":\"8.9467\"},{\"lat\":\"45.9944\",\"lng\":\"8.9464\"},{\"lat\":\"45.9932\",\"lng\":\"8.9468\"},{\"lat\":\"45.9924\",\"lng\":\"8.9474\"},{\"lat\":\"45.9920\",\"lng\":\"8.9483\"}]");

        RunningRecord record1 = RunningRecord.convertFormMap(record);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(this);
        dataBaseHelper.addRecord(this, record1);

    }

    private String getLastWeekStartDate() {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -backdays);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;
    }




}