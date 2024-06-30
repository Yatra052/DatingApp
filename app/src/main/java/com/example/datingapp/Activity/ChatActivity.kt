package com.example.datingapp.Activity

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.datingapp.Adapter.MessageAdapter
import com.example.datingapp.Model.MessageModel
import com.example.datingapp.Model.UserModel
import com.example.datingapp.Notification.NotificationData
import com.example.datingapp.Notification.PushNotification
import com.example.datingapp.Notification.api.ApiUtilities
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import java.util.Locale


class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        getData(intent.getStringExtra("chat_id"))
        verifyChatId()


        binding.send.setOnClickListener{

            if(binding.yourmessage.text!!.isEmpty())

            {
                Toast.makeText(this, "Please Enter Your Message", Toast.LENGTH_SHORT).show()
            }
            else
            {

                storeData(binding.yourmessage.text.toString())
            }
        }

    }

    private var senderId: String ?= null
    private var chatId: String ?= null
    private var receiverId: String? = null



    private fun verifyChatId() {

        receiverId =  intent.getStringExtra("userId")
        senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

        chatId = senderId + receiverId
        val reverseChatId = receiverId + senderId


        val reference =   FirebaseDatabase.getInstance().getReference("chats")

        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.hasChild(chatId!!)){
                    getData(chatId)
                }
                else if(snapshot.hasChild(reverseChatId))
                {
                    chatId = reverseChatId
                    getData(chatId)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        })




    }


    private fun getData(chatId: String?) {


        FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = arrayListOf<MessageModel>()

                    for (show  in snapshot.children)
                    {
                        list.add(show.getValue(MessageModel::class.java)!!)

                    }

                    binding.recyclerview2.adapter = MessageAdapter(this@ChatActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()

                }

            })
    }


    private fun storeData(msg: String) {


        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String =  SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())


        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["senderId"] = senderId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)

        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener{
            if (it.isSuccessful)
            {
                binding.yourmessage.text = null


                sendNotification(msg)

                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun sendNotification(msg: String) {


        FirebaseDatabase.getInstance().getReference("users").child(receiverId!!).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        val data = snapshot.getValue(UserModel::class.java)


                        if(snapshot.exists())
                        {
                            val data = snapshot.getValue(UserModel::class.java)

                            val notificationData = PushNotification(NotificationData("New Message", msg),
                                data?.fcmtoken)

                            ApiUtilities.getInstance()?.sendNotification(

                                notificationData
                            )?.enqueue(object: Callback<PushNotification>{
                                override fun onResponse(
                                    call: Call<PushNotification>?,
                                    response: Response<PushNotification>?
                                ) {


                                    Toast.makeText(this@ChatActivity, "Notification Send", Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(
                                    call: Call<PushNotification>?,
                                    t: Throwable?
                                ) {
                                    Toast.makeText(this@ChatActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                                }

                            })




                        }
                    }


                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            }
        )




    }
}
