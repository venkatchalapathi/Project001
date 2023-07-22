package com.example.project001

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project001.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.btnSubmit.setOnClickListener {
            //Validate all fields

            //Submit data to fb

            pushFormToFirebase()
        }
    }

    private fun pushFormToFirebase() {

    }
}