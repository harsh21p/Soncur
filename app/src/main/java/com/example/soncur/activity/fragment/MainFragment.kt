package com.example.soncur.activity.fragment

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
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
import com.example.soncur.activity.Camera
import com.example.soncur.activity.StaticRef.*
import com.example.soncur.adapter.NonPersonalProductAdapter
import com.example.soncur.adapter.ProductAdapter
import com.example.soncur.datamodel.ModelProductList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {
    private var database= FirebaseDatabase.getInstance()
    private var myRef: DatabaseReference? = null
    lateinit var auth: FirebaseAuth
    private var productList = ArrayList<ModelProductList>()
    private var nonPersonalProductList = ArrayList<ModelProductList>()
    private var productAdapter:ProductAdapter? = null
    private var nonPersonalProductAdapter:NonPersonalProductAdapter? = null
    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        nonPersonalProductAdapter =  NonPersonalProductAdapter(nonPersonalProductList,this@MainFragment,requireActivity())
        productAdapter =  ProductAdapter(productList,this@MainFragment,requireActivity())

        val productRecyclerView = requireView()!!.findViewById<RecyclerView>(R.id.personal_product_recycler)
        productRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        productRecyclerView!!.adapter = productAdapter!!

        val nonPersonalProductRecyclerView = requireView()!!.findViewById<RecyclerView>(R.id.non_personal_product_recycler)
        nonPersonalProductRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        nonPersonalProductRecyclerView!!.adapter = nonPersonalProductAdapter!!

        personal_button!!.setOnClickListener(View.OnClickListener {
            personal!!.setTextColor(Color.parseColor("#000000") )
            non_personal!!.setTextColor(Color.parseColor("#BDBDBD"))
            personal_selected!!.visibility= View.VISIBLE
            non_personal_selected!!.visibility= View.INVISIBLE
            personal_product_recycler!!.visibility=View.VISIBLE
            non_personal_product_recycler!!.visibility=View.GONE
        })
        non_personal_button!!.setOnClickListener(View.OnClickListener {
            non_personal!!.setTextColor(Color.parseColor("#000000"))
            personal!!.setTextColor(Color.parseColor("#BDBDBD"))
            non_personal_selected!!.visibility= View.VISIBLE
            personal_selected!!.visibility= View.INVISIBLE
            personal_product_recycler!!.visibility=View.GONE
            non_personal_product_recycler!!.visibility=View.VISIBLE
        })

        auth= FirebaseAuth.getInstance()
        myRef = database!!.reference

        var uProduct:String?=null

        progress_dashboard!!.visibility = View.VISIBLE

        val docRef = db.collection("Links").document(auth!!.uid.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    uProduct = document.data!!.getValue("Product").toString()
                    if(uProduct == "") {
                        try {
                            progress_dashboard!!.visibility = View.GONE
                            Toast.makeText(requireActivity(),"Not found",Toast.LENGTH_LONG).show()
                        }catch (e:Exception){

                        }

                    }else{
                        setProducts(uProduct!!)
                    }
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)

                try {
                    progress_dashboard!!.visibility = View.GONE
                    Toast.makeText(requireActivity(),"Not found",Toast.LENGTH_LONG).show()
                }catch (e:Exception){

                }

            }

    }

    private fun setProducts(uProduct: String) {

        productList.clear()
        nonPersonalProductList.clear()

        uProductList = uProduct!!.split(':')
        var i = 0
        if(uProductList.isNotEmpty()){
        while (i < uProductList.size){
            var uProductListSecond = uProductList[i].split(',')
            if(uProductListSecond.size!=1){
                var temp = uProductListSecond[3]+"\n"+uProductListSecond[4]
                var model = ModelProductList(uProductListSecond[1].toInt(),temp,uProductListSecond[0],uProductListSecond[2],uProductListSecond[5],uProductListSecond[0])
                if(uProductListSecond[0].toInt() != 2) {
                    productList!!.add(model)
                    productAdapter!!.notifyDataSetChanged()
                }else{
                    nonPersonalProductList!!.add(model)
                    nonPersonalProductAdapter!!.notifyDataSetChanged()
                }
            }else{
                try {
                    progress_dashboard!!.visibility = View.GONE
                    Toast.makeText(requireActivity(),"Not found",Toast.LENGTH_LONG).show()
                }catch (e:Exception){

                }

            }
            i+=1
            try {
                progress_dashboard!!.visibility = View.GONE
            }catch (e:Exception){

            }
        }
        }else {
            try {
                progress_dashboard!!.visibility = View.GONE
                Toast.makeText(requireActivity(),"Not found",Toast.LENGTH_LONG).show()
            }catch (e:Exception){

            }

        }
    }

    fun onItemClick(position: Int) {
        productId = productList[position].uProductId
        productShape = productList[position].uProductShape
        val iCameraScreen = Intent(activity, Camera::class.java)
        startActivity(iCameraScreen)
    }

    fun onItemClickNonPersonal(position: Int) {
        productId = nonPersonalProductList[position].uProductId
        productShape = nonPersonalProductList[position].uProductShape
        val iCameraScreen = Intent(activity, Camera::class.java)
        startActivity(iCameraScreen)
    }

}