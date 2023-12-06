package com.example.connectwise

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WebinarsAdapter(private var webinarsList: List<Webinar>) :
    RecyclerView.Adapter<WebinarsAdapter.WebinarViewHolder>() {

    class WebinarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = view.findViewById(R.id.textViewDescription)
        val textViewDateTime: TextView = view.findViewById(R.id.textViewDateTime)
        val textViewSpeaker: TextView = view.findViewById(R.id.textViewSpeaker)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebinarViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_webinars, parent, false)
        return WebinarViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WebinarViewHolder, position: Int) {
        val webinar = webinarsList[position]
        holder.textViewTitle.text = webinar.Title
        holder.textViewDescription.text = webinar.Description
        holder.textViewDateTime.text = webinar.DateAndTime
        holder.textViewSpeaker.text = webinar.SpeakerName
    }

    override fun getItemCount() = webinarsList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateWebinars(newWebinarsList: List<Webinar>) {
        webinarsList = newWebinarsList
        notifyDataSetChanged()
    }
}