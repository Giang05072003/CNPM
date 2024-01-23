package com.example.tropicalforest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tropicalforest.adapter.CategoryFilterAdapter
import com.example.tropicalforest.databinding.ActivityCategoryFilterBinding
import com.example.tropicalforest.model.CategoryData
import com.example.tropicalforest.model.ProductData
import com.example.tropicalforest.shareData.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryFilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryFilterBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        loadCategoryFilterName(binding.categoryFilterTextView)

        binding.backButton.setOnClickListener {
            val intent = Intent(this@CategoryFilterActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.categoryFilterRecyclerView.layoutManager = GridLayoutManager(this, 2)
        loadCategoryFilter(binding.categoryFilterRecyclerView)
        binding.categoryFilterRecyclerView.setHasFixedSize(true)

    }

    private fun loadCategoryFilter(recyclerView: RecyclerView) {
        val myCategoryFilterData = ArrayList<ProductData>()
        databaseReference = firebaseDatabase.reference.child("product")
        databaseReference.orderByChild("cat_id").equalTo(MyData.cat_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val productData = productSnapshot.getValue(ProductData::class.java)
                        if (productData != null) {
                            myCategoryFilterData.add(productData)
                        }
                    }
                    recyclerView.adapter = CategoryFilterAdapter(this@CategoryFilterActivity, myCategoryFilterData)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CategoryFilterActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadCategoryFilterName(catName: TextView) {
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
                Toast.makeText(this@CategoryFilterActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}