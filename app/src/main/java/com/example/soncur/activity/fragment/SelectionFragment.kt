package com.example.soncur.activity.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.soncur.R
import kotlinx.android.synthetic.main.selection_fragment.*


class SelectionFragment  : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.selection_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        have_an_email_account.setOnClickListener(View.OnClickListener {
            showFragment(EmailFragment())
        })


        mobile_no_input.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                showFragment(MobileFragment())
            } else {
                showFragment(MobileFragment())
            }
        })

        mobile_no_input_layout.setOnClickListener(View.OnClickListener {
            showFragment(MobileFragment())
        })

    }

    fun showFragment(fragment: Fragment){
        try{
            val frame = requireActivity().supportFragmentManager.beginTransaction()
            frame.replace(R.id.fragment_login,fragment)
            frame.commit()
        }catch (e:Exception){

        }
    }
}