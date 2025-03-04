package com.example.android_lab1.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_lab1.R
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        // Set up button click listeners to navigate away from HomeFragment:
        view.findViewById<MaterialButton>(R.id.share_to_instagram).setOnClickListener {
            navController.navigate(R.id.fragment_instagram_stories)
        }
        view.findViewById<MaterialButton>(R.id.music_player).setOnClickListener {
            navController.navigate(R.id.fragment_music_player)
        }
        view.findViewById<MaterialButton>(R.id.airplane_mode).setOnClickListener {
            navController.navigate(R.id.fragment_airplane_mode)
        }
        view.findViewById<MaterialButton>(R.id.see_upcoming_events).setOnClickListener {
            navController.navigate(R.id.fragment_see_upcoming_events)
        }
    }
}