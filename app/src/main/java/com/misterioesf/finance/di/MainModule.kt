package com.misterioesf.finance.di

import android.content.Context
import androidx.room.Room
import com.misterioesf.finance.network.CurrencyService
import com.misterioesf.finance.Utils
import com.misterioesf.finance.data.dao.FinanceDao
import com.misterioesf.finance.data.dao.FinanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): FinanceDatabase {
        //.fallbackToDestructiveMigration()
//        Room.databaseBuilder(this,  FinanceDatabase::class.java, "finance").addMigrations(
//            MIGRATION_1_2).build()
        return Room.databaseBuilder(appContext, FinanceDatabase::class.java, "finance").build()
    }

    @Provides
    @Singleton
    fun provideTransferDao(appDatabase: FinanceDatabase): FinanceDao {
        return appDatabase.getDao()
    }

    @Provides
    @Singleton
    fun provideCurrencyService(
        @ApplicationContext appContext: Context
    ): CurrencyService? {
        return if (!Utils.isInternetOn(appContext)) null
        else Retrofit.Builder()
            .baseUrl("https://api.nbrb.by/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyService::class.java)
    }
}