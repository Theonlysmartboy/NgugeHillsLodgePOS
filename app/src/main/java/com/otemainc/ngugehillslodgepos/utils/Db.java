package com.otemainc.ngugehillslodgepos.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db extends SQLiteOpenHelper {
    // on database schema change, you must increment database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ngugeHills.db";
    //create users table
    private static final String SQL_CREATE_USERS_TABLE =  "CREATE TABLE tbl_users (" +
            "id int(20) NOT NULL," +
            "name varchar(60) NOT NULL," +
            "email varchar(60) NOT NULL," +
            "phone varchar(12) NOT NULL," +
            "token varchar(255) NOT NULL," +
            "refferal_code varchar(255) NOT NULL," +
            "town varchar(100)," +
            "longi varchar(100)," +
            "lati varchar(100));";
    //Create cart table
    private static final String SQL_CREATE_CART_TABLE =
            "CREATE TABLE tbl_cart_items (" +
                    "id int(20) NOT NULL," +
                    "name varchar(100) NOT NULL," +
                    "amount varchar(60) NOT NULL," +
                    "quantity int(11) NOT NULL);";
    private static final String SQL_CREATE_WISH_LIST_TABLE =
            "CREATE TABLE tbl_wish_list_items (" +
                    "id int(20) NOT NULL," +
                    "name varchar(100) NOT NULL," +
                    "amount varchar(60) NOT NULL," +
                    "quantity int(11) NOT NULL);";
    //delete users table
    private static final String SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS tbl_users";
    private static final String SQL_DELETE_CART_TABLE =
            "DROP TABLE IF EXISTS tbl_cart_items";
    private static final String SQL_DELETE_WISH_LIST_TABLE =
            "DROP TABLE IF EXISTS tbl_wish_list_items";
    public Db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_CART_TABLE);
        db.execSQL(SQL_CREATE_WISH_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion <10){
            NgugeHillsLodge.getContext()
                    .getSharedPreferences("UserSessionPref", 0)
                    .edit()
                    .putBoolean("IsLoggedIn",false)
                    .commit();
            db.execSQL(SQL_DELETE_USERS_TABLE);
            db.execSQL(SQL_DELETE_CART_TABLE);
            db.execSQL(SQL_DELETE_WISH_LIST_TABLE);
            onCreate(db);
            Intent i = NgugeHillsLodge.getContext().getPackageManager().getLaunchIntentForPackage(
                    NgugeHillsLodge.getContext().getPackageName() );
            assert i != null;
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NgugeHillsLodge.getContext().startActivity(i);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public boolean addUser( int id, String name,String email, String phone,
                            String token, String code, String town){
        boolean added;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("id",id);
        contentValue.put("name",name);
        contentValue.put("email",email);
        contentValue.put("phone",phone);
        contentValue.put("token",token);
        contentValue.put("refferal_code",code);
        contentValue.put("town",town);
        long result = db.insert("tbl_users",null,contentValue);
        added= result != -1;
        return added;
    }
    public Cursor user(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * from tbl_users",null);
    }

    public String townName() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor user = db.rawQuery("SELECT * from tbl_users",null);
        user.moveToFirst();
        String townName = user.getString(6);
        user.close();
        return townName;
    }
    public String token() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor user = db.rawQuery("SELECT * from tbl_users",null);
        user.moveToFirst();
        String token = user.getString(4);
        user.close();
        return token;
    }
    //get refferalCode
    public String referralCode() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor user = db.rawQuery("SELECT * from tbl_users",null);
        user.moveToFirst();
        String code = user.getString(5);
        user.close();
        return code;
    }

    public void deleteUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_users");
    }

    public boolean addToCart(int id, String name, double amount, int quantity){
        boolean addedCart;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("id",id);
        contentValue.put("name",name);
        contentValue.put("amount",amount);
        contentValue.put("quantity",quantity);
        long result = db.insert("tbl_cart_items",null,contentValue);
        addedCart= result != -1;
        return addedCart;
    }
    public boolean addToWishList(int id, String name, double amount, int quantity){
        boolean added;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("id",id);
        contentValue.put("name",name);
        contentValue.put("amount",amount);
        contentValue.put("quantity",quantity);
        long result = db.insert("tbl_wish_list_items",null,contentValue);
        added= result != -1;
        return added;
    }
    public boolean updateCart(String id, String name, double amount, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("id",id);
        contentValue.put("name",name);
        contentValue.put("amount",amount);
        contentValue.put("quantity",quantity);
        db.update("tbl_cart_items",contentValue,"id=?",new String[]{id});
        return true;
    }
    public boolean updateWishList(String id, String name, double amount, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("id",id);
        contentValue.put("name",name);
        contentValue.put("amount",amount);
        contentValue.put("quantity",quantity);
        db.update("tbl_wish_list_items",contentValue,"id=?",new String[]{id});
        return true;
    }
    public Cursor cartItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select * from tbl_cart_items",null);
    }

    public Cursor wishListItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select * from tbl_wish_list_items",null);
    }
    public int deleteCartItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tbl_cart_items","id=?",new String[]{id});
    }

    public int deleteWishlistItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tbl_wish_list_items","id=?",new String[]{id});
    }

    public void clearCart(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_cart_items");
    }

    public void clearWishList(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_wish_list_items");
    }
}
