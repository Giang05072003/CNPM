package com.example.tropicalforest

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tropicalforest.adapter.CartAdapter
import com.example.tropicalforest.databinding.ActivityCartBinding
import com.example.tropicalforest.model.CartData
import com.example.tropicalforest.model.ProductData
import com.example.tropicalforest.shareData.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var prodDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.backButton.setOnClickListener {
            val intent = Intent(this@CartActivity, MainActivity::class.java)
            startActivity(intent)
        }

        loadData(binding.cartTotalTextView, binding.cartRecyclerView)
        binding.cartRecyclerView.setHasFixedSize(true)

        binding.deleteButton.setOnClickListener {
            deleteData(binding.cartTotalTextView, binding.cartRecyclerView)
        }

        binding.buyButton.setOnClickListener {
            if (binding.cartTotalTextView.text == "0đ") {
                Toast.makeText(this@CartActivity, "Bạn chưa có sản phẩm nào trong giỏ hàng!", Toast.LENGTH_SHORT).show()
            }else{
                checkCustomer()
            }
        }

    }

    private fun loadData(textView: TextView, recyclerView: RecyclerView) {
        val myProductData = ArrayList<ProductData>()
        var totalPrice = 0
        databaseReference = firebaseDatabase.reference.child("cart")
        databaseReference.orderByChild("user_id").equalTo(MyData.user_id).addListenerForSingleValueEvent(object :
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

                    prodDatabaseReference = firebaseDatabase.reference.child("product")
                    prodDatabaseReference.orderByChild("prod_id").addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        @SuppressLint("SetTextI18n")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (productSnapshot in snapshot.children) {
                                    val productData = productSnapshot.getValue(ProductData::class.java)
                                    if (productData != null && productIds.contains(productData.prod_id)) {
                                        myProductData.add(productData)
                                        totalPrice += productData.price!!.toInt()
                                    }
                                }
                                textView.text = totalPrice.toString() + "đ"
                                recyclerView.adapter = CartAdapter(this@CartActivity, myProductData)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@CartActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteData(textView: TextView, recyclerView: RecyclerView) {
        val myProductData = ArrayList<ProductData>()
        databaseReference = firebaseDatabase.reference.child("cart")
        val query = databaseReference.orderByChild("user_id").equalTo(MyData.user_id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (cartSnapshot in snapshot.children) {
                        val key = cartSnapshot.key
                        if (key != null) {
                            databaseReference.child(key).removeValue()
                        }
                    }

                    textView.text = "0đ"
                    recyclerView.adapter = CartAdapter(this@CartActivity, myProductData)
                }
                Toast.makeText(this@CartActivity, "Loại bỏ sản phẩm thành công!", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkCustomer() {
        val customerReference = firebaseDatabase.reference.child("customer")
        customerReference.orderByChild("user_id").equalTo(MyData.user_id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(customerSnapshot: DataSnapshot) {
                if (customerSnapshot.exists()) {
                    val intent = Intent(this@CartActivity, ConfirmActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@CartActivity, "Vui lòng cập nhật thông tin cá nhân!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}