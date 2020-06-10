package com.example.papipel.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.papipel.Models.Product

// Constants for the database
val DATABASE_NAME = "Papipel"
val DATABASE_VERSION = 1
val COL_ID = "id"

// Constants for the table Products
val TABLE_NAME_PRODUCTS = "Products"
val COL_PRODUCT_ID = "product_id"
val COL_NAME = "name"
val COL_CATEGORY = "category"
val COL_PRICE = "price"
val COL_QUANTITY = "quantity"
val COL_DESCRIPTION = "description"
val COL_ACTIVE = "active"
val CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_NAME_PRODUCTS + " (" +
        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COL_PRODUCT_ID + " VARCHAR(256)," +
        COL_NAME + " VARCHAR(256)," +
        COL_CATEGORY + " VARCHAR(256)," +
        COL_PRICE + " VARCHAR(256)," +
        COL_QUANTITY + " INTEGER," +
        COL_DESCRIPTION + " VARCHAR(256)," +
        COL_ACTIVE + " INTEGER)"

// Constants for the table Order
val TABLE_NAME_ORDERS = "Orders"
val COL_VALUE = "value"
val COL_DATE = "date"
val CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_NAME_ORDERS + " (" +
        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COL_PRODUCT_ID + " VARCHAR(256)," +
        COL_VALUE + " VARCHAR(256)," +
        COL_DATE + " VARCHAR(256))"

open class DatabaseHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_PRODUCTS)
        db?.execSQL(CREATE_TABLE_ORDERS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}