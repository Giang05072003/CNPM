package com.example.tropicalforest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tropicalforest.model.CustomerData
import com.example.tropicalforest.model.UserData
import com.example.tropicalforest.shareData.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InfoFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_info, container, false)

        firebaseDatabase = FirebaseDatabase.getInstance()

        val userTextView = view.findViewById<TextView>(R.id.userTextView)
        userTextView.text = MyData.user_id

        val cartButton = view.findViewById<Button>(R.id.cartButton)
        cartButton.setOnClickListener {

            startActivity(Intent(activity, CartActivity::class.java))

        }

        val invoiceButton = view.findViewById<Button>(R.id.invoiceButton)
        invoiceButton.setOnClickListener {

            startActivity(Intent(activity, InvoiceActivity::class.java))

        }


        val changeInfoButton = view.findViewById<Button>(R.id.changeInfoButton)
        changeInfoButton.setOnClickListener {

            startActivity(Intent(activity, ChangeInfoActivity::class.java))

        }

        val introduceButton = view.findViewById<Button>(R.id.introduceButton)
        introduceButton.setOnClickListener {

            startActivity(Intent(activity, IntroduceActivity::class.java))

        }

        val changePasswordButton = view.findViewById<Button>(R.id.changePasswordButton)
        changePasswordButton.setOnClickListener {

            startActivity(Intent(activity, ChangePasswordActivity::class.java))

        }

        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {

            startActivity(Intent(activity, LoginActivity::class.java))

        }

        return view
    }

}