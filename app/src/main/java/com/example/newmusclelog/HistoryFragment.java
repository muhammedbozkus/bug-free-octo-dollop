package com.example.newmusclelog;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.newmusclelog.data.Exercise;
import com.example.newmusclelog.data.Workout;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryFragment extends Fragment {

    private ListView lv;
    private FirebaseListAdapter adapter;
    private FirebaseAuth mAuth;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);


        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        lv = v.findViewById(R.id.lvHistory);
        Query query = FirebaseDatabase.getInstance().getReference().child("Workouts").child(user_id);
        FirebaseListOptions<Workout> options = new FirebaseListOptions.Builder<Workout>()
                .setLayout(R.layout.workout)
                .setQuery(query, Workout.class)
                .build();

        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView workoutname = v.findViewById(R.id.workoutName);
                TextView exerciseInfo = v.findViewById(R.id.exerciseInfo);

                Workout workout = (Workout) model;
                workoutname.setText(workout.getWorkoutName().toString());

                String list = Arrays.toString(workout.getExercises().toArray()).replace("[", " ").replace("]", "").replace(",", "");
                exerciseInfo.setText(list);



            }
        };

        lv.setAdapter(adapter);
        return v;
    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
