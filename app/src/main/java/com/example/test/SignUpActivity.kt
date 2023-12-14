package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class SignUpActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var loginRedirect: TextView
    private lateinit var spinnerUserType: Spinner

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Authentication and Realtime Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users")

        // Initialize Views (same as before)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etRepassword = findViewById(R.id.et_repassword)
        btnRegister = findViewById(R.id.btn_register) // Initialize btnRegister here
        loginRedirect = findViewById(R.id.loginRedirect)
        spinnerUserType = findViewById(R.id.spinner_user_type)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val repassword = etRepassword.text.toString().trim()
            val userType = spinnerUserType.selectedItem.toString()

            if (password == repassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                val user = User(
                                    name,
                                    email,
                                    password,
                                    userType
                                ) // Save user type in the User object
                                if (userType == "Seller") {
                                    val sellerId = generateSellerId() // Generate unique seller ID
                                    user.sellerId =
                                        sellerId // Assign unique seller ID to the seller
                                } else if (userType == "Buyer") {
                                    val buyerId = generateBuyerId()
                                    user.buyerId = buyerId
                                }

                                database.child(userId).setValue(user)
                            }
                            navigateToLoginActivity()
                        } else {
                            // Handle registration failure
                        }
                    }
            } else {
                // Handle password mismatch error (same as before)
                etRepassword.error = "Passwords do not match"
            }
        }

        loginRedirect.setOnClickListener {
            // Navigate to the LoginActivity when "Already registered? Login here" is clicked
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finish this activity so the user cannot go back to the registration screen
    }

    private fun generateSellerId(): String {
        // Generate a unique seller ID
        return UUID.randomUUID().toString()
    }

    private fun generateBuyerId(): String {
        // Generate a unique buyer ID
        return UUID.randomUUID().toString()
    }
}

data class User(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val userType: String = "",
    var sellerId: String = "",
    var buyerId: String = ""
)