package com.example.soncur.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.soncur.R
import com.example.soncur.activity.fragment.AddFragment
import com.example.soncur.activity.fragment.MainFragment
import com.example.soncur.activity.fragment.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dashboard.*

class Dashboard : AppCompatActivity() {
    private var auth: FirebaseAuth?=null
    private var database= FirebaseDatabase.getInstance()
    private var email:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        auth = FirebaseAuth.getInstance()
        val fragmentHome = MainFragment()
        val fragmentProfile = ProfileFragment()
        database!!.reference.child("myAuthUser").child(auth!!.uid.toString()).get().addOnSuccessListener {
          try {
              email = it.child("Email").value.toString()
              if(email == "admin@soncur.com"){
                  showFragment(AddFragment())
              }else{
                  showFragment(fragmentHome)
              }
          }catch (e:Exception){}
        }.addOnFailureListener{}

        home_button.setOnClickListener(View.OnClickListener {
            profile_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_profile_0
                ));
            home_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.homesoncur
                ));
            if(email == "admin@soncur.com"){
                showFragment(AddFragment())
            }else{
                if(email !== null) {
                    showFragment(fragmentHome)
                }
            }
        })
        profile_button.setOnClickListener(View.OnClickListener {
            profile_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_profile_1
                ));
            home_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.homenotselected
                ));

            showFragment(fragmentProfile)
        })
        scan_button_main!!.setOnClickListener(View.OnClickListener {
            showFragment(Camera())
//            Toast.makeText(this,"Not available for now.",Toast.LENGTH_SHORT).show()
        })
    }

    private fun showFragment(fragment: Fragment){
        try{
            val frame = supportFragmentManager.beginTransaction()
            frame.replace(R.id.fragment_main,fragment)
            frame.commit()
        }catch (e:Exception){

        }
    }

    override fun onStart() {
        super.onStart()
        var user = auth!!.currentUser
        if(user==null){
            var iLogin =   Intent(this, Login::class.java)
            startActivity(iLogin)
            finish()
        }

    }
}