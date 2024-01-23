package com.example.tropicalforest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tropicalforest.databinding.ActivityProductDetailBinding
import com.example.tropicalforest.model.CartData
import com.example.tropicalforest.model.CategoryData
import com.example.tropicalforest.model.ProductData
import com.example.tropicalforest.model.UserData
import com.example.tropicalforest.shareData.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        loadProductDetailCatName(binding.ProductDetailCatNameTextView)

        binding.backButton.setOnClickListener {
            val intent = Intent(this@ProductDetailActivity, MainActivity::class.java)
            startActivity(intent)
        }

        loadProcuctDetail(binding.ProductDetailTextView, binding.ProductDetailImageView, binding.ProductDetailNameTextView, binding.ProductDetailDescribeTextView, binding.ProductDetailPriceTextView)

        binding.addCartButton.setOnClickListener {
            addCart()
        }

    }

    private fun loadProcuctDetail(name: TextView, image: ImageView, prodName: TextView, describe: TextView, price: TextView) {
        databaseReference = firebaseDatabase.reference.child("product")
        databaseReference.orderByChild("prod_id").equalTo(MyData.prod_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val productData = productSnapshot.getValue(ProductData::class.java)
                        if (productData != null) {
                            name.text = productData.prod_name
                            Glide.with(this@ProductDetailActivity).load(productData.prod_image).into(image)
                            prodName.text = productData.prod_name
                            describe.text = productData.prod_describe
                            price.text = productData.price.toString() + "đ"
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProductDetailActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadProductDetailCatName(catName: TextView) {
        databaseReference = firebaseDatabase.reference.child("category")
        databaseReference.orderByChild("cat_id").equalTo(MyData.cat_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (categorySnapshot in snapshot.children) {
                        val categoryData = categorySnapshot.getValue(CategoryData::class.java)
                        if (categoryData != null) {
                            catName.text = categoryData.cat_name.toString()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProductDetailActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addCart() {
        databaseReference = firebaseDatabase.reference.child("cart")
        databaseReference.orderByChild("user_id").equalTo(MyData.user_id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var productExists = false
                    if (snapshot.exists()) {
                        for (cartSnapshot in snapshot.children) {
                            val cartData = cartSnapshot.getValue(CartData::class.java)
                            if (cartData != null && cartData.prod_id == MyData.prod_id) {
                                productExists = true
                                break
                            }
                        }
                    }

                    if (productExists) {
                        Toast.makeText(this@ProductDetailActivity, "Đã tồn tại trong giỏ hàng!", Toast.LENGTH_SHORT).show()
                    } else {
                        val key = databaseReference.push().key
                        val cartData = CartData(MyData.user_id, MyData.prod_id)
                        databaseReference.child(key!!).setValue(cartData)
                        Toast.makeText(this@ProductDetailActivity, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ProductDetailActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}