package com.example.newmusclelog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newmusclelog.custom.NewWorkoutDialog;
import com.example.newmusclelog.data.Workout;
import com.example.newmusclelog.data.WorkoutHistory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private WorkoutHistory workoutHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            workoutHistory = new WorkoutHistory(getApplicationContext().getFilesDir());
        } catch (IOException e) {
            e.printStackTrace();
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        final BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.loginFragment || destination.getId() == R.id.homeFragment ||
                destination.getId() == R.id.signupFragment || destination.getId() == R.id.activeWorkoutFragment) {
                    bottomNav.setVisibility(View.GONE);
                }
                else {
                    bottomNav.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void createNewWorkoutDialog(View view) throws IOException {
        NewWorkoutDialogFragment dialog = new NewWorkoutDialogFragment(this);
        dialog.show();
    }

    public void launchNewWorkoutWizard(String workoutName) throws IOException {
        ActiveWorkoutFragment activeWorkoutFragment = new ActiveWorkoutFragment();
        Workout newWorkout = new Workout(workoutName);
        workoutHistory.addWorkout(newWorkout);
        Bundle bundle = new Bundle();
        bundle.putParcelable("WORKOUT", newWorkout);
        activeWorkoutFragment.setArguments(bundle);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.activeWorkoutFragment);
    }
}
