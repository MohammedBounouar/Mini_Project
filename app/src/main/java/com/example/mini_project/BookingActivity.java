package com.example.mini_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookingActivity extends AppCompatActivity {
    EditText editEmail, editPhone, editDate, editStartHour, editEndHour;
    Button btnBook;
    int roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        roomId = getIntent().getIntExtra("roomId", -1);

        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editDate = findViewById(R.id.editDate);
        editStartHour = findViewById(R.id.editStartHour);
        editEndHour = findViewById(R.id.editEndHour);
        btnBook = findViewById(R.id.btnBook);

        btnBook.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String date = editDate.getText().toString().trim();
            String startHour = editStartHour.getText().toString().trim();
            String endHour = editEndHour.getText().toString().trim();

            if (email.isEmpty() || phone.isEmpty() || date.isEmpty() || startHour.isEmpty() || endHour.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(this);
            boolean success = dbHelper.insertBooking(roomId, email, phone, date, startHour, endHour);
            if (success) {
                Toast.makeText(this, "Room booked successfully!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to book room", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
