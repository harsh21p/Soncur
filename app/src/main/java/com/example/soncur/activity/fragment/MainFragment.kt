package com.example.soncur.activity.fragment

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soncur.R
import com.example.soncur.activity.Camera
import com.example.soncur.activity.StaticRef.*
import com.example.soncur.adapter.NonPersonalProductAdapter
import com.example.soncur.adapter.ProductAdapter
import com.example.soncur.datamodel.ModelProductList
import com.example.soncur.datamodel.Product
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    @RequiresApi(Build.VERSION_CODES.O)
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
        val typeface = resources.getFont(R.font.poppins_semi_bold)
        val typeface1 = resources.getFont(R.font.poppins_regular)



        personal!!.setOnClickListener(View.OnClickListener {
            personal!!.typeface = typeface
            non_personal!!.typeface = typeface1
            personal_product_recycler!!.visibility=View.VISIBLE
            non_personal_product_recycler!!.visibility=View.GONE
        })
        non_personal!!.setOnClickListener(View.OnClickListener {
            personal!!.typeface = typeface1
            non_personal!!.typeface = typeface
            personal_product_recycler!!.visibility=View.GONE
            non_personal_product_recycler!!.visibility=View.VISIBLE
        })
        auth= FirebaseAuth.getInstance()
        myRef = database!!.reference

        myRef!!.child("url_data").get().addOnSuccessListener {
            url = it.child("url").value.toString()
            Log.e("URL", url)
        }.addOnFailureListener {
            Log.e("URL",it.toString())
        }
        var uProduct = mutableListOf<Product>()
        progress_dashboard!!.visibility = View.VISIBLE
        val docRef = db.collection("Links").document(auth!!.uid.toString())
        docRef.collection("Products").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                   document.forEach{ it ->
                       if(it != null){
                           progress_dashboard!!.visibility = View.GONE
                            val date = it.data.get("Date").toString()
                           val for_ = it.data.get("For").toString()
                           val material = it.data.get("Material").toString()
                           val name = it.data.get("Name").toString()
                           val occasion = it.data.get("Occasion").toString()
                           val plating = it.data.get("Plating").toString()
                           val type = it.data.get("Type").toString()
                           val id = it.data.get("ID").toString()
                           val typeOfProduct = it.data.get("typeOfProduct").toString()
                           val image: Int = it.data.get("Image").toString().toInt()
                           val shape =  it.data.get("Shape").toString()
                           val relationship = it.data.get("Relationship").toString()
                           uProduct.add(Product(date,for_,material,name,occasion,plating,type,id,typeOfProduct,image,shape,relationship))
                       }
                   }
                    setProducts(uProduct)
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
                try {
                    progress_dashboard!!.visibility = View.GONE
                }catch (e:Exception){
                }
            }
    }
    private fun setProducts(uProduct: MutableList<Product>) {
        productList.clear()
        nonPersonalProductList.clear()
        if(uProduct.isNotEmpty()){
            uProduct.forEach { it ->
                var model = ModelProductList(it.image,it.occasion,it.typeOfProduct,it.name,it.id,it.shape,it)
                if (it.typeOfProduct !== "Non Personal") {
                    productList!!.add(model)
                    productAdapter!!.notifyDataSetChanged()
                } else {
                    nonPersonalProductList!!.add(model)
                    nonPersonalProductAdapter!!.notifyDataSetChanged()
                }
            }
            progress_dashboard!!.visibility = View.GONE
        }else{
            progress_dashboard!!.visibility = View.GONE
        }
        onItemClick(0)
    }
    fun onItemClick(position: Int) {
        productId = productList[position].uProductId
        productShape = productList[position].uProductShape
        uFinalProduct = productList[position].product
//        showFragment(Camera())
    }
    private fun showFragment(fragment: Fragment){
        try{
            val frame = requireActivity().supportFragmentManager.beginTransaction()
            frame.replace(R.id.fragment_main,fragment)
            frame.commit()
        }catch (e:Exception){

        }
    }
    fun onItemClickNonPersonal(position: Int) {
        productId = nonPersonalProductList[position].uProductId
        productShape = nonPersonalProductList[position].uProductShape
//        showFragment(Camera())
    }

}