
package com.example.soncur.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.soncur.R
import com.example.soncur.activity.fragment.MainFragment
import com.example.soncur.datamodel.ModelProductList
import kotlinx.android.synthetic.main.product_recyclerview_layout.view.*

internal class NonPersonalProductAdapter (private var List: List<ModelProductList>, private val listener: MainFragment,private val context: Context) :
    RecyclerView.Adapter<NonPersonalProductAdapter.MyViewHolder>() {

    internal open inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {

//            itemView.scan_button.setOnClickListener(this)

        }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position!= RecyclerView.NO_POSITION) {
                listener.onItemClickNonPersonal(position)
            }
        }
    }


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_recyclerview_layout, parent, false)
        return View1ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        (holder as View1ViewHolder).bind(position)
    }
    override fun getItemCount(): Int {
        return List.size
    }
    private inner class View1ViewHolder(itemView: View) :
        MyViewHolder(itemView)  {
        var productImage: ImageView = itemView.findViewById(R.id.product_image)
        var titleWithName: TextView = itemView.findViewById(R.id.title_with_name)
        var sentence: TextView = itemView.findViewById(R.id.sentence)
        var scanButton: TextView = itemView.findViewById(R.id.scan_button)
        fun bind(position: Int) {
            val recyclerViewModel = List[position]
            var productNo = recyclerViewModel.uProductNo
            sentence.text = recyclerViewModel.uSentence
            titleWithName.text = recyclerViewModel.uTitle
            productFinder(productImage,productNo)
        }

        private fun productFinder(productImage: ImageView,productNo:Int) {
            when(productNo){
                11 ->  productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.bop_pendant_gold_574_1
                    ));
                12 -> productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.bop_pendant_silver_568_1
                    ));
                13 -> productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.bop_pendant_silver_569_1
                    ));
                14 -> productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.bop_pendant_silver_570_1
                    ));
                15 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.bop_pendant_silver_572_1
                    ));
                16 -> productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.bop_pendant_silver_573_1
                    ));


                21 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.int_earing_565
                    ));
                22 -> productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.int_earing_566
                    ));
                23 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.int_pendant_567
                    ));
                24 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.int_pendant_568
                    ));
                25 -> productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.int_ring_567
                    ));
                26 -> productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.int_ring_568
                    ));


                31 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_cufflink_542
                    ));
                32 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_cufflink_543
                    ));
                33 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_cufflink_square_542
                    ));
                34 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_cufflink_square_543
                    ));


                41 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_pendant_png_303
                    ));
                42 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_pendant_png_304
                    ));
                43 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_pendant_png_305
                    ));
                44 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_pendant_png_306
                    ));
                45 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_pendant_png_307
                    ));
                46 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_pendant_png_308
                    ));


                51 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_ring_png_238
                    ));
                52 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_ring_png_239
                    ));
                53 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_ring_png_240
                    ));
                54 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_ring_png_247
                    ));
                55 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_ring_png_253
                    ));
                56 ->productImage.setImageDrawable(
                    ContextCompat.getDrawable(context,
                        R.drawable.brochure_ring_render_ring_png_254
                    ));
            }

        }
    }
}
