package com.example.tropicalforest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tropicalforest.databinding.ActivityForgotPasswordBinding
import com.example.tropicalforest.model.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")

        binding.forgotPasswordButton.setOnClickListener {
            val forgotPasswordAccount = binding.forgotPasswordEditText.text.toString()

            if (forgotPasswordAccount.isNotEmpty()) {
                forgotPassword(forgotPasswordAccount)
            }else{
                Toast.makeText(this@ForgotPasswordActivity, "Không được để trống!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
            finish()
        }

    }

    private fun forgotPassword (user_id: String) {
        databaseReference.orderByChild("user_id").equalTo(user_id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val password = user_id
                    val userData = UserData(user_id, password)
                    databaseReference.child(user_id).setValue(userData)
                    Toast.makeText(this@ForgotPasswordActivity, "Mật khẩu đã được đổi thành tên tài khoản!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@ForgotPasswordActivity, "Người dùng không tồn tại!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ForgotPasswordActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}