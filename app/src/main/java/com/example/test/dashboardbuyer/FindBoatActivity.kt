package com.example.test.dashboardbuyer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.SeafoodAdapter
import com.example.test.SeafoodItem
import com.google.firebase.database.*

class FindBoatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SeafoodAdapter
    private lateinit var seafoodList: MutableList<SeafoodItem>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_boat)

        initializeViews()

        retrieveBoatSeafoodItems()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewBoatSeafood)
        seafoodList = mutableListOf()
        adapter = SeafoodAdapter(seafoodList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().reference.child("seafood_items")
    }

    private fun retrieveBoatSeafoodItems() {
        val query = databaseReference.orderByChild("source").equalTo("From Boat")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
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
}
