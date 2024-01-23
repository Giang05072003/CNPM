package com.example.tropicalforest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tropicalforest.adapter.CategoryFilterAdapter
import com.example.tropicalforest.adapter.InvoiceAdapter
import com.example.tropicalforest.databinding.ActivityCategoryFilterBinding
import com.example.tropicalforest.databinding.ActivityInvoiceBinding
import com.example.tropicalforest.model.InvoiceData
import com.example.tropicalforest.model.ProductData
import com.example.tropicalforest.shareData.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InvoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInvoiceBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.backButton.setOnClickListener {

            val intent = Intent(this@InvoiceActivity, MainActivity::class.java)
            startActivity(intent)

        }

        loadInvoice(binding.invoiceRecyclerView)
        binding.invoiceRecyclerView.setHasFixedSize(true)

    }

    private fun loadInvoice(recyclerView: RecyclerView) {
        val myInvoiceData = ArrayList<InvoiceData>()
        databaseReference = firebaseDatabase.reference.child("invoice")
        databaseReference.orderByChild("user_id").equalTo(MyData.user_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (invoiceSnapshot in snapshot.children) {
                        val invoiceData = invoiceSnapshot.getValue(InvoiceData::class.java)
                        if (invoiceData != null) {
                            myInvoiceData.add(invoiceData)
                        }
                    }
                    recyclerView.adapter = InvoiceAdapter(this@InvoiceActivity, myInvoiceData)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InvoiceActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}