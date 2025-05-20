package com.example.mini_project;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
        import androidx.cardview.widget.CardView;
import java.util.List;

public class user_roomsListAdapter extends ArrayAdapter<Room> {
    private List<Room> rooms;

    public user_roomsListAdapter(Context context, List<Room> rooms) {
        super(context, 0, rooms);
        this.rooms = rooms;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room room = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_rooms_list_item, parent, false);
        }

        // Bind views
        TextView textRoom = convertView.findViewById(R.id.textRoom);
        TextView textStatus = convertView.findViewById(R.id.textStatus);
        TextView textSize = convertView.findViewById(R.id.textSize);
        TextView textCapacity = convertView.findViewById(R.id.textCapacity);
        TextView textPrice = convertView.findViewById(R.id.textPrice);
        LinearLayout featuresRow = convertView.findViewById(R.id.featuresRow);
        ImageView iconDelete = convertView.findViewById(R.id.iconDelete);

        textRoom.setText(room.getName());
        boolean isReserved = room.isBooked();
        textStatus.setText(isReserved ? "Reserved" : "Available");
        textStatus.setTextColor(isReserved ? android.graphics.Color.RED : android.graphics.Color.GREEN);
        textSize.setText(room.getSize());
        textCapacity.setText(room.getCapacity() + " people");
        textPrice.setText("$" + room.getPrice() + "/hr");

        // Features
        featuresRow.removeAllViews();
        for (String feature : room.getFeatures()) {
            LinearLayout featureLayout = new LinearLayout(getContext());
            featureLayout.setOrientation(LinearLayout.HORIZONTAL);

            ImageView featureIcon = new ImageView(getContext());
            featureIcon.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
            featureIcon.setImageResource(getFeatureIcon(feature));

            TextView featureText = new TextView(getContext());
            featureText.setText(feature);
            featureText.setTextSize(13f);
            featureText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            featureLayout.addView(featureIcon);
            featureLayout.addView(featureText);
            featuresRow.addView(featureLayout);
        }


        convertView.setOnClickListener(v -> {
            Log.d("Adapter", "convertView clicked: " + room.getName());
            Toast.makeText(getContext(), "Item clicked: " + room.getName(), Toast.LENGTH_SHORT).show();
            if (!room.isBooked()) {
                showReservationDialog(room);
            } else {
                showReservedDialog(room);
            }
        });



        return convertView;
    }


    private void showReservationDialog(Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reserve Room");

        final EditText input = new EditText(getContext());
        input.setHint("Enter reservation day (e.g. Monday)");
        builder.setView(input);

        builder.setPositiveButton("Reserve", (dialog, which) -> {
            String day = input.getText().toString().trim();
            Toast.makeText(getContext(), "Room reserved for " + day, Toast.LENGTH_SHORT).show();
            room.setBooked(true);
            notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showReservedDialog(Room room) {
        new AlertDialog.Builder(getContext())
                .setTitle("Room Reserved")
                .setMessage("This room is already reserved for the selected day.")
                .setPositiveButton("OK", null)
                .show();
    }

    private int getFeatureIcon(String feature) {
        switch (feature) {
            case "Projector":
                return android.R.drawable.ic_menu_slideshow;
            case "WiFi":
                return android.R.drawable.ic_menu_share;
            case "AC":
                return android.R.drawable.ic_menu_compass;
            default:
                return android.R.drawable.ic_menu_info_details;
        }
    }

    public void updateRooms(List<Room> newRooms) {
        rooms.clear();
        rooms.addAll(newRooms);
        notifyDataSetChanged();
    }
}
