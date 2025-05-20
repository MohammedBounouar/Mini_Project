package com.example.mini_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

public class Reservations extends AppCompatActivity implements ReservationAdapter.OnReservationActionListener {

    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 101;

    private ListView listView;
    private List<Reservation> reservations;

    private Reservation pendingReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        createNotificationChannel(this);

        listView = findViewById(R.id.listView);
        DBHelper dbHelper = new DBHelper(this);
        reservations = dbHelper.getAllReservations();

        ReservationAdapter adapter = new ReservationAdapter(this, reservations, this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onAccept(Reservation reservation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                sendNotification(reservation);
            } else {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
                pendingReservation = reservation;
            }
        } else {
            sendNotification(reservation);
        }
    }

    @Override
    public void onRefuse(Reservation reservation) {
        Toast.makeText(this, "Refused: " + reservation.getUserName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingReservation != null) {
                    sendNotification(pendingReservation);
                    pendingReservation = null;
                }
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //AI work
    private void sendNotification(Reservation reservation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted — safe to send notification
                sendNotification(reservation);
            } else {
                // Permission not granted — request it
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
                // Save reservation if needed to send notification after permission granted
                pendingReservation = reservation;
            }
        } else {
            sendNotification(reservation);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "RESERVATION_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_check)
                .setContentTitle("Reservation Accepted")
                .setContentText("You accepted the reservation for " + reservation.getUserName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = reservation.getUserName().hashCode();
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reservations Channel";
            String description = "Notifications for reservation status";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("RESERVATION_CHANNEL_ID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
