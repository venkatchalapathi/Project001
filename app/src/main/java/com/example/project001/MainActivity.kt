package com.example.project001

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project001.databinding.ActivityMainBinding
import com.example.project001.models.FormModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            //Validate all fields

            //Submit data to fb

            pushFormToFirebase()
        }
    }

    private fun pushFormToFirebase() {
        var model = FormModel(fullName = binding.etFullname.text.toString(),
        fatherName = binding.etFathername.text.toString(),
        contactNumber = binding.etContact.text.toString(),
        email = binding.etEmail.text.toString(),
        country = binding.etCountry.text.toString(),
        state = binding.etState.text.toString(),
        district = binding.etDistrict.text.toString(),
        village = binding.etVillage.text.toString())

        val db = FirebaseFirestore.getInstance()
        db.collection(MyFirebaseUtils.FORMS)
            .document()
            .set(model)
            .addOnSuccessListener { showToast("New Record Added") }
            .addOnFailureListener { showToast("Error while saving data!") }
    }

    private fun showToast(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show()
    }
}