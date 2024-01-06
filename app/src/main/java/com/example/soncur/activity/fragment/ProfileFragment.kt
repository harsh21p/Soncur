package com.example.soncur.activity.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.soncur.R
import com.example.soncur.activity.Login
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment: Fragment() {
    private var database= FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference? = null
    private var uName:String?=null
    private var uEmail:String?=null
    private var uContact:String?=null
    private var GALLERY_REQUEST = 100
    private var image:Uri?=null
    private val auth = FirebaseAuth.getInstance()
    private var sRef = FirebaseStorage.getInstance().reference.child(
        "users/ ${auth.uid}/profile.jpg")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progress_profile2.visibility = View.GONE
        try {
            progress_profile2.visibility = View.VISIBLE
            sRef.downloadUrl.addOnSuccessListener {
                if(it!=null) {
                    try {
                        progress_profile2.visibility = View.GONE
                        Picasso.get().load(it).into(profile_pic)
                    }catch (e:Exception){
                        progress_profile2.visibility = View.GONE
                    }
                }
            }.addOnFailureListener(OnFailureListener {
                progress_profile2.visibility = View.GONE
            })
        }catch (e:Exception){
            progress_profile2.visibility = View.GONE
        }

        logout_button.setOnClickListener(View.OnClickListener {
            popup_logout?.visibility = View.VISIBLE
        })

        yes_button.setOnClickListener(View.OnClickListener {
            popup_logout?.visibility = View.GONE
            auth.signOut()
            val iLoginScreen = Intent(activity, Login::class.java)
            startActivity(iLoginScreen)
            requireActivity().finish()
        })

        no_button.setOnClickListener(View.OnClickListener {
            popup_logout?.visibility = View.GONE
        })

        myRef = database!!.reference
        myRef!!.child("myAuthUser").child(auth!!.uid.toString()).get().addOnSuccessListener {
                    try {
                        uName = it.child("Name").value.toString()
                        uEmail = it.child("Email").value.toString()
                        uContact = it.child("Contact").value.toString()
                        setInfo()
                }catch (e:Exception){
                    try {
                        Toast.makeText(activity, "Something went wrong ($e)", Toast.LENGTH_SHORT).show()
                    }catch (e:Exception){

                    }
                        progress_profile2.visibility = View.GONE
                }
        }.addOnFailureListener{
            try {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
            }
            Log.e("firebase", "Error getting data", it)
            progress_profile2.visibility = View.GONE

        }

        edit_button.setOnClickListener(View.OnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            progress_profile2.visibility = View.VISIBLE
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        })

    }

    private fun setInfo() {
        user_name.text = "$uName"
    }

    private fun uploadImage() {
        if (image != null) {
            sRef.putFile(image!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            try {
                                progress_profile2.visibility = View.GONE
                                Toast.makeText(requireActivity(),"Uploaded",Toast.LENGTH_SHORT).show()
                            }catch (e:Exception){

                            }
                        }
                }.addOnFailureListener {
                    progress_profile2.visibility = View.GONE
                }
        } else {
            progress_profile2.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            if(resultCode == RESULT_OK){
                image = data!!.data
                Picasso.get().load(image).into(profile_pic)
                uploadImage()
            }else{
                progress_profile2.visibility = View.GONE
            }
        }
    }
}