package com.example.marketflow.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.marketflow.firebase.FireBaseCommon
import com.example.marketflow.utlities.Constants.INTRODUCTION_FRAGMENT_SP
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireBaseFireStore() = Firebase.firestore

    @Provides
    fun provideIntroductionFragmentSP(application: Application): SharedPreferences =
        application.getSharedPreferences(INTRODUCTION_FRAGMENT_SP, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFireBaseCommon(firebaseAuth: FirebaseAuth, firebaseFireStore: FirebaseFirestore) =
        FireBaseCommon(firebaseFireStore, firebaseAuth)

    @Provides
    @Singleton
    fun providesStorage() = FirebaseStorage.getInstance().reference


}