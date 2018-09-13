package com.ramotion.showroom.examples.navigationtoolbar.pager

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ramotion.showroom.R
import com.ramotion.showroom.examples.navigationtoolbar.PageDataSet

sealed class PageItem(view: View) : RecyclerView.ViewHolder(view) {
    fun clearContent() {}
}

class ItemUser(view: View) : PageItem(view) {
    private val avatar = view.findViewById<ImageView>(R.id.avatar)
    private val userName = view.findViewById<TextView>(R.id.user_name)
    private val status = view.findViewById<TextView>(R.id.status)

    fun setContent(content: PageDataSet.ItemData) {
        userName.setText(content.userName)
        status.setText(content.status)
        avatar.setImageResource(content.avatar)

        Glide.with(avatar.context).load(content.avatar).into(avatar)
    }
}

class ItemImage(view: View) : PageItem(view) {
    private val imageView = view.findViewById<ImageView>(R.id.page_image)

    fun setImage(imgId: Int) {
        Glide.with(imageView.context).load(imgId).into(imageView)
    }
}
