package com.example.tropicalforest

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tropicalforest.adapter.CategoryFilterAdapter
import com.example.tropicalforest.adapter.InvoiceDetailAdapter
import com.example.tropicalforest.databinding.ActivityInvoiceDetailBinding
import com.example.tropicalforest.model.CartData
import com.example.tropicalforest.model.CustomerData
import com.example.tropicalforest.model.InvoiceData
import com.example.tropicalforest.model.LineData
import com.example.tropicalforest.model.ProductData
import com.example.tropicalforest.shareData.MyData
import com.example.tropicalforest.shareData.MyData.inv_id
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InvoiceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInvoiceDetailBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInvoiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.backButton.setOnClickListener {
            val intent = Intent(this@InvoiceDetailActivity, InvoiceActivity::class.java)
            startActivity(intent)
        }

        loadCustomer(binding.customerTextView)

        binding.productInvoiceRecyclerView.layoutManager = GridLayoutManager(this, 2)
        loadProduct(binding.productInvoiceRecyclerView)
        binding.productInvoiceRecyclerView.setHasFixedSize(true)

        loadTotalPrice(binding.totalInvoiceTextView)

        loadStatus(binding.statusCard, binding.statusTextView)

    }

    private fun loadCustomer(textView: TextView) {
        databaseReference = firebaseDatabase.reference.child("customer")
        databaseReference.orderByChild("user_id").equalTo(MyData.user_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (customerSnapshot in snapshot.children) {
                        val customerData = customerSnapshot.getValue(CustomerData::class.java)
                        if (customerData != null) {
                            textView.text = customerData.cus_name.toString() + "\n" + customerData.cus_phone.toString() + "\n" + customerData.cus_address.toString()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InvoiceDetailActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadProduct(recyclerView: RecyclerView) {
        databaseReference = firebaseDatabase.reference.child("line")
        databaseReference.orderByChild("inv_id").equalTo(MyData.inv_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val productIds = mutableListOf<String>()

                    for (lineSnapshot in snapshot.children) {
                        val lineData = lineSnapshot.getValue(LineData::class.java)
                        if (lineData != null) {
                            lineData.prod_id?.let { productIds.add(it) }
                        }
                    }

                    val myProductData = ArrayList<ProductData>()
                    databaseReference = firebaseDatabase.reference.child("product")
                    databaseReference.orderByChild("prod_id").addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (prodId in productIds) {
                                    for (productSnapshot in snapshot.children) {
                                        val productData = productSnapshot.getValue(ProductData::class.java)
                                        if (productData != null && productData.prod_id == prodId) {
                                            myProductData.add(productData)
                                        }
                                    }
                                }
                                recyclerView.adapter = InvoiceDetailAdapter(this@InvoiceDetailActivity, myProductData)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@InvoiceDetailActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InvoiceDetailActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadTotalPrice(textView: TextView) {
        databaseReference = firebaseDatabase.reference.child("invoice")
        databaseReference.orderByChild("inv_id").equalTo(MyData.inv_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (invoiceSnapshot in snapshot.children) {
                        val invoiceData = invoiceSnapshot.getValue(InvoiceData::class.java)
                        if (invoiceData != null) {
                            textView.text = invoiceData.totalPrice + "đ"
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InvoiceDetailActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadStatus(cardView: CardView, textView: TextView) {
        databaseReference = firebaseDatabase.reference.child("invoice")
        databaseReference.orderByChild("inv_id").equalTo(MyData.inv_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (invoiceSnapshot in snapshot.children) {
                        val invoiceData = invoiceSnapshot.getValue(InvoiceData::class.java)
                        if (invoiceData != null && invoiceData.inv_status == "1") {
                            val colorGreenId = R.color.green
                            val color = ContextCompat.getColor(this@InvoiceDetailActivity, colorGreenId)
                            ViewCompat.setBackgroundTintList(cardView, ColorStateList.valueOf(color))
                            textView.text = "Đã giao hàng"
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InvoiceDetailActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}