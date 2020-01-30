package com.example.newmusclelog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SignupFragment extends Fragment {

    EditText emailId, password, confirmPassword;
    Button btnSignUp;
    FirebaseAuth mFirebaseAuth;
    TextInputLayout emailLayout, passwordLayout, confirmLayout;

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
        confirmPassword = (EditText) v.findViewById(R.id.signUpConfirmPasswordEt) ;
        btnSignUp =(Button) v.findViewById(R.id.signUpBtn2);
        emailLayout = (TextInputLayout) v.findViewById(R.id.etSignUpEmailLayout);
        passwordLayout = (TextInputLayout) v.findViewById(R.id.etSignUpPasswordLayout);
        confirmLayout = (TextInputLayout) v.findViewById(R.id.etSignUpConfirmLayout);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String confirmPwd = confirmPassword.getText().toString();

                if(!email.isEmpty()){
                    emailLayout.setError(null);
                }
                if(!pwd.isEmpty()){
                    passwordLayout.setError(null);
                }

                if(!confirmPwd.isEmpty()){
                    confirmLayout.setError(null);
                }

                if(email.isEmpty()){
                    emailLayout.setError("Please enter your E-Mail");
                }

                else  if(pwd.isEmpty()){
                    passwordLayout.setError("Please enter your password");
                    password.requestFocus();
                }

                else  if(confirmPwd.isEmpty()){
                    confirmLayout.setError("Please confirm your password");
                    password.requestFocus();
                }

                else if(!pwd.equals(confirmPwd)) {
                    confirmLayout.setError("Password does not match");
                }
                else  if(!(email.isEmpty() && pwd.isEmpty()) && pwd.equals(confirmPwd)){
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
