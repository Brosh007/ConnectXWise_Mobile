package com.example.connectwise



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class InternAdapter(context: Context, private val interns: List<Intern>)
    : ArrayAdapter<Intern>(context, 0, interns) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_intern, parent, false)
        val intern = getItem(position)

        val internIdTextView = view.findViewById<TextView>(R.id.textViewName)
        internIdTextView.text = intern?.InternID

        return view
    }
}
