package com.misterioesf.finance.di

import com.misterioesf.finance.data.repository.CurrencyRepositoryImpl
import com.misterioesf.finance.data.repository.FinanceRepositoryImpl
import com.misterioesf.finance.domain.data.CurrencyRepository
import com.misterioesf.finance.domain.data.FinanceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideCurrencyRepository(repository: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    @Singleton
    fun provideFinanceRepository(repository: FinanceRepositoryImpl): FinanceRepository
}