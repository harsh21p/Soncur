package com.example.soncur.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.soncur.R
import com.example.soncur.activity.fragment.EmailFragment
import com.example.soncur.activity.fragment.MobileFragment
import com.example.soncur.activity.fragment.OtpVerification
import com.example.soncur.activity.fragment.SelectionFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_screen.*

class Login : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        auth = FirebaseAuth.getInstance()

        val fragmentEmail = EmailFragment()
        val fragmentSelection= SelectionFragment()
        val fragmentMobile = MobileFragment()
        val fragmentOtpVerification = OtpVerification()

        showFragment(fragmentSelection)

        go_to_signup_page.setOnClickListener(View.OnClickListener {
            val iSignup = Intent(this, Signup::class.java)
            startActivity(iSignup)
        })

    }

    private fun showFragment(fragment: Fragment){
        try{
            val frame = supportFragmentManager.beginTransaction()
            frame.replace(R.id.fragment_login,fragment)
            frame.commit()
        }catch (e:Exception){

        }
    }

    override fun onStart() {
        super.onStart()
        var user = auth.currentUser
        if(user!=null){
            var iDashboard =   Intent(this, Dashboard::class.java)
            startActivity(iDashboard)
            finish()
        }

    }

}