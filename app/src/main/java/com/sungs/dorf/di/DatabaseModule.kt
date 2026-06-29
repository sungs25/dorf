package com.sungs.dorf.di

import android.content.Context
import androidx.room3.Room
import com.sungs.dorf.data.local.DorfDatabase
import com.sungs.dorf.data.local.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDorfDatabase(@ApplicationContext context: Context): DorfDatabase {
        return Room.databaseBuilder(
            context,
            DorfDatabase::class.java,
            "dorf.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideWordDao(db: DorfDatabase): WordDao {
        return db.wordDao
    }

}