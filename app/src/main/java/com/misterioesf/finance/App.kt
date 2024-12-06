package com.misterioesf.finance

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.room.Room
import com.misterioesf.finance.dao.FinanceDatabase
import com.misterioesf.finance.repository.CurrencyRepository
import com.misterioesf.finance.repository.FinanceRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
}