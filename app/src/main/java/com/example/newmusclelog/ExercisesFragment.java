package com.example.newmusclelog;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.newmusclelog.custom.DataHelper;
import com.example.newmusclelog.data.Exercise;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ExercisesFragment extends Fragment {

    private ListView lv;
    private ArrayList<Exercise> exerciseList;
    private ArrayList<String> nameList;
    private Adapter adapter;

    public ExercisesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_exercises, container, false);

        lv = v.findViewById(R.id.lv);
        try {
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
        lv.setAdapter((ListAdapter) adapter);

        return v;
    }

}
