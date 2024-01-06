package com.example.soncur.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.soncur.R
import com.example.soncur.activity.fragment.AddFragment
import com.example.soncur.datamodel.User

internal class UserAdapter(private var List: List<User>, private val listener: AddFragment,private val context: Context) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    internal open inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position!= RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardofuser, parent, false)
        return View1ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        (holder as View1ViewHolder).bind(position)
    }
    override fun getItemCount(): Int {
        return List.size
    }
    private inner class View1ViewHolder(itemView: View) :
        MyViewHolder(itemView) {
        var userNameEmail: TextView = itemView.findViewById(R.id.userNameEmail)
        fun bind(position: Int) {
            val recyclerViewModel = List[position]
            userNameEmail.text = recyclerViewModel.email
        }
    }
}
