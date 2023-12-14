package com.example.test.dashboardbuyer


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MpesaPaybill : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpesa_paybill)

        val textView: TextView = findViewById(R.id.mpesatextViewContent)

        // Receive the selected order from the previous activity
        val selectedOrder: OrderStatus? = this.intent.getParcelableExtra("selectedOrder")

        selectedOrder?.let { order ->
            // Fetch total price from the selected order
            val totalPrice = order.totalPrice

            // Fetch seller contact phone number using generatedItemId
            val generatedItemId = order.generatedItemId
            fetchSellerContact(generatedItemId) { phoneNumber ->
                // Generate the hardcoded content with retrieved data
                val hardcodedContent = "Go to MPESA menu on your phone \n" +
                        "Safaricom icon > Mpesa \n" +
                        "or \n" +
                        "Sim tool > Safaricom > Mpesa \n" +
                        "Select Send Money: \n" +
                        "Total Price: $totalPrice\n" +
                        "Seller's Contact: $phoneNumber"

                // Set the hardcoded content to the TextView
                textView.text = hardcodedContent
            }
        }
    }

    private fun fetchSellerContact(generatedItemId: String, callback: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance().reference
        val itemRef = database.child("seafood_items").child(generatedItemId)

        itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val sellerContact = dataSnapshot.child("phoneNumber").getValue(String::class.java)
                    sellerContact?.let { phoneNumber ->
                        callback.invoke(phoneNumber)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
}
