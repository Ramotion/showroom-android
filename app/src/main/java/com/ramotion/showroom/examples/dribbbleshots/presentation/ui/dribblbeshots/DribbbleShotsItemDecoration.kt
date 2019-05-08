package com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ramotion.showroom.R
import kotlin.math.roundToInt

class DribbbleShotsItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

  private val innerOffset = context.resources.getDimension(R.dimen.dribbble_shots_inner_offset).roundToInt()
  private val outsideOffset = context.resources.getDimension(R.dimen.dribbble_shots_outside_offset).roundToInt()

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    val position = parent.getChildAdapterPosition(view)

    val viewType = if (position >= 0) parent.adapter?.getItemViewType(position) else -1

    outRect.left = when {
      viewType == SHOTS_LOADING_ITEM -> 0
      position == 0 || position % 2 == 0 -> outsideOffset
      else -> innerOffset
    }
    outRect.top = when {
      position == 0 || position == 1 -> 0
      else -> innerOffset
    }
    outRect.right = when {
      viewType == SHOTS_LOADING_ITEM -> 0
      position % 2 > 0 -> outsideOffset
      else -> innerOffset
    }
    outRect.bottom = innerOffset
  }
}