package com.example.datingapp


import android.os.Bundle

import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat

import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.datingapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    var actionBarDrawerToggle: ActionBarDrawerToggle ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        actionBar?.show()

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerlayout, R.string.Open, R.string.Close)


        binding.drawerlayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navigationView.setNavigationItemSelectedListener(this)

        val navController = findNavController(R.id.fragment)

        NavigationUI.setupWithNavController(binding.bottom, navController)


    }







    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.rateus -> {
                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()

            }
            R.id.rateus -> {
                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()

            }
            R.id.rateus -> {
                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()

            }
            R.id.rateus -> {
                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()

            }
            R.id.rateus -> {
                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()

            }
            R.id.rateus -> {
                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()

            }

        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if(actionBarDrawerToggle!!.onOptionsItemSelected(item))
        {
            true
        }
        else{
            super.onOptionsItemSelected(item)
        }
    }



    override fun onBackPressed() {


        if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)){

            binding.drawerlayout.close()

        }
        else{
            super.onBackPressed()
        }
    }
}