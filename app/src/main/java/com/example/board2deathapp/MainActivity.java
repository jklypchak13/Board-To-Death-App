package com.example.board2deathapp;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        Log.d("CREATE", "Successfully created the activity");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("START", "Successfully started the activity");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("RESUME", "Successfully Resumed the activity");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("PAUSE", "Successfully paused the activity");
    }


    @Override
    protected void onStop(){
        super.onStop();
        Log.d("STOP", "Successfully stopped the activity.");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("RESTART", "Successfully restarted the activity.");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("DESTROY", "Successfully destroyed the activity.");
    }
}
