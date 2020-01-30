package com.example.newmusclelog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newmusclelog.custom.CustomItemTouchHelperCallback;
import com.example.newmusclelog.custom.ExerciseRecyclerViewAdapter;
import com.example.newmusclelog.data.Exercise;
import com.example.newmusclelog.data.Workout;
import com.example.newmusclelog.data.WorkoutHistory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ActiveWorkoutFragment extends Fragment {


    MainActivity ma = new MainActivity();


    private Workout workout;
    private TextView emptyRecyclerView;
    private RecyclerView exerciseRecyclerView;
    private ExerciseRecyclerViewAdapter adapter;
    private WorkoutHistory workoutHistory;
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseWorkouts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v =  inflater.inflate(R.layout.fragment_active_workout, container, false);
        fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(this::addNewExercise);
        setHasOptionsMenu(true);

        workout = new Workout("WORKOUT");

        if(savedInstanceState != null) {
            workout = savedInstanceState.getParcelable("key");
        }

        emptyRecyclerView = v.findViewById(R.id.empty_recycler_view);
        //buildRecyclerView(workout);
        exerciseRecyclerView = v.findViewById(R.id.recycler_view_exercise);

        // improves performance if size of RecyclerView content is fixed
        // taken from developer.android.com
        exerciseRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        exerciseRecyclerView.setLayoutManager(layoutManager);

        // add divider
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        exerciseRecyclerView.addItemDecoration(itemDecoration);

        // Create adapter and set its data set to workout
        adapter = new ExerciseRecyclerViewAdapter(workout, this);

        // Set up swipe to dismiss and ability to move RecyclerView items around

        // Create callback object for ItemTouchHelper
        ItemTouchHelper.Callback callback = new CustomItemTouchHelperCallback(adapter);

        // Implement object created above
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

        touchHelper.attachToRecyclerView(exerciseRecyclerView);

        if (adapter.getItemCount() == 0)
        {
            showEmptyRecyclerViewText();
        }
        else
        {
            exerciseRecyclerView.setAdapter(adapter);
        }

        ExercisesFragment ef = new ExercisesFragment();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    System.out.println("yapa");
                    getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, ef).commit();
                }
                return false;
            }
        });

        return v;
    }

    // Adds save button (check mark) to action bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.active_workout_action_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {

                List<Exercise> exercises = workout.getExercises();

                if(exercises.isEmpty()) {
                    Toast.makeText(getContext(), "You have to add at least 1 exercise", Toast.LENGTH_SHORT).show();
                }


                else {

                    final EditText workoutName = new EditText(this.getContext());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                    builder.setTitle("Give a name for your workout:");
                    builder.setView(workoutName);
                    workoutName.setPadding(30,50,30,20);
                    workoutName.setTextColor(Color.parseColor("#FFFFFF"));

                    builder.setPositiveButton("OK", (dialog, which) -> {
                        mAuth = FirebaseAuth.getInstance();
                        String user_id = mAuth.getCurrentUser().getUid();
                        String workoutN = workoutName.getText().toString();

                        databaseWorkouts = FirebaseDatabase.getInstance().getReference("Workouts").child(user_id);
                        String id = databaseWorkouts.push().getKey();

                        workout = new Workout(workoutN, user_id, exercises);
                        databaseWorkouts.child(id).setValue(workout);

                        Navigation.findNavController(this.getView()).navigate(R.id.historyFragment);
                        Toast.makeText(getContext(), "Workout saved", Toast.LENGTH_SHORT).show();
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    });

                    AlertDialog dialog = builder.create();

                    dialog.show();


                }

                return true;
            }
            case R.id.action_delete: {
                exerciseRecyclerView.requestFocus();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                builder.setTitle("Confirm deletion").setMessage("Are you sure you want to delete" +
                        " this workout?");

                builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Navigation.findNavController(this.getView()).navigate(R.id.newworkoutFragment);
                    Toast.makeText(getContext(), "Workout deleted", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
                    dialog.dismiss();
                });

                AlertDialog dialog = builder.create();

                dialog.show();

                /*Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.parseColor("#D81B60"));
                Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.parseColor("#D81B60"));*/
                //builder.show();

                return true;
            }
            default:
                // unrecognized button pressed
                return super.onOptionsItemSelected(item);
        }
    }

    public void showEmptyRecyclerViewText()
    {
        emptyRecyclerView.setVisibility(View.VISIBLE);
    }

    public void addNewExercise(View view) {

        Bundle bundle = getArguments();
        String value = "";
        if(bundle != null) {
            value = bundle.getString("ExerciseTitle");
        }
        System.out.println("lala");
        System.out.println(value);

        Exercise newExercise = new Exercise(value, -1, -1);
        if (exerciseRecyclerView.getAdapter() == null) {
            exerciseRecyclerView.setAdapter(adapter);
        }


        emptyRecyclerView.setVisibility(View.INVISIBLE);

        workout.addExercise(newExercise);

        adapter.notifyItemInserted(workout.getExercises().size() - 1);
    }

    private void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService
                (Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("key", workout);
    }

}
