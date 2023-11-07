package com.ecuacion.finalproject.ui.home;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecuacion.finalproject.R;
import com.ecuacion.finalproject.data.add_task;
import com.ecuacion.finalproject.databinding.ActivityMainScreenBinding;
import com.ecuacion.finalproject.model;
import com.ecuacion.finalproject.taskAdapter.myadapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MainScreen extends AppCompatActivity {

    RecyclerView rcview;
    myadapter adpater;
    CardView btnAddTask;
    SearchView searchView;
    private ActivityMainScreenBinding binding;

    //for calendar


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            boolean isDarkBackground = true;
            if (isDarkBackground) { // Check if the background is dark
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // Clear the flag for light status bar icons
                decor.setSystemUiVisibility(0);
            }
        }

        rcview = findViewById(R.id.rcview);
        rcview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Task"), model.class)
                        .build();

        adpater = new myadapter(options, getApplicationContext(), binding);
        rcview.setAdapter(adpater);

        btnAddTask = findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), add_task.class));
            Log.d("MainActivity", "onCreate: Started"); // Add similar log statements throughout your code

        });


        searchView = findViewById(R.id.search);
        searchView.setQueryHint("Search Title Task");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query when the user submits it (e.g., by pressing Enter)
                processSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the search query as the user types
                processSearch(newText);
                return true;
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        adpater.startListening();
        Log.d(TAG, "onCreate: Activity is created.");
    }


    @Override
    protected void onStop() {
        super.onStop();
        adpater.stopListening();
        Log.d(TAG, "onStart: Activity is starting.");
    }


    private void processSearch(String query) {
        // Convert the query to lowercase for case-insensitive search
        String lowercaseQuery = query.toLowerCase();

        // If the query is empty, display all students
        if (lowercaseQuery.isEmpty()) {
            FirebaseRecyclerOptions<model> options =
                    new FirebaseRecyclerOptions.Builder<model>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Task"), model.class)
                            .build();
            adpater = new myadapter(options, getApplicationContext(), binding);
            adpater.startListening();
            rcview.setAdapter(adpater);
        } else {
            updateRecyclerView(lowercaseQuery);
        }
    }

    private void updateRecyclerView(String query) {
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Task")
                                .orderByChild("title")
                                .startAt(query)
                                .endAt(query + "\uf8ff"), model.class)
                        .build();

        Log.d("QueryDebug", "Query: " + query);

        // Update the existing adapter's options
        adpater.updateOptions(options);

        // Notify the adapter that the data set has changed
        adpater.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        // Display a confirmation dialog before allowing the user to exit
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // If the user clicks "Yes," exit the app
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // If the user clicks "No," dismiss the dialog and do nothing
                    dialog.dismiss();
                })
                .show();
    }


}