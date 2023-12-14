package com.example.test.dashboardbuyer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.SeafoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MakeOrderActivity : AppCompatActivity() {
    private lateinit var kgEditText: EditText
    private lateinit var deliveryInfoEditTextName: EditText
    private lateinit var deliveryInfoEditTextPhone: EditText
    private lateinit var deliveryInfoEditTextLocation: EditText
    private lateinit var orderDetailsTextView: TextView
    private lateinit var makeOrderButton: Button
    private lateinit var clearOrderButton: Button
    private lateinit var submitOrderButton: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var ordersRef: DatabaseReference
    private lateinit var selectedItem: SeafoodItem
    private lateinit var paymentMethodTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_order)

        selectedItem = intent.getParcelableExtra<SeafoodItem>("SELECTED_ITEM") ?: return

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        ordersRef = database.reference.child("orders")

        // Initialize views
        kgEditText = findViewById(R.id.kgEditText)
        deliveryInfoEditTextName = findViewById(R.id.deliveryInfoEditTextName)
        deliveryInfoEditTextPhone = findViewById(R.id.deliveryInfoEditTextPhone)
        deliveryInfoEditTextLocation = findViewById(R.id.deliveryInfoEditTextLocation)
        orderDetailsTextView = findViewById(R.id.orderDetailsTextView)
        paymentMethodTextView = findViewById(R.id.paymentMethodTextView)

        makeOrderButton = findViewById(R.id.makeOrderButton)
        clearOrderButton = findViewById(R.id.clearOrderButton)
        submitOrderButton = findViewById(R.id.submitOrderButton)

        paymentMethodTextView.text = selectedItem.payment

        makeOrderButton.setOnClickListener {
            // Get values from EditText fields
            val kgQuantity = kgEditText.text.toString()
            val deliveryName = deliveryInfoEditTextName.text.toString()
            val deliveryPhone = deliveryInfoEditTextPhone.text.toString()
            val deliveryLocation = deliveryInfoEditTextLocation.text.toString()

            // Generate order details
            val orderDetails =
                generateOrderDetails(kgQuantity, deliveryName, deliveryPhone, deliveryLocation)
            orderDetailsTextView.text = orderDetails // Update order details TextView
        }

        clearOrderButton.setOnClickListener {
            // Clear EditText fields
            kgEditText.text.clear()
            deliveryInfoEditTextName.text.clear()
            deliveryInfoEditTextLocation.text.clear()
            deliveryInfoEditTextPhone.text.clear()

            // Clear order details TextView
            orderDetailsTextView.text = ""
        }

        submitOrderButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser ?: return@setOnClickListener
            val userId = currentUser.uid

            val sellerId = selectedItem.sellerId
            val generatedItemId = selectedItem.generatedItemId

            val kgQuantity = kgEditText.text.toString()
            val deliveryName = deliveryInfoEditTextName.text.toString()
            val deliveryPhone = deliveryInfoEditTextPhone.text.toString()
            val deliveryLocation = deliveryInfoEditTextLocation.text.toString()
            val fishType = selectedItem.fishType

            saveOrder(
                kgQuantity,
                deliveryName,
                deliveryPhone,
                deliveryLocation,
                fishType,
                userId,
                sellerId,
                generatedItemId
            )

            updateSelectedItemAvailability(generatedItemId, kgQuantity.toDouble())
        }
    }

    private fun saveOrder(
        kgQuantity: String,
        deliveryName: String,
        deliveryPhone: String,
        deliveryLocation: String,
        fishType: String,
        buyerId: String,
        sellerId: String,
        generatedItemId: String
    ) {
        val totalPrice = calculateTotalPrice(kgQuantity)

        val order = Order(
            totalPrice = totalPrice,
            kgQuantity = kgQuantity,
            deliveryName = deliveryName,
            deliveryPhone = deliveryPhone,
            deliveryLocation = deliveryLocation,
            fishType = fishType,
            buyerId = buyerId,
            sellerId = sellerId,
            generatedItemId = generatedItemId
        )

        val generalOrdersRef = ordersRef.child("orders")
        val orderId = generalOrdersRef.push().key ?: ""

        order.orderId = orderId

        generalOrdersRef.child(orderId).setValue(order)
            .addOnSuccessListener {
                val alertDialog = AlertDialog.Builder(this)
                    .setMessage("Order placed successfully!")
                    .setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this, FindSeaFood::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                    .setCancelable(false)
                    .create()
                alertDialog.show()
            }
            .addOnFailureListener {
                // Handle failure for user's orders
            }
    }

    private fun generateOrderDetails(quantity: String, name: String, phone: String, location: String): String {
        val deliveryInfo = "Name: $name\nPhone: $phone\nLocation: $location"
        return "Total Price: ${calculateTotalPrice(quantity)}\nQuantity Ordered: $quantity kg\n$deliveryInfo"
    }

    private fun calculateTotalPrice(quantity: String): String {
        val parsedQuantity = quantity.toDoubleOrNull() ?: 0.0
        val pricePerKg = selectedItem.price.toDouble() ?: 0.0 // Convert price to Double or another numeric type

        return (pricePerKg * parsedQuantity).toString()
    }

    private fun updateSelectedItemAvailability(generatedItemId: String, orderedQuantity: Double) {
        val itemsRef = database.reference.child("seafood_items")
        val itemRef = itemsRef.child(generatedItemId)

        itemRef.child("availability").get().addOnSuccessListener { dataSnapshot ->
            val currentItemAvailability = dataSnapshot.getValue(String::class.java) ?: "0"
            val parsedAvailability = currentItemAvailability.toDoubleOrNull() ?: 0.0
            val newAvailability = parsedAvailability - orderedQuantity

            itemRef.child("availability").setValue(newAvailability.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    // Handle success
                } else {
                    // Handle failure
                }
            }
        }.addOnFailureListener {
            // Handle failure
        }
    }
}
