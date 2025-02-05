package com.orhava.trivia2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import io.reactivex.annotations.NonNull;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText edit_email,edit_password;
    private MaterialButton button_register;
    private Button button_login;
    private View rootLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initialize();
        onStart();
        startButtons();




    }

    void startButtons(){
        button_login.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        });
    }

    void initialize(){
        edit_email= findViewById(R.id.edit_email);
        edit_password= findViewById(R.id.edit_password);
        button_register=findViewById(R.id.button_register);
        button_login=findViewById(R.id.button_login);
        mAuth = FirebaseAuth.getInstance();
        rootLayout = findViewById(R.id.RlMainMenu);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Snackbar snackbar = Snackbar.make(rootLayout, getString(R.string.User_is_already_connected), Snackbar.LENGTH_SHORT);
            snackbar.setAction(R.string.ok, v -> snackbar.dismiss()); // Optional: Add an action for the user to dismiss the message
            snackbar.show();
        } else {
            reload();
        }
    }


    public void reload() {
        button_register.setOnClickListener(view -> {
            if(edit_email==null || Objects.requireNonNull(edit_email.getText()).toString().isEmpty() ){

                Snackbar snackbar = Snackbar.make(rootLayout, getString(R.string.Enter_Email), Snackbar.LENGTH_SHORT);
                snackbar.setAction(R.string.ok, v -> snackbar.dismiss()); // Optional: Add an action for the user to dismiss the message
                snackbar.show();
            }
            if(edit_password==null || Objects.requireNonNull(edit_password.getText()).toString().isEmpty() ) {


                Snackbar snackbar = Snackbar.make(rootLayout, getString(R.string.Enter_Password), Snackbar.LENGTH_SHORT);
                snackbar.setAction(R.string.ok, v -> snackbar.dismiss()); // Optional: Add an action for the user to dismiss the message
                snackbar.show();
            }

            if(edit_password!=null && edit_email!=null && !(Objects.requireNonNull(edit_password.getText()).toString().isEmpty()) && !(Objects.requireNonNull(edit_email.getText()).toString().isEmpty())){

                mAuth.createUserWithEmailAndPassword(Objects.requireNonNull(edit_email.getText()).toString().trim(), edit_password.getText().toString().trim())
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in    user's information
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.


                                    Snackbar snackbar = Snackbar.make(rootLayout, getString(R.string.Authentication_failed), Snackbar.LENGTH_SHORT);
                                    snackbar.setAction(R.string.ok, v -> snackbar.dismiss()); // Optional: Add an action for the user to dismiss the message
                                    snackbar.show();
                                    updateUI(null);
                                }
                            }

                            private void updateUI(FirebaseUser user) {
                                if(user!=null) {
                                    startActivity(new Intent(SignUp.this, SignIn.class));
                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                }
                            }
                        });

            }

        });
    }


}