import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.connectwise.R

class InternMeetingsAdapter(
    private var meetingsRequests: List<MeetingsRequestsIntern>,
    private val onAccept: (MeetingsRequestsIntern) -> Unit,
    private val onDecline: (MeetingsRequestsIntern) -> Unit
) : RecyclerView.Adapter<InternMeetingsAdapter.InternMeetingViewHolder>() {

    class InternMeetingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCompanyName: TextView = view.findViewById(R.id.tvCompanyName)
        val tvMeetingDateTime: TextView = view.findViewById(R.id.tvMeetingDateTime)
        val btnAccept: Button = view.findViewById(R.id.btnAccept)
        val btnDecline: Button = view.findViewById(R.id.btnDecline)
        // Add other views as needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InternMeetingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_intern_meeting, parent, false)
        return InternMeetingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InternMeetingViewHolder, position: Int) {
        val meetingRequest = meetingsRequests[position]
        holder.tvCompanyName.text = meetingRequest.BusinessOwnerCompanyName
        holder.tvMeetingDateTime.text = meetingRequest.MeetingDateTime

        holder.btnAccept.setOnClickListener { onAccept(meetingRequest) }
        holder.btnDecline.setOnClickListener { onDecline(meetingRequest) }
    }

    override fun getItemCount() = meetingsRequests.size

    fun updateMeetingsRequests(newMeetingsRequests: List<MeetingsRequestsIntern>) {
        meetingsRequests = newMeetingsRequests
        notifyDataSetChanged()
    }
}