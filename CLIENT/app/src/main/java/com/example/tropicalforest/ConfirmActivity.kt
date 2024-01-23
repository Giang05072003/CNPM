package com.example.tropicalforest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tropicalforest.adapter.InvoiceDetailAdapter
import com.example.tropicalforest.databinding.ActivityConfirmBinding
import com.example.tropicalforest.databinding.ActivityInvoiceDetailBinding
import com.example.tropicalforest.model.CartData
import com.example.tropicalforest.model.CustomerData
import com.example.tropicalforest.model.InvoiceData
import com.example.tropicalforest.model.LineData
import com.example.tropicalforest.model.ProductData
import com.example.tropicalforest.shareData.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.backButton.setOnClickListener {
            val intent = Intent(this@ConfirmActivity, CartActivity::class.java)
            startActivity(intent)
        }

        loadCustomer(binding.customerTextView)

        binding.productInvoiceRecyclerView.layoutManager = GridLayoutManager(this, 2)
        loadProduct(binding.totalInvoiceTextView, binding.productInvoiceRecyclerView)
        binding.productInvoiceRecyclerView.setHasFixedSize(true)

        binding.buyButton.setOnClickListener {
            buy(binding.totalInvoiceTextView)
        }

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
                Toast.makeText(this@ConfirmActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadProduct(textView: TextView, recyclerView: RecyclerView) {
        var totalPrice = 0
        databaseReference = firebaseDatabase.reference.child("cart")
        databaseReference.orderByChild("user_id").equalTo(MyData.user_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val productIds = mutableListOf<String>()

                    for (cartSnapshot in snapshot.children) {
                        val cartData = cartSnapshot.getValue(CartData::class.java)
                        if (cartData != null) {
                            cartData.prod_id?.let { productIds.add(it) }
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
                                            totalPrice += productData.price!!.toInt()
                                        }
                                    }
                                }
                                textView.text = totalPrice.toString() + "đ"
                                recyclerView.adapter = InvoiceDetailAdapter(this@ConfirmActivity, myProductData)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@ConfirmActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ConfirmActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun buy(textView: TextView) {
        val user_id = MyData.user_id

        val cartReference = firebaseDatabase.reference.child("cart")
        cartReference.orderByChild("user_id").equalTo(user_id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(cartSnapshot: DataSnapshot) {
                if (cartSnapshot.exists()) {
                    val productIds = mutableListOf<String>()

                    for (cartSnapshot in cartSnapshot.children) {
                        val cartData = cartSnapshot.getValue(CartData::class.java)
                        if (cartData != null) {
                            cartData.prod_id?.let { productIds.add(it) }
                            cartReference.child(cartSnapshot.key!!).removeValue()
                        }
                    }

                    // Tạo hóa đơn
                    val invoiceReference = firebaseDatabase.reference.child("invoice")
                    val inv_id = invoiceReference.push().key
                    val totalPrice = textView.text.toString().substring(0, textView.text.toString().length - 1)
                    val invoiceData = InvoiceData(inv_id, user_id, totalPrice, "0")

                    if (inv_id != null) {
                        invoiceReference.child(inv_id).setValue(invoiceData)

                        // Thêm sản phẩm vào hóa đơn
                        val lineReference = firebaseDatabase.reference.child("line")
                        for (prodId in productIds) {
                            val key = lineReference.push().key
                            val lineData = LineData(inv_id, prodId)
                            if (key != null) {
                                lineReference.child(key).setValue(lineData)
                            }
                        }

                        val intent = Intent(this@ConfirmActivity, SuccessActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@ConfirmActivity, "Mua hàng thành công!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ConfirmActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}