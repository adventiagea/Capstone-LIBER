package com.dicoding.picodiploma.capstone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dicoding.picodiploma.capstone.databinding.ActivityLoginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding : ActivityLoginBinding
    private lateinit var loginDB : DatabaseReference
    lateinit var sharedPreferences: SharedPreferences
    val PREFERENCES_NAME = "liber_preferences"
    val KEY_USER = "key_user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        loginBinding.button.setOnClickListener {
            val username = loginBinding.UsernameInput.text.toString()

            if (username.isNotEmpty()){

                readDataUsername(username)
                saveUser(username)
            }
            else {
                Toast.makeText(this, "Please enter username and password!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUser(username: String){
        val user : SharedPreferences.Editor = sharedPreferences.edit()

        user.putString(KEY_USER, username)
        user.apply()
    }

    private fun readDataUsername(username: String){
        loginDB = FirebaseDatabase.getInstance().getReference("User")

        val usernameInput = loginBinding.UsernameInput.text.toString()
        val passwordInput = loginBinding.editTextTextPassword2.text.toString()

        loginDB.child(username).get().addOnSuccessListener {
            if (it.exists()) {
                val usernameData = it.child("id").value.toString()
                val passwordData = it.child("pass").value.toString()

                if (usernameInput==usernameData && passwordInput == passwordData){
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "Username or Password is wrong!", Toast.LENGTH_SHORT).show()

                }

            }

            else {
                Toast.makeText(this, "Username doesn't exist!", Toast.LENGTH_SHORT).show()

            }
        }.addOnFailureListener {
            Toast.makeText(this, "Username doesn't exist!", Toast.LENGTH_SHORT).show()
        }
    }

}