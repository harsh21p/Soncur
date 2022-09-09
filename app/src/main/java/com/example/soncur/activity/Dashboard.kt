package com.example.soncur.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.soncur.R
import com.example.soncur.activity.fragment.CartFragment
import com.example.soncur.activity.fragment.MagicFragment
import com.example.soncur.activity.fragment.MainFragment
import com.example.soncur.activity.fragment.ProfileFragment
import kotlinx.android.synthetic.main.dashboard.*

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        val fragmentHome = MainFragment()
        val fragmentProfile = ProfileFragment()
        val fragmentCart = CartFragment()
        val fragmentMagic = MagicFragment()

        showFragment(fragmentHome)

        home_button.setOnClickListener(View.OnClickListener {
            profile_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_profile_0
                ));
            home_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_home_1
                ));
            cart_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_cart_0
                ));
            magic_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.settings_0
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
                    R.drawable.ic_home_0
                ));
            cart_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_cart_0
                ));
            magic_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.settings_0
                ));
            showFragment(fragmentProfile)
        })

        magic_button.setOnClickListener(View.OnClickListener {
            profile_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_profile_0
                ));
            home_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_home_0
                ));
            cart_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_cart_0
                ));
            magic_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_settings_1
                ));
            showFragment(fragmentMagic)
        })

        cart_button.setOnClickListener(View.OnClickListener {
            profile_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_profile_0
                ));
            home_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_home_0
                ));
            cart_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.ic_cart_1
                ));
            magic_button.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.settings_0
                ));
            showFragment(fragmentCart)
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
}