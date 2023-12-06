package com.example.connectwise

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class WebinarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titleView: TextView = itemView.findViewById(R.id.tvWebinarTitle)
    private val dateView: TextView = itemView.findViewById(R.id.tvWebinarDateTime)
    private val descriptionView: TextView = itemView.findViewById(R.id.tvWebinarDescription)
    private val locationView: TextView = itemView.findViewById(R.id.tvWebinarLocation)
    private val speakerNameView: TextView = itemView.findViewById(R.id.tvWebinarSpeakerName)
    private val speakerBioView: TextView = itemView.findViewById(R.id.tvWebinarSpeakerBio)

    fun bind(webinar: Webinar) {
        titleView.text = webinar.Title
        dateView.text = webinar.DateAndTime
        descriptionView.text = webinar.Description
        locationView.text = webinar.Location
        speakerNameView.text = webinar.SpeakerName
        speakerBioView.text = webinar.SpeakerBio
    }
}