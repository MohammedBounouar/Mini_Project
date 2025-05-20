package com.example.mini_project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mini_project.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;

    private ListView listView;
    private List<Room> roomList;
    private RoomsListAdapter adapter;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(this);
        listView = findViewById(R.id.listView);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.toolbar, navController);


        roomList = dbHelper.getAllRooms();
        adapter = new RoomsListAdapter(this, roomList, true);
        listView.setAdapter(adapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
            View dialogView = inflater.inflate(R.layout.dialog_add_room, null);

            EditText roomNameInput = dialogView.findViewById(R.id.editTextRoomName);
            EditText roomSizeInput = dialogView.findViewById(R.id.editTextRoomSize);
            EditText roomCapacityInput = dialogView.findViewById(R.id.editTextRoomCapacity);
            EditText roomPriceInput = dialogView.findViewById(R.id.editTextRoomPrice);
            EditText roomFeaturesInput = dialogView.findViewById(R.id.editTextFeatures);

            String[] allFeatures = {"WiFi", "Projector", "Air Conditioning"};
            boolean[] selectedFeatures = new boolean[allFeatures.length];
            List<String> selectedFeaturesList = new ArrayList<>();

            roomFeaturesInput.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Select Features");
                builder.setMultiChoiceItems(allFeatures, selectedFeatures, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        selectedFeaturesList.add(allFeatures[which]);
                    } else {
                        selectedFeaturesList.remove(allFeatures[which]);
                    }
                });
                builder.setPositiveButton("OK", (dialog, which) -> {
                    roomFeaturesInput.setText(String.join(", ", selectedFeaturesList));
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            });

            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Add New Room")
                    .setView(dialogView)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String name = roomNameInput.getText().toString().trim();
                        String size = roomSizeInput.getText().toString().trim();
                        String capacityStr = roomCapacityInput.getText().toString().trim();
                        String priceStr = roomPriceInput.getText().toString().trim();

                        if (name.isEmpty() || size.isEmpty() || capacityStr.isEmpty() || priceStr.isEmpty()) {
                            Toast.makeText(HomeActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int capacity = Integer.parseInt(capacityStr);
                        double price = Double.parseDouble(priceStr);

                        Room newRoom = new Room(name, size, capacity, price, false, new ArrayList<>(selectedFeaturesList));

                        dbHelper.addRoom(newRoom);

                        roomList = dbHelper.getAllRooms();
                        adapter = new RoomsListAdapter(this, roomList, true); // Show delete
                        listView.setAdapter(adapter);

                        Toast.makeText(HomeActivity.this, "Room added: " + name, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_reservations) {
                startActivity(new Intent(HomeActivity.this, Reservations.class));
                return true;
            }
            return false;
        });
    }

    // Needs work
    private void showFilterMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedFilter = item.getTitle().toString();
            filterRooms(selectedFilter);
            return true;
        });

        popupMenu.show();
    }


    private void filterRooms(String filter) {
        List<Room> allRooms = dbHelper.getAllRooms();
        List<Room> filteredList = new ArrayList<>();

        switch (filter) {
            case "All":
                filteredList = allRooms;
                break;
            case "Reserved":
                for (Room room : allRooms) {
                    if (room.isBooked()) filteredList.add(room);
                }
                break;
            case "Available":
                for (Room room : allRooms) {
                    if (!room.isBooked()) filteredList.add(room);
                }
                break;
        }

        adapter = new RoomsListAdapter(this, roomList, true); // Show delete

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
