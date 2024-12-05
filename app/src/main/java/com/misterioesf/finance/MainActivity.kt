package com.misterioesf.finance

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.misterioesf.finance.viewModel.MainVIewModel

const val PERMISSIONS_CODE = 100

class MainActivity : AppCompatActivity() {
    private lateinit var mainVIewModel: MainVIewModel
    private val permissionList = arrayOf<String>("android.permission.READ_SMS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setupWithNavController(navController)

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
}