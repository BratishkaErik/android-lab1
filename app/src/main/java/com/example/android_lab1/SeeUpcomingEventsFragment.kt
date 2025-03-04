package com.example.android_lab1

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class SeeUpcomingEventsFragment : Fragment(R.layout.fragment_see_upcoming_events) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView_events)
        adapter = EventsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private val PERMISSION_REQUEST_CODE = 100

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissions(
                arrayOf(Manifest.permission.READ_CALENDAR),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        fetchEvents()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            fetchEvents()
            return
        }
    }

    @SuppressLint("Range")
    private fun fetchEvents() {
        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )

        val selection = "${CalendarContract.Events.DTSTART} > ?"
        val selectionArgs = arrayOf(
            System.currentTimeMillis().toString()
        )

        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

        requireContext().contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val events = mutableListOf<EventData>()
            while (cursor.moveToNext()) {
                val title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE))
                val startTime =
                    cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART))
                val endTime = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTEND))
                events.add(EventData(title, startTime, endTime))
            }
            adapter.submitList(events)
        }
    }
}

data class EventData(val title: String, val startTime: Long, val endTime: Long)

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    private var events: List<EventData> = emptyList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView = itemView.findViewById<TextView>(R.id.textView_event_title)
        val timeTextView = itemView.findViewById<TextView>(R.id.textView_event_time)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.titleTextView.text = event.title

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val startTime = sdf.format(Date(event.startTime))
        val endTime = sdf.format(Date(event.endTime))
        holder.timeTextView.text = "$startTime - $endTime"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = events.size

    fun submitList(newEvents: List<EventData>) {
        events = newEvents
        notifyDataSetChanged()
    }
}