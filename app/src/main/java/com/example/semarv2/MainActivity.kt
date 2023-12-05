package com.example.semarv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.semarv2.databinding.ActivityMainBinding
import com.example.semarv2.features.home.HomeFragment
import com.example.semarv2.features.kuis.QuestionActivity
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            initNavHost()
            setUpBottomNavigation()
        }
    }

    private fun ActivityMainBinding.setUpBottomNavigation() {
        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(HOME_ITEM, getString(R.string.home), R.drawable.ic_home),
            CurvedBottomNavigation.Model(SCAN_ITEM, getString(R.string.scan), R.drawable.ic_scan),
            CurvedBottomNavigation.Model(PROFILE_ITEM, getString(R.string.profile), R.drawable.ic_profile),
        )
        bottomNavigation.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener {
                navController.navigate(it.id)
            }
            // optional
            setupNavController(navController)
        }
    }

    private fun initNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set the start destination for the navigation graph
        val navGraph = navController.graph
    }
//    override fun onBackPressed() {
//        if (navController.currentDestination!!.id == HOME_ITEM)
//            super.onBackPressed()
//        else {
//            when (navController.currentDestination!!.id) {
//                SCAN_ITEM -> {
//                    navController.popBackStack(R.id.homeFragment, false)
//                }
//                PROFILE_ITEM -> {
//                    navController.popBackStack(R.id.homeFragment, false)
//                }
//                else -> {
//                    navController.navigateUp()
//                }
//            }
//        }
//    }
    companion object {
        val HOME_ITEM = R.id.homeFragment
        val SCAN_ITEM = R.id.scanFragment
        val PROFILE_ITEM = R.id.profileFragment
        const val FRAGMENT_NAME = "activity_name"
    }
}