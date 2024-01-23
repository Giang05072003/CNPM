package com.example.tropicalforest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.tropicalforest.databinding.ActivityChangeInfoBinding
import com.example.tropicalforest.model.CustomerData
import com.example.tropicalforest.shareData.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChangeInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeInfoBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.backButton.setOnClickListener {
            val intent = Intent(this@ChangeInfoActivity, MainActivity::class.java)
            startActivity(intent)
        }

        getInfo(binding.changeInfoNameEditText, binding.changeInfoPhoneEditText, binding.changeInfoAddressEditText)

        binding.changeInfoButton.setOnClickListener {

            if (binding.changeInfoNameEditText.text.toString().isNotEmpty() && binding.changeInfoPhoneEditText.text.toString().isNotEmpty() && binding.changeInfoAddressEditText.text.toString().isNotEmpty()) {
                if (binding.changeInfoPhoneEditText.text.toString().length != 10) {
                    Toast.makeText(this@ChangeInfoActivity, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show()
                }else{
                    MyData.user_id?.let { it1 -> changeInfo(it1, binding.changeInfoNameEditText.text.toString(), binding.changeInfoPhoneEditText.text.toString(), binding.changeInfoAddressEditText.text.toString()) }
                    val intent = Intent(this@ChangeInfoActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }else{
                Toast.makeText(this@ChangeInfoActivity, "Không được để trống!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun changeInfo(userId: String, cusName: String, cusPhone: String, cusAddress: String) {

        databaseReference = firebaseDatabase.reference.child("customer")
        databaseReference.orderByChild("user_id").equalTo(userId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val customerData = CustomerData(userId, cusName, cusPhone, cusAddress)
                databaseReference.child(userId).setValue(customerData)
                Toast.makeText(this@ChangeInfoActivity, "Thay đổi thông tin thành công!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ChangeInfoActivity, MainActivity::class.java))
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChangeInfoActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getInfo(cusName: EditText, cusPhone: EditText, cusAddress: EditText) {
        databaseReference = firebaseDatabase.reference.child("customer")
        databaseReference.orderByChild("user_id").equalTo(MyData.user_id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (customerSnapshot in snapshot.children) {
                        val customerData = customerSnapshot.getValue(CustomerData::class.java)
                        if (customerData != null) {
                            cusName.setText(customerData.cus_name)
                            cusPhone.setText(customerData.cus_phone)
                            cusAddress.setText(customerData.cus_address)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChangeInfoActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}