package com.example.soncur.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.soncur.R
import com.example.soncur.activity.fragment.MainFragment
import com.example.soncur.activity.fragment.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dashboard.*

class Dashboard : AppCompatActivity() {
    private var auth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        auth = FirebaseAuth.getInstance()
        val fragmentHome = MainFragment()
        val fragmentProfile = ProfileFragment()
        showFragment(fragmentHome)
        home_button.setOnClickListener(View.OnClickListener {
            profile_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_profile_0
                ));
            home_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.homesoncur
                ));
            showFragment(fragmentHome)
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
//            profile_button.setImageDrawable(
//                ContextCompat.getDrawable(this,
//                    R.drawable.ic_profile_0
//                ));
//            home_button.setImageDrawable(
//                ContextCompat.getDrawable(this,
//                    R.drawable.homenotselected
//                ));
//            showFragment(camFragment)
            Toast.makeText(this,"Not available for now.",Toast.LENGTH_SHORT);
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