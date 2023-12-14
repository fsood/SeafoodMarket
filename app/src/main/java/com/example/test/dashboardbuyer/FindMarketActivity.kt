package com.example.test.dashboardbuyer

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.SeafoodAdapter
import com.example.test.SeafoodItem
import com.google.firebase.database.*

class FindMarketActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SeafoodAdapter
    private lateinit var seafoodList: MutableList<SeafoodItem>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_market)

        val backButton: ImageButton = findViewById(R.id.backB)
        backButton.setOnClickListener {
            navigateToDashboard()
        }
        recyclerView = findViewById(R.id.recyclerViewSeafood)
        seafoodList = mutableListOf()
        adapter = SeafoodAdapter(seafoodList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().reference.child("seafood_items")

        retrieveSeafoodItems()
    }

    private fun retrieveSeafoodItems() {
        databaseReference.orderByChild("source").equalTo("From Market")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    seafoodList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val seafoodItem = snapshot.getValue(SeafoodItem::class.java)
                        seafoodItem?.let { seafoodList.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }
    private fun navigateToDashboard() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
