package com.example.lastlastversiondiplom.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PaintingDao{
    @Insert
    fun insertAll(paintings: List<Painting>)

    @Query("SELECT title FROM paintings ORDER BY RANDOM() LIMIT 4")
    fun getRandomPaintingTitles(): List<String>

    @Query("SELECT author FROM paintings ORDER BY RANDOM() LIMIT 4")
    fun getRandomPaintingAuthors(): List<String>

    @Query("SELECT year FROM paintings ORDER BY RANDOM() LIMIT 4")
    fun getRandomPaintingYears(): List<String>

    @Query("SELECT image FROM paintings ORDER BY RANDOM() LIMIT 4")
    fun getRandomPaintingImages(): List<ByteArray>

    @Query("SELECT * FROM paintings ORDER BY RANDOM() LIMIT 1")
    fun getRandomPaintingLiveData(): LiveData<Painting>

    @Query("SELECT * FROM paintings")
    fun getAllPaintings(): List<Painting>


    @Query("SELECT DISTINCT author FROM paintings")
    fun getAllAuthors(): List<String>
}