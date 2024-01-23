package com.example.tropicalforest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tropicalforest.databinding.ActivityChangePasswordBinding
import com.example.tropicalforest.model.UserData
import com.example.tropicalforest.shareData.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.backButton.setOnClickListener {

            val intent = Intent(this@ChangePasswordActivity, MainActivity::class.java)
            startActivity(intent)

        }

        binding.changePasswordButton.setOnClickListener {

            val changePassword = binding.changePasswordEditText.text.toString()
            val changeRePassword = binding.changeRePasswordEditText.text.toString()

            if (changePassword.isNotEmpty() && changeRePassword.isNotEmpty()) {
                if (changePassword == changeRePassword) {
                    changePassword(changePassword)
                }else{
                    Toast.makeText(this@ChangePasswordActivity, "Mật khẩu đã nhập không trùng khớp!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@ChangePasswordActivity, "Không được để trống!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun changePassword(passWord: String) {
        databaseReference = firebaseDatabase.reference.child("user")
        databaseReference.orderByChild("user_id").equalTo(MyData.user_id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)
                        if (userData != null) {
                            userData.password = passWord
                            MyData.user_id?.let { databaseReference.child(it).setValue(userData) }
                            Toast.makeText(this@ChangePasswordActivity, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ChangePasswordActivity, MainActivity::class.java)
                            startActivity(intent)
                            return
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChangePasswordActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}