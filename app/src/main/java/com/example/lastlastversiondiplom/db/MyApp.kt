package com.example.lastlastversiondiplom.db

import android.app.Application
import androidx.room.Room

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val db = Room.databaseBuilder(
            applicationContext,
            PaintingDatabase::class.java,
            "my_database"
        ).build()

        Thread {
            db.populateDatabase()
        }.start()

    }
}