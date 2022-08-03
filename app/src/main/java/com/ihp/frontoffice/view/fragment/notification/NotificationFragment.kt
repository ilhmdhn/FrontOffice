package com.ihp.frontoffice.view.fragment.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ihp.frontoffice.R
import com.ihp.frontoffice.databinding.FragmentNotificationBinding
import com.ihp.frontoffice.events.EventsWrapper.TitleFragment
import com.ihp.frontoffice.events.GlobalBus

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMainTitle()

    }

    private fun setMainTitle() {
        GlobalBus
            .getBus()
            .post(TitleFragment("Notifikasi"))
    }

    override fun onStart() {
        super.onStart()
        GlobalBus.getBus().register(this)
    }

    override fun onStop() {
        super.onStop()
        GlobalBus.getBus().unregister(this)
    }
}