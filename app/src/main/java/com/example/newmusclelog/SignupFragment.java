package com.example.newmusclelog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {

    EditText emailId, password;
    Button btnSignUp;
    FirebaseAuth mFirebaseAuth;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = (EditText) v.findViewById(R.id.signUpEmailEt);
        password = (EditText) v.findViewById(R.id.signUpPasswordEt);
        btnSignUp =(Button) v.findViewById(R.id.signUpBtn2);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(getActivity(), "Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(getActivity(),"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Navigation.findNavController(v).navigate(R.id.newworkoutFragment);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(), "Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

}
