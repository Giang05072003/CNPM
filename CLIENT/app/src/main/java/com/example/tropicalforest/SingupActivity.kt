package com.example.tropicalforest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tropicalforest.databinding.ActivitySingupBinding
import com.example.tropicalforest.model.UserData
import com.example.tropicalforest.shareData.MyData.user_id
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SingupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")

        binding.signupButton.setOnClickListener {
            val singupUserId = binding.signupAccountEditText.text.toString()
            val singupPassword = binding.signupPasswordEditText.text.toString()
            val singupRePassword = binding.signupRePasswordEditText.text.toString()

            if (singupUserId.isNotEmpty() && singupPassword.isNotEmpty() && singupRePassword.isNotEmpty()) {
                if (singupPassword != singupRePassword) {
                    Toast.makeText(this@SingupActivity, "Vui lòng kiểm tra lại mật khẩu!", Toast.LENGTH_SHORT).show()
                }else{
                    singupUser(singupUserId, singupPassword)
                }
            }else{
                Toast.makeText(this@SingupActivity, "Không được để trống!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@SingupActivity, LoginActivity::class.java))
            finish()
        }

    }

    private fun singupUser (userId: String, password: String) {
        databaseReference.orderByChild("user_id").equalTo(userId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    val userData = UserData(userId, password)
                    databaseReference.child(userId).setValue(userData)
                    Toast.makeText(this@SingupActivity, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SingupActivity, LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@SingupActivity, "Người dùng đã tồn tại!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SingupActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}