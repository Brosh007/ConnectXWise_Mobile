package com.example.connectwise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WebinarsInternAdapter(
    private val webinars: MutableList<Webinar>,
    private val onSignUp: (Webinar) -> Unit
) : RecyclerView.Adapter<WebinarsInternAdapter.WebinarViewHolder>() {

    class WebinarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val dateAndTime: TextView = view.findViewById(R.id.dateAndTime)
        val signUpButton: Button = view.findViewById(R.id.signUpButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebinarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_intern_webinar, parent, false)
        return WebinarViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebinarViewHolder, position: Int) {
        val webinar = webinars[position]
        holder.title.text = webinar.Title
        holder.dateAndTime.text = webinar.DateAndTime
        holder.signUpButton.setOnClickListener { onSignUp(webinar) }
    }

    override fun getItemCount(): Int = webinars.size

    fun updateWebinars(webinarsList: List<Webinar>) {
        webinars.clear()
        webinars.addAll(webinarsList)
        notifyDataSetChanged()
    }
}