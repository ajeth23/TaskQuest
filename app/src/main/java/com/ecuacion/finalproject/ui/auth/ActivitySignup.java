package com.ecuacion.finalproject.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecuacion.finalproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class ActivitySignup extends AppCompatActivity {

    private EditText edtUsername, crtEmail, crtPassword, crtRePassword;
    private MaterialButton btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);

        mAuth = FirebaseAuth.getInstance();

        edtUsername = findViewById(R.id.username);
        crtEmail = findViewById(R.id.email);
        crtPassword = findViewById(R.id.password);
        crtRePassword = findViewById(R.id.repassword);

        btnRegister = findViewById(R.id.signupbtn);

        btnRegister.setOnClickListener(view -> {
            register();
        });
    }


    private void register() {
        String email = crtEmail.getText().toString();
        String password = crtPassword.getText().toString();
        String rePassword = crtRePassword.getText().toString();

        if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(ActivitySignup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(rePassword)) {
            Toast.makeText(ActivitySignup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Redirect to the login page upon successful registration
                        Intent intent = new Intent(ActivitySignup.this, ActivityLogin.class);
                        Toast.makeText(ActivitySignup.this, "Account Successfully Created", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish(); // Optional: Finish the RegisterActivity so the user can't navigate back to it
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            // Handle user already exists exception
                            Toast.makeText(ActivitySignup.this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            // Handle invalid email format exception
                            Toast.makeText(ActivitySignup.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // Handle other exceptions
                            Toast.makeText(ActivitySignup.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}