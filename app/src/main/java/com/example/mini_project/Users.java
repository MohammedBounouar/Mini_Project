package com.example.mini_project;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class Users extends AppCompatActivity {
    ListView listView;
    RoomsListAdapter adapter;
    List<Room> roomList;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        listView = findViewById(R.id.usersListView);
        dbHelper = new DBHelper(this);

        roomList = dbHelper.getAllRooms();

        adapter = new RoomsListAdapter(this, roomList, false); // Hide delete
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Room selectedRoom = roomList.get(position);
            showBookingDialog(selectedRoom);
        });

    }
    private void showBookingDialog(Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.activity_booking, null);
        builder.setView(view);

        EditText email = view.findViewById(R.id.editEmail);
        EditText phone = view.findViewById(R.id.editPhone);
        EditText date = view.findViewById(R.id.editDate);
        EditText startHour = view.findViewById(R.id.editStartHour);
        EditText endHour = view.findViewById(R.id.editEndHour);
        Button btnBook = view.findViewById(R.id.btnBook);

        AlertDialog dialog = builder.create();

        btnBook.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String phoneText = phone.getText().toString().trim();
            String dateText = date.getText().toString().trim();
            String start = startHour.getText().toString().trim();
            String end = endHour.getText().toString().trim();

            if (!emailText.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                email.setError("Invalid email");
                return;
            }

            if (!start.matches("\\d{1,2}:\\d{2}") || !end.matches("\\d{1,2}:\\d{2}")) {
                Toast.makeText(this, "Invalid time format. Use HH:mm", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper db = new DBHelper(this);
            boolean success = db.insertBooking(room.getId(), emailText, phoneText, dateText, start, end);

            if (success) {
                Toast.makeText(this, "Room booked!", Toast.LENGTH_SHORT).show();
                room.setBooked(true);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to book room", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }



}
