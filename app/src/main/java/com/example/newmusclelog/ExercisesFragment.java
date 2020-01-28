package com.example.newmusclelog;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.newmusclelog.custom.DataHelper;
import com.example.newmusclelog.data.Exercise;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ExercisesFragment extends Fragment {

    private ListView lv;
    private FirebaseListAdapter adapter;
    private ArrayList<Exercise> exerciseList;
    private ArrayList<String> nameList;
    //private Adapter adapter;

    public ExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_exercises, container, false);

        lv = v.findViewById(R.id.lv);
        Query query = FirebaseDatabase.getInstance().getReference().child("exerciseList");
        FirebaseListOptions<ExerciseElement> options = new FirebaseListOptions.Builder<ExerciseElement>()
                .setLayout(R.layout.exercise)
                .setQuery(query, ExerciseElement.class)
                .build();

        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView bodypart = v.findViewById(R.id.bodypart);
                TextView title = v.findViewById(R.id.title);

                ExerciseElement el = (ExerciseElement) model;
                bodypart.setText(el.getBodypart().toString());
                title.setText(el.getTitle().toString());


            }
        };
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        /*try {
            exerciseList = DataHelper.loadExercise(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nameList = new ArrayList<>();
        for(int i = 0; i < exerciseList.size(); i++) {
            String str = exerciseList.get(i).getName();
            nameList.add(str);
        }

        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, nameList);
        lv.setAdapter((ListAdapter) adapter);*/

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
