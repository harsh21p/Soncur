package com.example.soncur.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soncur.R
import com.example.soncur.adapter.ProductAdapter
import com.example.soncur.adapter.UserAdapter
import com.example.soncur.datamodel.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add.add_button
import kotlinx.android.synthetic.main.fragment_add.date
import kotlinx.android.synthetic.main.fragment_add.for_who
import kotlinx.android.synthetic.main.fragment_add.id_o
import kotlinx.android.synthetic.main.fragment_add.image
import kotlinx.android.synthetic.main.fragment_add.listOfUsers
import kotlinx.android.synthetic.main.fragment_add.material
import kotlinx.android.synthetic.main.fragment_add.name_of_owner
import kotlinx.android.synthetic.main.fragment_add.occasion
import kotlinx.android.synthetic.main.fragment_add.plating
import kotlinx.android.synthetic.main.fragment_add.relationship
import kotlinx.android.synthetic.main.fragment_add.shape
import kotlinx.android.synthetic.main.fragment_add.type
import kotlinx.android.synthetic.main.fragment_add.type_of_product
import kotlinx.android.synthetic.main.fragment_add.user

class AddFragment  : Fragment() {
    lateinit var auth: FirebaseAuth
    var db = FirebaseFirestore.getInstance()
    var data: HashMap<String, String>? = null
    var dropDown:Boolean = true
    var list = ArrayList<User>()
    private var userAdapter: UserAdapter? = null
    private var selected: User? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userAdapter =  UserAdapter(list,this,requireActivity())

        val userRecyclerView = requireView()!!.findViewById<RecyclerView>(R.id.listOfUsers)
        userRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        userRecyclerView!!.adapter = userAdapter!!

        val docRefUser = db.collection("Links")
        docRefUser.get().addOnSuccessListener { document ->
                if (document !== null) {
                    document.forEach{
                        Log.e("LINK",it.toString())
                        list.add(User(it.data!!.get("Email").toString(),it.id.toString()))
                    }
                    userAdapter!!.notifyDataSetChanged()
                }
            }

        auth = FirebaseAuth.getInstance()

        add_button.setOnClickListener(View.OnClickListener {
            if(selected !== null) {
                val docRef = docRefUser.document(selected!!.auth)
                addProduct(docRef)
            }else{
                Toast.makeText(requireActivity(),"Select User",Toast.LENGTH_SHORT).show()
            }
        })

        user.setOnClickListener(View.OnClickListener {
            if(list.isNotEmpty()){
                if(dropDown){
                    listOfUsers.visibility = View.VISIBLE
                }else{
                    listOfUsers.visibility = View.GONE
                }
                dropDown = !dropDown
            }else{
                Toast.makeText(requireActivity(),"No Users Found",Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun addProduct(docRef: DocumentReference){
        if(date.text.isNotBlank() &&
            for_who.text.isNotBlank() &&
            name_of_owner.text.isNotBlank() &&
            occasion.text.isNotBlank() &&
            plating.text.isNotBlank() &&
            image.text.isNotBlank() &&
            relationship.text.isNotBlank() &&
            shape.text.isNotBlank() &&
            type.text.isNotBlank() &&
            type_of_product.text.isNotBlank() &&
            material.text.isNotBlank() &&
            id_o.text.isNotBlank() &&
            user.text.isNotBlank()){
            data = hashMapOf(
                "Date" to date.text.toString(),
                "For" to for_who.text.toString(),
                "Name" to name_of_owner.text.toString(),
                "Occasion" to occasion.text.toString(),
                "Plating" to plating.text.toString(),
                "Image" to image.text.toString(),
                "Relationship" to relationship.text.toString(),
                "Shape" to shape.text.toString(),
                "Type" to type.text.toString(),
                "typeOfProduct" to type_of_product.text.toString(),
                "Material" to material.text.toString(),
                "ID" to id_o.text.toString()
            )
            docRef.collection("Products")
                .add(data!!)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(requireActivity(),"New Product Added",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireActivity(), "Error$e",Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(requireActivity(), "Fill all the details",Toast.LENGTH_SHORT).show()
        }
    }

    fun onItemClick(position: Int) {
        selected = list[position]
        user.text=selected!!.email
        listOfUsers.visibility = View.GONE
        dropDown = !dropDown
    }
}