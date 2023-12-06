package com.example.connectwise

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MeetingRequestAdapter(private val context: Context, private val meetingRequests: List<MeetingRequest>) : ArrayAdapter<MeetingRequest>(context, 0, meetingRequests) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_meeting_request, parent, false)
        }

        val meetingRequest = meetingRequests[position]

        listItemView!!.findViewById<TextView>(R.id.tvCompanyName).text = meetingRequest.BusinessOwnerCompanyName
        listItemView.findViewById<TextView>(R.id.tvInternName).text = "${meetingRequest.InternFirstName} ${meetingRequest.InternLastName}"
        listItemView.findViewById<TextView>(R.id.tvDateTime).text = meetingRequest.MeetingDateTime
        listItemView.findViewById<TextView>(R.id.tvPurpose).text = meetingRequest.MeetingPurpose
        listItemView.findViewById<TextView>(R.id.tvStatus).text = meetingRequest.Status

        return listItemView
    }
}