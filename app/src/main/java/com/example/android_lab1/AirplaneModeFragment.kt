package com.example.android_lab1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class AirplaneModeFragment : Fragment(R.layout.fragment_airplane_mode) {

    private lateinit var airplaneModeReceiver: BroadcastReceiver
    private lateinit var textViewAirplaneMode: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewAirplaneMode = view.findViewById(R.id.textView_airplane_mode)

        airplaneModeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                    val isEnabled = intent.getBooleanExtra("state", false)
                    textViewAirplaneMode.text = when {
                        isEnabled -> "Airplane mode is enabled"
                        else -> "Airplane mode is disabled"

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        requireActivity().registerReceiver(airplaneModeReceiver, filter)
    }

    override fun onPause() {
        super.onPause()

        requireActivity().unregisterReceiver(airplaneModeReceiver)
    }
}
