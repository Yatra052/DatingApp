package com.example.datingapp.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.datingapp.Activity.EditProfileActivity
import com.example.datingapp.Auth.LoginActivity
import com.example.datingapp.Model.UserModel
import com.example.datingapp.R
import com.example.datingapp.Utils.Config
import com.example.datingapp.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {
   private lateinit var binding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


     Config.showDialog(requireContext())


     binding = FragmentProfileBinding.inflate(layoutInflater)



        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .get().addOnSuccessListener {
                if(it.exists())
                {
                    val data = it.getValue(UserModel::class.java)

                    binding.name.setText(data!!.name.toString())
                    binding.city.setText(data.city.toString())
                    binding.email.setText(data.email.toString())
                    binding.number.setText(data.number.toString())

                    Glide.with(requireContext()).load(data.image).placeholder(R.drawable.woman).into(binding.userImage)

                    Config.HideDialog()




                }
            }


        binding.logout.setOnClickListener{

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }


        binding.editprofile.setOnClickListener{
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }


        return binding.root

    }


}