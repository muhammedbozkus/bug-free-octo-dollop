package com.example.newmusclelog;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class NewWorkoutFragment extends Fragment {

    Button newWorkout;

    public NewWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_newworkout, container, false);

        setHasOptionsMenu(true);
        newWorkout = (Button) v.findViewById(R.id.button);

        newWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.activeWorkoutFragment);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.homemenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:



                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                builder.setTitle("Log out").setMessage("Are you sure you want to logout?");

                builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Navigation.findNavController(this.getView()).navigate(R.id.homeFragment);
                });
                builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
                    dialog.dismiss();
                });

                AlertDialog dialog = builder.create();

                dialog.show();
                break;
        }
        return true;
    }
}
