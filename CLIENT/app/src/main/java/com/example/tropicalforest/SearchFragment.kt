package com.example.tropicalforest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tropicalforest.adapter.SearchAdapter
import com.example.tropicalforest.model.ProductData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)

        firebaseDatabase = FirebaseDatabase.getInstance()

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val searchRecyclerView = view.findViewById<RecyclerView>(R.id.searchRecyclerView)
        searchRecyclerView.layoutManager = GridLayoutManager(context, 2)
        searchList("", searchRecyclerView)
        searchRecyclerView.setHasFixedSize(true)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText, searchRecyclerView)
                return true
            }
        })

        return view
    }

    private fun searchList(text: String, recyclerView: RecyclerView) {
        val myProductData = ArrayList<ProductData>()
        databaseReference = firebaseDatabase.reference.child("product")
        databaseReference.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val productData = productSnapshot.getValue(ProductData::class.java)
                        if (productData != null) {
                            if (productData.prod_name?.contains(text) == true) {
                                myProductData.add(productData)
                            }
                        }
                    }
                    recyclerView.adapter = SearchAdapter(this@SearchFragment, myProductData)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}