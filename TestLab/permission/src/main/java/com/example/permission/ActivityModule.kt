package com.example.permission

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun appCompatActivity(activity: Activity): AppCompatActivity = activity as AppCompatActivity

    @ActivityScoped
    @Provides
    fun permissionManager(activity: AppCompatActivity) : PermissionManager = PermissionManager(activity)
}