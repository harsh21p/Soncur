package com.example.soncur.activity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.example.soncur.R


class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    init {
        inflate(context,R.layout.product_recyclerview_layout,null)
    }
}