package com.example.project001

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project001.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PreferenceManager.getUserType(this) != "" && PreferenceManager.getUserType(this) == "admin") {
            //navigate to admin activity
            var intent = Intent(this, AdminDummyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        } else if (PreferenceManager.getUserType(this) != "" && PreferenceManager.getUserType(this) == "user") {
            //Navigate to user activiy
            var intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        } else {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.login.setOnClickListener {
                binding.loading.visibility = View.VISIBLE
                checkUser()
            }
        }

    }

    private fun checkUser() {

        var db = FirebaseFirestore.getInstance()
        db.collection(MyFirebaseUtils.USERS)
            .whereEqualTo(MyFirebaseUtils.USERNAME, binding.username.text.toString().trim())
            .whereEqualTo(MyFirebaseUtils.PASSWORD, binding.password.text.toString().trim())
            .get()
            .addOnSuccessListener {
                binding.loading.visibility = View.GONE
//                Toast.makeText(this,"Success"+it?.documents.toString(),Toast.LENGTH_LONG).show()
                if (it!!.isEmpty) return@addOnSuccessListener
//                Toast.makeText(this,it.documents[0].getString("userType")!!,Toast.LENGTH_LONG).show()
                PreferenceManager.saveUser(
                    this,
                    it.documents[0].getString("userName")!!,
                    it.documents[0].getString("userType")!!
                )

                if (PreferenceManager.getUserType(this) != "" && PreferenceManager.getUserType(this) == "admin") {
                    //navigate to admin activity
                    var intent = Intent(this, AdminDummyActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else if (PreferenceManager.getUserType(this) != "" && PreferenceManager.getUserType(this) == "user") {
                    //Navigate to user activiy
                    var intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener {
                binding.loading.visibility = View.GONE
                Toast.makeText(this,"Failure "+it.message,Toast.LENGTH_LONG).show()
            }

    }
}