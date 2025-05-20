package com.example.mini_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RoomBooking.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ROOMS = "rooms";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_CAPACITY = "capacity";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IS_BOOKED = "isBooked";
    private static final String COLUMN_FEATURES = "features";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ROOMS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ROOMS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SIZE + " TEXT, " +
                COLUMN_CAPACITY + " INTEGER, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IS_BOOKED + " INTEGER, " +
                COLUMN_FEATURES + " TEXT)";
        db.execSQL(CREATE_ROOMS_TABLE);

        // ✅ Create bookings table if not exists
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE IF NOT EXISTS bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "room_id INTEGER, " +
                "user_id INTEGER, " +
                "date TEXT, " +
                "start_hour TEXT, " +
                "end_hour TEXT)";
        db.execSQL(CREATE_BOOKINGS_TABLE);


        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "role TEXT)";
        db.execSQL(CREATE_USERS_TABLE);


        defaultUser(db);
    }
    private void defaultUser(SQLiteDatabase db) {
        Cursor cursor = db.query("users", new String[]{"id"},
                "email = ?", new String[]{"admin@example.com"}, null, null, null);

        boolean adminExists = cursor != null && cursor.moveToFirst();

        if (cursor != null) cursor.close();

        if (!adminExists) {
            ContentValues values = new ContentValues();
            values.put("name", "admin");
            values.put("email", "admin@example.com");
            values.put("password", "admin123");
            values.put("role", "admin");


            db.insert("users", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS bookings");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }



    public void addRoom(Room room) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, room.getName());
        values.put(COLUMN_SIZE, room.getSize());
        values.put(COLUMN_CAPACITY, room.getCapacity());
        values.put(COLUMN_PRICE, room.getPrice());
        values.put(COLUMN_IS_BOOKED, room.isBooked() ? 1 : 0);
        values.put(COLUMN_FEATURES, String.join(",", room.getFeatures()));

        db.insert(TABLE_ROOMS, null, values);
        db.close();
    }



    public List<Room> getAllRooms() {
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ROOMS, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE));
                int capacity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAPACITY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                boolean isBooked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_BOOKED)) == 1;
                String featuresStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FEATURES));
                List<String> features = featuresStr.isEmpty() ? new ArrayList<>() : Arrays.asList(featuresStr.split(","));

                Room room = new Room(id, name, size, capacity, price, isBooked, features);
                roomList.add(room);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return roomList;
    }

    public Room getRoomById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROOMS, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE));
            int capacity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAPACITY));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
            boolean isBooked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_BOOKED)) == 1;
            String featuresStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FEATURES));
            List<String> features = featuresStr.isEmpty() ? new ArrayList<>() : Arrays.asList(featuresStr.split(","));

            cursor.close();
            return new Room(id, name, size, capacity, price, isBooked, features);
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    public boolean insertBooking(int roomId, String email, String phone, String date, String startHour, String endHour) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("users", new String[]{"id"}, "email = ?", new String[]{email}, null, null, null);
        int userId;
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
        } else {
            ContentValues userValues = new ContentValues();
            userValues.put("name", email); // or collect a name field from UI later
            userValues.put("email", email);
            userId = (int) db.insert("users", null, userValues);
        }


        ContentValues bookingValues = new ContentValues();
        bookingValues.put("room_id", roomId);
        bookingValues.put("user_id", userId);
        bookingValues.put("date", date);
        bookingValues.put("start_hour", startHour);
        bookingValues.put("end_hour", endHour);

        long result = db.insert("bookings", null, bookingValues);


        ContentValues updateRoom = new ContentValues();
        updateRoom.put("isBooked", 1);
        db.update("rooms", updateRoom, "id = ?", new String[]{String.valueOf(roomId)});

        return result != -1;
    }




    public void deleteRoom(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ROOMS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }




    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users",
                new String[]{"id", "name", "email", "password", "role"},
                "email=?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            cursor.close();

            return new User(id, name, userEmail, password, role);
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }


    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"id"}, "email=?", new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            return id;
        }
        return -1;
    }
    public long insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("role", "user");
        return db.insert("users", null, values);
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        String query = "SELECT users.name AS userName, rooms.name AS roomName, bookings.date AS day " +
                "FROM bookings " +
                "JOIN users ON bookings.user_id = users.id " +
                "JOIN rooms ON bookings.room_id = rooms.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"));
                String roomName = cursor.getString(cursor.getColumnIndexOrThrow("roomName"));
                String day = cursor.getString(cursor.getColumnIndexOrThrow("day"));

                list.add(new Reservation(userName, roomName, day));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }



}
