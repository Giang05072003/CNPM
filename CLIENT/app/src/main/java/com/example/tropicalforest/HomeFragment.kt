package com.example.tropicalforest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tropicalforest.adapter.CategoryAdapter
import com.example.tropicalforest.adapter.ProductAdapter
import com.example.tropicalforest.model.CategoryData
import com.example.tropicalforest.model.ProductData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        firebaseDatabase = FirebaseDatabase.getInstance()

        val categoryRecyclerView = view.findViewById<RecyclerView>(R.id.category_recyclerView)
        loadCategory(categoryRecyclerView)
        categoryRecyclerView.setHasFixedSize(true)

        val productRecyclerView = view.findViewById<RecyclerView>(R.id.product_recyclerView)
        productRecyclerView.layoutManager = GridLayoutManager(context, 2)
        loadProduct(productRecyclerView)
        productRecyclerView.setHasFixedSize(true)


        return view
    }

    private fun loadCategory(recyclerView: RecyclerView) {
        val myCategoryData = ArrayList<CategoryData>()
        databaseReference = firebaseDatabase.reference.child("category")
        databaseReference.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (categorySnapshot in snapshot.children) {
                        val categoryData = categorySnapshot.getValue(CategoryData::class.java)
                        if (categoryData != null) {
                            myCategoryData.add(categoryData)
                        }
                    }
                    recyclerView.adapter = CategoryAdapter(this@HomeFragment, myCategoryData)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun  loadProduct(recyclerView: RecyclerView) {
        val myProductData = ArrayList<ProductData>()
        databaseReference = firebaseDatabase.reference.child("product")
        databaseReference.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val productData = productSnapshot.getValue(ProductData::class.java)
                        if (productData != null) {
                            myProductData.add(productData)
                        }
                    }
                    recyclerView.adapter = ProductAdapter(this@HomeFragment, myProductData)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}