package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.test.dashboardbuyer.MainActivity
import com.example.test.dashboardseller.MainActivitySeller
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var loginRedirect: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users")

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        loginRedirect = findViewById(R.id.loginRedirect)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInWithEmailAndPassword(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        loginRedirect.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    if (user != null) {
                        // Fetch additional user data from Realtime Database
                        val userId = user.uid
                        database.child(userId).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userData = snapshot.getValue(User::class.java)
                                if (userData != null) {
                                    val userType = userData.userType // Fetch user type
                                    // Check user type and navigate accordingly
                                    if (userType == "Buyer") {
                                        navigateToMainActivity()
                                    } else if (userType == "Seller") {
                                        navigateToSellerActivity()
                                    } else {
                                        // Handle other types or scenarios
                                    }
                                } else {
                                    // User data not found in Realtime Database
                                    Toast.makeText(this@LoginActivity, "User data not found", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle database error
                                Toast.makeText(this@LoginActivity, "Database error", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        // Add any necessary extras or flags
        startActivity(intent)
        finish()
    }

    private fun navigateToSellerActivity() {
        val intent = Intent(this, MainActivitySeller::class.java)
        // Add any necessary extras or flags for seller activity
        startActivity(intent)
        finish()
    }
}