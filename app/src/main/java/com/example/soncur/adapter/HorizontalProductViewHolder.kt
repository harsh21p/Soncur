package com.example.soncur.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.soncur.R
import com.example.soncur.activity.Camera
import com.example.soncur.datamodel.ModelHorizontalProduct

internal class HorizontalProductViewHolder (private var List: ArrayList<ModelHorizontalProduct>, private val listener: Camera) :
    RecyclerView.Adapter<HorizontalProductViewHolder.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position!= RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
        var pName: TextView = view.findViewById(R.id.name)
        var pImage: ImageView = view.findViewById(R.id.image)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_product_view, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sessionName = List[position]
        holder.pName.text = sessionName.name
        productFinder(holder.pImage,sessionName.id)
    }

    private fun productFinder(productImage: ImageView,productNo:Int) {
        when(productNo){
            11 ->  productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.bop_pendant_gold_574_1
                ));
            12 -> productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.bop_pendant_silver_568_1
                ));
            13 -> productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.bop_pendant_silver_569_1
                ));
            14 -> productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.bop_pendant_silver_570_1
                ));
            15 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.bop_pendant_silver_572_1
                ));
            16 -> productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.bop_pendant_silver_573_1
                ));


            21 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.int_earing_565
                ));
            22 -> productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.int_earing_566
                ));
            23 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.int_pendant_567
                ));
            24 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.int_pendant_568
                ));
            25 -> productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.int_ring_567
                ));
            26 -> productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.int_ring_568
                ));


            31 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_cufflink_542
                ));
            32 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_cufflink_543
                ));
            33 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_cufflink_square_542
                ));
            34 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_cufflink_square_543
                ));


            41 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_pendant_png_303
                ));
            42 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_pendant_png_304
                ));
            43 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_pendant_png_305
                ));
            44 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_pendant_png_306
                ));
            45 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_pendant_png_307
                ));
            46 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_pendant_png_308
                ));


            51 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_ring_png_238
                ));
            52 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_ring_png_239
                ));
            53 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_ring_png_240
                ));
            54 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_ring_png_247
                ));
            55 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_ring_png_253
                ));
            56 ->productImage.setImageDrawable(
                ContextCompat.getDrawable(listener,
                    R.drawable.brochure_ring_render_ring_png_254
                ));
        }

    }

    override fun getItemCount(): Int {
        return List.size
    }
}
