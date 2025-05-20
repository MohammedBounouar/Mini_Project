package com.example.mini_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import com.example.mini_project.Room;


public class RoomsListAdapter extends ArrayAdapter<Room> {
    private List<Room> rooms;
    private boolean showDelete;

    public RoomsListAdapter(Context context, List<Room> rooms, boolean showDelete) {
        super(context, 0, rooms);
        this.rooms = rooms;
        this.showDelete = showDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room room = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_rooms_list_item, parent, false);
        }

        // Room name and status
        TextView textRoom = convertView.findViewById(R.id.textRoom);
        textRoom.setText(room.getName());

        TextView textStatus = convertView.findViewById(R.id.textStatus);
        boolean isReserved = room.isBooked();
        textStatus.setText(isReserved ? "Reserved" : "Available");
        textStatus.setTextColor(isReserved ? android.graphics.Color.RED : android.graphics.Color.GREEN);

        // Basic info (size, capacity, price)
        TextView textSize = convertView.findViewById(R.id.textSize);
        textSize.setText(room.getSize());

        TextView textCapacity = convertView.findViewById(R.id.textCapacity);
        textCapacity.setText(room.getCapacity() + " people");

        TextView textPrice = convertView.findViewById(R.id.textPrice);
        textPrice.setText("$" + room.getPrice() + "/hr");


        LinearLayout featuresRow = convertView.findViewById(R.id.featuresRow);
        featuresRow.removeAllViews(); // Clear previous content

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

        ImageView iconDelete = convertView.findViewById(R.id.iconDelete);
        if (iconDelete != null) {
            iconDelete.setVisibility(showDelete ? View.VISIBLE : View.GONE);

            if (showDelete) {
                iconDelete.setOnClickListener(v -> {
                    DBHelper dbHelper = new DBHelper(getContext());
                    dbHelper.deleteRoom(room.getId());
                    rooms.remove(position);
                    notifyDataSetChanged();
                });
            }
        }

        return convertView;
    }

    private int getFeatureIcon(String feature) {
        switch (feature) {
            case "Projector":
                return android.R.drawable.ic_menu_slideshow;
            case "WiFi":
                return android.R.drawable.ic_menu_share;
            case "AC":
            case "Air Conditioning":
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

