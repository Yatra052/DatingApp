package com.example.datingapp.Auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityLoginBinding
    val auth = FirebaseAuth.getInstance()
    private var verificationId: String? = null

    private lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dialog = AlertDialog.Builder(this).setView(R.layout.loading_layout)
            .setCancelable(false).create()


        binding.sendOTP.setOnClickListener{
            if(binding.userNumber.text!!.isEmpty())
            {
                binding.userNumber.error = "Please Enter Your Number"
            }

            else
            {
                sendOtp(binding.userNumber.text.toString())
            }
        }

        binding.verifyOTP.setOnClickListener{

            if(binding.userOTP.text!!.isEmpty())
            {
                binding.userOTP.error = "Please enter OTP"
            }
            else
            {
                verifyOtp(binding.userOTP.text.toString())
            }
        }
    }

    private fun verifyOtp(otp: String) {

        dialog.show()

            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)

            signInWithPhoneAuthCredential(credential)


    }

    private fun sendOtp(number: String) {

//        binding.sendOTP.showLoadingButton()
        dialog.show()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

//                binding.sendOTP.showNormalButton()
                signInWithPhoneAuthCredential(credential)

            }

            override fun onVerificationFailed(e: FirebaseException) {



            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {

               this@LoginActivity.verificationId =  verificationId
//                binding.sendOTP.showNormalButton()

                dialog.dismiss()

                binding.numberLayout.visibility = View.GONE
                binding.otpLayout.visibility = View.VISIBLE

            }
        }



        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91 $number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    binding.sendOTP.showNormalButton()

                    checkUserExist(binding.userNumber.text.toString())


//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
                }
                else {

                    dialog.dismiss()
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                      }

                }
            }

    private fun checkUserExist(number: String) {

        FirebaseDatabase.getInstance().getReference("users").child("+91"+number).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists())
                {
                    dialog.dismiss()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                else
                {
                    startActivity(Intent(this@LoginActivity, RegisterationActivity::class.java))
                    finish()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
                Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })



    }
}
