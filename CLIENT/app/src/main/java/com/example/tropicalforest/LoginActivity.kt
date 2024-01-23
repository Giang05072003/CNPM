package com.example.tropicalforest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tropicalforest.databinding.ActivityLoginBinding
import com.example.tropicalforest.model.UserData
import com.example.tropicalforest.shareData.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")

        binding.loginButton.setOnClickListener {
            val loginUserId = binding.loginAccountEditText.text.toString()
            val loginPassword = binding.loginPasswordEditText.text.toString()

            if (loginUserId.isNotEmpty() && loginPassword.isNotEmpty()) {
                loginUser(loginUserId, loginPassword)
            }else{
                Toast.makeText(this@LoginActivity, "Không được để trống!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.forgotPasswordRedirect.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            finish()
        }

        binding.singupRedirect.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SingupActivity::class.java))
            finish()
        }

    }

    private fun loginUser (userId: String, password: String) {
        databaseReference.orderByChild("user_id").equalTo(userId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData != null && userData.password == password) {
                            Toast.makeText(this@LoginActivity, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            MyData.user_id = userData.user_id
                            startActivity(intent)
                            finish()
                            return
                        }
                    }
                }
                Toast.makeText(this@LoginActivity, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}