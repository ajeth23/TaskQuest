package com.ecuacion.finalproject.ui.auth;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecuacion.finalproject.ui.home.MainScreen;
import com.ecuacion.finalproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity {

    private EditText edtEmail, edtPass;
    private MaterialButton btnLogin, btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);

        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(v -> login());

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), ActivitySignup.class)));

    }

    private void login() {
        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Log.d(TAG, "signInWithCustomToken:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "You are now Log in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainScreen.class));
                        finish();

                        // Add code to navigate to the main activity or perform other actions upon successful login
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            // Handle invalid user exception (user not registered)
                            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            // Handle invalid password exception
                            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // Handle other exceptions
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void expandItem(View view) {
        LinearLayout expand = view.findViewById(R.id.linear2);
        LinearLayout linear2 = view.findViewById(R.id.linear2);

        if (expand.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(linear2, new AutoTransition());
            expand.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(linear2, new AutoTransition());
            expand.setVisibility(View.GONE);
        }
    }


}