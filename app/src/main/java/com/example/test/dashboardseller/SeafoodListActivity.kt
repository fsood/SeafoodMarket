package com.example.test.dashboardseller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.SeafoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SeafoodListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seafood_list)

        recyclerView = findViewById(R.id.recyclerViewSeafood)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch and populate the seafood items list
        fetchSeafoodItems()
    }

    private fun fetchSeafoodItems() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val seafoodRef = FirebaseDatabase.getInstance().reference
                .child("seafood_items") // Reference the seafood_items directly
                .orderByChild("sellerId") // Assuming the field name in SeafoodItem is sellerId
                .equalTo(userId) // Filter by the logged-in seller's ID

            seafoodRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val seafoodList = mutableListOf<SeafoodItem>()

                    for (itemSnapshot in snapshot.children) {
                        val seafoodItem = itemSnapshot.getValue(SeafoodItem::class.java)
                        if (seafoodItem != null) {
                            seafoodList.add(seafoodItem)
                        }
                    }

                    // Set up the adapter with the seafood list
                    val adapter = SeafoodListAdapter(seafoodList)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Toast.makeText(
                        this@SeafoodListActivity,
                        "Database error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
