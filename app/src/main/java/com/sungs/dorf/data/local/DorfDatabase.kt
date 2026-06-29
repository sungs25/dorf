package com.sungs.dorf.data.local

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.sungs.dorf.Converters

@Database(entities = [Word::class], version = 1)
@ColumnTypeConverters(Converters::class)
abstract class DorfDatabase : RoomDatabase() {
    abstract val wordDao: WordDao
}