package com.example.datingapp.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.datingapp.Adapter.DatingAdapter
import com.example.datingapp.Model.UserModel
import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentDatingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class DatingFragment : Fragment() {

    private lateinit var binding: FragmentDatingBinding
    private lateinit var manager: CardStackLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentDatingBinding.inflate(layoutInflater)



        getData()

        return binding.root
    }

    private fun init() {

        manager = CardStackLayoutManager(requireContext(),object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {

                if (manager!!.topPosition == list!!.size)
                {
                    Toast.makeText(requireContext(), "No card found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })
        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)


    }

    companion object
    {
       var list: ArrayList<UserModel>?= null

    }

        private fun getData() {
            FirebaseDatabase.getInstance().getReference("users")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        Log.d("MYTAG", "onDataChange: ${snapshot.toString()}")


                        if (snapshot.exists()) {
                            list = arrayListOf()
                            for (data in snapshot.children) {
                                val model = data.getValue(UserModel::class.java)
                                list!!.add(model!!)
                            }

                            list!!.shuffle()
                            init()


                            binding.cardstackiew.layoutManager = manager
                            binding.cardstackiew.itemAnimator = DefaultItemAnimator()
                            binding.cardstackiew.adapter = DatingAdapter(requireContext(), list!!)
                        } else {

                            Toast.makeText(
                                requireContext(),
                                "Something went wrong ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }


                    override fun onCancelled(error: DatabaseError) {

                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                    }

                })


    }

}