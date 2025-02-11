package com.example.datingapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.Activity.ChatActivity
import com.example.datingapp.Model.UserModel
import com.example.datingapp.databinding.MessageItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageUserAdapter(val context: Context, val list: ArrayList<String>, val ChatKey: List<String>): RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {

    inner class MessageUserViewHolder(val binding: MessageItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {

        return MessageUserViewHolder(MessageItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
//
//       Glide.with(context).load(list[position].image).into(holder.binding.userImage)
//        holder.binding.userName.text = list[position].name


        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position]).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists())
                    {
                        val data = snapshot.getValue(UserModel::class.java)

                        Glide.with(context).load(data!!.image).into(holder.binding.userImage)
                        holder.binding.userName.text =  data.name
//                        Log.d("MY", "onDataChange: ${data}")

                    }



                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()

                }

            })

        holder.itemView.setOnClickListener{
            val inte = Intent(context, ChatActivity::class.java)
            inte.putExtra("chat_id", ChatKey[position])
            inte.putExtra("userId", list[position])
            context.startActivity(inte)





        }
    }

    override fun getItemCount(): Int {
        return list.size

    }

}