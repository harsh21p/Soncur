package com.example.soncur.activity.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.soncur.R
import kotlinx.android.synthetic.main.mobile_no_fragment.*

class MobileFragment  : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mobile_no_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mobile_no_input.requestFocus()

        continue_button.setOnClickListener(View.OnClickListener {
            if(mobile_no_input.text.isNotBlank() && mobile_no_input.text.length == 10){

                    showFragment(OtpVerification())

            }else{
                Toast.makeText(requireActivity(),"Please enter mobile number",Toast.LENGTH_SHORT).show()
            }
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