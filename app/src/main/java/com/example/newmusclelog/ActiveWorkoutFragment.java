package com.example.newmusclelog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
        /*Bundle bundle = savedInstanceState;
        if (bundle != null) {
            workout = bundle.getParcelable("WORKOUT");
        }*/

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

                // set focus to trigger the focus listeners in the CustomEditTexts
                /*exerciseRecyclerView.requestFocus();

                hideKeyboard(getContext(), emptyRecyclerView);

                try {
                    workoutHistory.updateWorkout(workout);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                //Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment).navigate(R.id.exercisesFragment);

                System.out.println(workout.getExercises().size());
                List<Exercise> exercises = workout.getExercises();
                String firstexercise = exercises.get(0).getName();
                System.out.println(firstexercise);

                mAuth = FirebaseAuth.getInstance();
                String user_id = mAuth.getCurrentUser().getUid();


                databaseWorkouts = FirebaseDatabase.getInstance().getReference("Workouts").child(user_id);
                String id = databaseWorkouts.push().getKey();

                workout = new Workout("abc", user_id, exercises);
                databaseWorkouts.child(id).setValue(workout);


                Toast.makeText(getContext(), "Workout saved", Toast.LENGTH_SHORT).show();

                return true;
            }
            case R.id.action_delete: {
                exerciseRecyclerView.requestFocus();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm deletion").setMessage("Are you sure you want to delete" +
                        " this workout?");

                builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    try {
                        workoutHistory.removeWorkout(workout);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getContext(), "Workout deleted", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
                    dialog.dismiss();
                });

                builder.show();
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
        Exercise newExercise = new Exercise("", -1, -1);
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
