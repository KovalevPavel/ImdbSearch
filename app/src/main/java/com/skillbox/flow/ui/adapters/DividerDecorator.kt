package com.skillbox.flow.ui.adapters

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DividerDecorator(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = 8
        with(outRect) {
            top = offset.dpToPixels(context)
            bottom = offset.dpToPixels(context)
        }
    }

    private fun Int.dpToPixels(context: Context): Int {
        val density = context.resources.displayMetrics.densityDpi
        val pixelsInDp = density / DisplayMetrics.DENSITY_DEFAULT
        return this * pixelsInDp
    }
}
