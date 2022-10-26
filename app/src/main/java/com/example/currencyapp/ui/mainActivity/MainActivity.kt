package com.example.currencyapp.ui.mainActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.currencyapp.R
import com.example.currencyapp.TAG
import com.example.currencyapp.databinding.ActivityMainBinding
import com.example.currencyapp.domain.services.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        setupNetworkObserver()
    }

    private fun setupNetworkObserver() {
        viewModel.networkStatus.observe(this) { status ->
            Log.d(TAG, "ConnectivityStatus: $status")

            binding.tvNoInternetConnection.isVisible =
                status != ConnectivityObserver.Status.Available
        }
    }

    private fun setupNavigation() {
        val navController = getNavController()

        val appBarConfiguration = getAppBarConfiguration()

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun getAppBarConfiguration() = AppBarConfiguration(
        setOf(
            R.id.navigation_currencies, R.id.navigation_news
        )
    )

    private fun getNavController(): NavController {
        //Need this approach to get navController when using FragmentContainerView as nav_host_fragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

}