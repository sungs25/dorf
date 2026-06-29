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
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDorfDatabase(@ApplicationContext context: Context): DorfDatabase {
        return Room.databaseBuilder(context, DorfDatabase::class.java, "dorf.db")
            .setDriver(BundledSQLiteDriver())          // ← 필수. 이게 빠져서 지금 안 됨
            .setQueryCoroutineContext(Dispatchers.IO)  // ← 권장. Room 3은 Executor 대신 코루틴 컨텍스트를 받음
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideWordDao(db: DorfDatabase): WordDao {
        return db.wordDao
    }

}