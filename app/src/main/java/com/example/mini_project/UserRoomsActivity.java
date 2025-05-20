package com.example.mini_project;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class UserRoomsActivity extends AppCompatActivity {
    private ListView usersListView;
    private user_roomsListAdapter adapter;
    private List<Room> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersListView = findViewById(R.id.usersListView);

        DBHelper dbHelper = new DBHelper(this);
        rooms = dbHelper.getAllRooms();

        adapter = new user_roomsListAdapter(this, rooms);
        usersListView.setAdapter(adapter);

    }
}
