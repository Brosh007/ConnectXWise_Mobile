package com.example.connectwise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InternMeetingAdapter(
    private val meetingRequests: MutableList<MeetingRequest>,
    private val onAccept: (MeetingRequest) -> Unit,
    private val onDecline: (MeetingRequest) -> Unit
) : RecyclerView.Adapter<InternMeetingAdapter.InternMeetingViewHolder>() {

    class InternMeetingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val companyName: TextView = view.findViewById(R.id.companyName)
        val meetingDateTime: TextView = view.findViewById(R.id.meetingDateTime)
        val acceptButton: Button = view.findViewById(R.id.acceptButton)
        val declineButton: Button = view.findViewById(R.id.declineButton)
        // Additional view bindings
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InternMeetingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meeting_intern_request, parent, false)
        return InternMeetingViewHolder(view)
    }

    override fun onBindViewHolder(holder: InternMeetingViewHolder, position: Int) {
        val item = meetingRequests[position]
        holder.companyName.text = item.BusinessOwnerCompanyName
        holder.meetingDateTime.text = item.MeetingDateTime
        holder.acceptButton.setOnClickListener { onAccept(item) }
        holder.declineButton.setOnClickListener { onDecline(item) }
        // Additional view bindings
    }

    override fun getItemCount(): Int = meetingRequests.size

    fun updateMeetingsRequests(newMeetingsRequests: List<MeetingRequest>) {
        meetingRequests.clear()
        meetingRequests.addAll(newMeetingsRequests)
        notifyDataSetChanged()
    }
}