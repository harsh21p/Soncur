package com.example.soncur.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.soncur.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.magic_fragment.*

class MagicFragment  : Fragment() {
    private var database = FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference? = null
    lateinit var auth: FirebaseAuth
    private var uName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.magic_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        myRef = database!!.reference

        progress_magic.visibility = View.VISIBLE

        myRef!!.child("myAuthUser").child(auth!!.uid.toString()).get().addOnSuccessListener {
            try {
                uName = it.child("Name").value.toString()
                setInfo()
            } catch (e: Exception) {
                Toast.makeText(activity, "Something went wrong ($e)", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun setInfo() {
        try {
            progress_magic.visibility = View.GONE
        }catch (e:Exception){

        }

    }
}