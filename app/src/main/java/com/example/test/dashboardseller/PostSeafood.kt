package com.example.test.dashboardseller

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.SeafoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PostSeafood : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var selectedImageView: ImageView
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_seafood)

        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        selectedImageView = findViewById(R.id.selectedImageView)
        selectedImageUri = Uri.EMPTY

        val backButton: ImageButton = findViewById(R.id.backB)
        val addPhotoButton: Button = findViewById(R.id.buttonAddPhoto)
        val cancelButton: Button = findViewById(R.id.buttonCancel)
        val submitButton: Button = findViewById(R.id.buttonSubmit)

        backButton.setOnClickListener {
            navigateToDashboard()
        }

        addPhotoButton.setOnClickListener {
            openGallery()
        }

        cancelButton.setOnClickListener {
            Toast.makeText(this, "Post Seafood unsuccessful", Toast.LENGTH_SHORT).show()
            navigateToDashboard()
        }

        submitButton.setOnClickListener {
            handleFormSubmission()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data ?: Uri.EMPTY
                if (selectedImageUri != Uri.EMPTY) {
                    selectedImageView.setImageURI(selectedImageUri)
                    selectedImageView.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

    private fun handleFormSubmission() {
        val vendorName = findViewById<EditText>(R.id.editTextVendorName).text.toString()
        val location = findViewById<EditText>(R.id.editTextLocation).text.toString()
        val price = findViewById<EditText>(R.id.editTextPrice).text.toString().toDoubleOrNull()
        val deliveryInfo = findViewById<EditText>(R.id.editTextDeliveryInfo).text.toString()
        val availability = findViewById<EditText>(R.id.editTextAvailability).text.toString()
        val phoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber).text.toString().toLongOrNull()
        val fishType = findViewById<Spinner>(R.id.spinnerFishTypes).selectedItem.toString()
        val source = findViewById<Spinner>(R.id.spinnerSource).selectedItem.toString()
        val payment = findViewById<Spinner>(R.id.spinnerPaymentMethods).selectedItem.toString()

        if (vendorName.isNotEmpty() && location.isNotEmpty() && price != null && selectedImageUri != Uri.EMPTY) {
            val photoRef = storageReference.child("images/${selectedImageUri.lastPathSegment}")
            val uploadTask = photoRef.putFile(selectedImageUri)

            uploadTask.addOnSuccessListener { uploadTaskSnapshot ->
                photoRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val seafoodItemId = database.child("seafood_items").push().key
                        var generatedItemId = seafoodItemId ?: ""

                        val seafoodItem = SeafoodItem(
                            vendorName = vendorName,
                            location = location,
                            price = price,
                            imageUrl = downloadUri.toString(),
                            deliveryInfo = deliveryInfo,
                            availability = availability,
                            phoneNumber = phoneNumber ?: 0L,
                            fishType = fishType,
                            source = source,
                            payment = payment,
                            sellerId = userId,
                            generatedItemId = generatedItemId
                        )

                        val updates = HashMap<String, Any>()
                        updates["/seafood_items/$generatedItemId"] = seafoodItem

                        database.updateChildren(updates)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                                showPostSuccessAlert()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, MainActivitySeller::class.java)
        startActivity(intent)
        finish()
    }

    private fun showPostSuccessAlert() {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage("Seafood posted successfully!")
            .setPositiveButton("OK") { _, _ ->
                navigateToDashboard()
            }
            .setCancelable(false)
            .create()
        alertDialog.show()
    }
}
