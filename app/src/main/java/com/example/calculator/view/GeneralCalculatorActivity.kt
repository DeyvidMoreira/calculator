package com.example.calculator.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.R
import com.example.calculator.databinding.ActivityGeneralCalculatorBinding

class GeneralCalculatorActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGeneralCalculatorBinding

    private lateinit var btnHistoric: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGeneralCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarGeneralCalculator.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_general_calculator)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_simple_calculator,
                R.id.nav_scientific_calculator,
                R.id.nav_measurement_converter
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        historic()
    }

    fun historic() {
        btnHistoric = findViewById(R.id.btn_histotic)
        btnHistoric.setOnClickListener {
            Toast.makeText(applicationContext, "histórico", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_general_calculator)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}