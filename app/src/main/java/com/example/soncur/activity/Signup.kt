package com.example.soncur.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soncur.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.signup_screen.*

class Signup : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    private var database= FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        try {
            database.setPersistenceEnabled(true)
            database.setPersistenceCacheSizeBytes((50 * 1000 * 1000).toLong())
        } catch (e: Exception) {

        }
        var auth= FirebaseAuth.getInstance()
        myRef = database!!.reference
        val iLoginScreen = Intent(this@Signup, Login::class.java)

        signup_button.setOnClickListener(View.OnClickListener {

            val email = signup_email.text.toString()
            val password = signup_password.text.toString()

            if (signup_email.text.toString().isBlank() || signup_password.text.toString().isBlank() || contact_no.text.toString().isBlank() || signup_confirm_password.text.toString().isBlank() || full_name.text.toString().isBlank()) {

                Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_LONG).show()

            } else {

                if(signup_password.text.toString() == signup_confirm_password.text.toString()){

                    progress_bar_signup.visibility= View.VISIBLE
                    signup_button.visibility= View.INVISIBLE
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            var localRef = myRef!!.child("myAuthUser").child(auth!!.uid.toString())
                            try {
                                localRef!!.child("Name").setValue(full_name.text.toString()).addOnCompleteListener { task ->
                                    if(task.isSuccessful) {
                                        localRef!!.child("Email").setValue(email).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                localRef!!.child("Contact").setValue(contact_no.text.toString()).addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        if(task.isSuccessful) {
                                                            progress_bar_signup.visibility = View.GONE
                                                            signup_button.visibility = View.VISIBLE
                                                            val data = hashMapOf(
                                                                "Email" to email.toString()
                                                            )
                                                            db.collection("Links").document(auth!!.uid.toString())
                                                                .set(data)
                                                                .addOnSuccessListener {
                                                                    auth.signOut()
                                                                    startActivity(iLoginScreen)
                                                                    finish()
                                                                    Log.d(TAG, "DocumentSnapshot successfully written!") }
                                                                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                Toast.makeText(this, "Something went wrong please contact support", Toast.LENGTH_LONG).show()
                                progress_bar_signup.visibility = View.GONE
                                signup_button.visibility = View.VISIBLE
                                auth.signOut()
                            }
                        }
                    }.addOnFailureListener { exception ->
                        progress_bar_signup.visibility= View.GONE
                        signup_button.visibility= View.VISIBLE
                        Toast.makeText(this, "Something went wrong ($exception)", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "Check password", Toast.LENGTH_LONG).show()
                }
            }
        })

        go_to_login_page.setOnClickListener {
            startActivity(iLoginScreen)
            finish()
        }

    }
}