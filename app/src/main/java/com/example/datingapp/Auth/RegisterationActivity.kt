package com.example.datingapp.Auth

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.datingapp.MainActivity
import com.example.datingapp.Model.UserModel
import com.example.datingapp.R
import com.example.datingapp.Utils.Config
import com.example.datingapp.databinding.ActivityRegisterationBinding
import com.google.android.gms.common.internal.HideFirstParty
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

class RegisterationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterationBinding
    private var imageUri: Uri? = null
    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){

        imageUri = it

        binding.userImage.setImageURI(imageUri)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.userImage.setOnClickListener{

            selectImage.launch("image/*")
        }

        binding.saveData.setOnClickListener{
            validateData()

        }

    }

    private fun validateData() {

        if(binding.userName.text.toString().isEmpty() ||
            binding.userEmail.text.toString().isEmpty()||
                    binding.usercity.text.toString().isEmpty() || imageUri == null)
                    {

                        Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()


                    }
        else if (!binding.termsAndCondition.isChecked){

            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show()

        }
        else
        {
            uploadImage()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadImage() {

   Config.showDialog(this)
       val storageRef =  FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile.jpg")
            storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storageData(it)
                }.addOnFailureListener{
               Config.HideDialog()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }

    }

    private fun storageData(imageUrl: Uri?) {



        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

              val token = task.result




            val data = UserModel(
                name = binding.userName.text.toString(),
                image = imageUrl.toString(),
                email = binding.userEmail.text.toString(),
                city =  binding.usercity.text.toString(),
                fcmtoken = token,
                number = FirebaseAuth.getInstance().currentUser!!.phoneNumber

            )

            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).setValue(data).addOnCompleteListener {


                    if (it.isSuccessful) {

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        Toast.makeText(this, "User Register Successfull", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()

                    }


                }

                })
        }
    }



