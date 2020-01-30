package com.example.newmusclelog;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ExampleFragment extends Fragment {

    Button btn;

    public ExampleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_example, container, false);
        btn = (Button) v.findViewById(R.id.button4);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment ldf = new HomeFragment();
                Bundle args = new Bundle();
                args.putString("key", "123");
                ldf.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.nav_host_fragment, ldf).commit();
                Navigation.findNavController(v).navigate(R.id.homeFragment);
            }
        });

        return v;
    }

}
