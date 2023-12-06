package com.example.connectwise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WebinarAdapter(private var webinars: List<Webinar>) : RecyclerView.Adapter<WebinarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebinarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_webinar, parent, false)
        return WebinarViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebinarViewHolder, position: Int) {
        holder.bind(webinars[position])
    }

    override fun getItemCount(): Int = webinars.size

    fun updateData(newWebinars: List<Webinar>) {
        webinars = newWebinars
        notifyDataSetChanged()
    }
}