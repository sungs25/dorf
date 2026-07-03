package com.sungs.dorf.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    // Firestore 인스턴스는 앱 전체에서 하나. 싱글톤.
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore
}