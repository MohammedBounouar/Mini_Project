package com.example.mini_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;


public class ReservationAdapter extends ArrayAdapter<Reservation> {
    private Context context;
    private List<Reservation> reservations;
    private OnReservationActionListener listener;

    public ReservationAdapter(Context context, List<Reservation> reservations, OnReservationActionListener listener) {
        super(context, 0, reservations);
        this.context = context;
        this.reservations = reservations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.reservation_item, parent, false);
        }

        Reservation reservation = reservations.get(position);

        TextView user = convertView.findViewById(R.id.tvUser);
        TextView room = convertView.findViewById(R.id.tvRoom);
        TextView day = convertView.findViewById(R.id.tvDay);
        ImageButton btnAccept = convertView.findViewById(R.id.btnAccept);
        ImageButton btnRefuse = convertView.findViewById(R.id.btnRefuse);

        user.setText("User: " + reservation.getUserName());
        room.setText("Room: " + reservation.getRoomName());
        day.setText("Day: " + reservation.getDay());

        btnAccept.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAccept(reservation);
            }
        });

        btnRefuse.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRefuse(reservation);
            }
        });

        return convertView;
    }

    public interface OnReservationActionListener {
        void onAccept(Reservation reservation);
        void onRefuse(Reservation reservation);
    }
}

