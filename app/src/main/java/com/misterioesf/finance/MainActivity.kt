package com.misterioesf.finance

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.ui.*
import com.misterioesf.finance.viewModel.MainVIewModel

const val PERMISSIONS_CODE = 100
class MainActivity : AppCompatActivity(), AddNewObjectCallback {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var homeFragment: HomeFragment
    private lateinit var mainVIewModel: MainVIewModel
    private val permissionList = arrayOf<String>("android.permission.READ_SMS")

    override fun onTransferSelected(accountId: Int, currency: Currencies, transfer: Transfer?) {
        val fragment =
            TransferFragment.newInstance(accountId, currency, transfer) { supportFragmentManager.popBackStack() }
        doTransaction(fragment)
    }

    override fun onAccountSelected(account: Account?) {
        val fragment =
            AccountHomeFragment.newInstance(account) { supportFragmentManager.popBackStack() }
        doTransaction(fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        homeFragment = HomeFragment.newInstance()

        doTransaction(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            try {
                when (it.itemId) {
                    R.id.homeFragment -> {
                        doTransaction(HomeFragment.newInstance())
                        true
                    }
                    // by account spinner
                    R.id.allTransfersFragment -> {
                        doTransaction(TransfersListFragment.newInstance())
                        true
                    }
                    R.id.allAccountsFragment -> {
                        doTransaction(AccountListFragment.newInstance())
                        true
                    }
                    else -> {
                        doTransaction(HomeFragment.newInstance())
                        true
                    }
                }
            } catch (e: Exception) {
                Log.e("Main", e.stackTraceToString())
                true
            }
        }

        mainVIewModel = ViewModelProvider(this)[MainVIewModel::class.java]
        requestPermissions(permissionList, PERMISSIONS_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  permission granted, proceed with camera-related tasks
                } else {
                    //  permission denied, handle accordingly
                }
            }
        }
    }

    private fun doTransaction(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}