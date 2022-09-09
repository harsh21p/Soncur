package com.example.soncur.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.soncur.R
import com.example.soncur.activity.Dashboard

class OtpVerification  : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.otp_verification_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    fun onVerificationComplete(){
        val iDashboard = Intent(requireActivity(), Dashboard::class.java)
        startActivity(iDashboard)
        requireActivity().finish()
    }
}