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

class App: Application() {

    private val database: FinanceDatabase by lazy {
        Room.databaseBuilder(this,  FinanceDatabase::class.java, "finance").build()
//        Room.databaseBuilder(this,  FinanceDatabase::class.java, "finance").addMigrations(
//            MIGRATION_1_2).build()
    }

    override fun onCreate() {
        super.onCreate()
        try {
            val service: CurrencyService = ApiInstance.createService(CurrencyService::class.java)
            FinanceRepository.initialize(database.getDao())
            if (isInternetOn(baseContext)) CurrencyRepository.initialize(service)
            else CurrencyRepository.initialize(null)
        } catch (e: java.lang.Exception) {

        }
        Log.e("APP","enternent ${isInternetOn(context = this.baseContext)}")
    }

    private fun isInternetOn(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}