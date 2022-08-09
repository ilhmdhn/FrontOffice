package com.ihp.frontoffice.view.fragment.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.data.remote.respons.DataRoomCall
import com.ihp.frontoffice.databinding.FragmentNotificationBinding
import com.ihp.frontoffice.events.EventsWrapper.TitleFragment
import com.ihp.frontoffice.events.GlobalBus
import com.ihp.frontoffice.view.LoginActivity

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationViewModel: NotificationViewModel
    private val notificationAdapter = NotificationAdapter()

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
        notificationViewModel = ViewModelProvider(requireActivity()).get(NotificationViewModel::class.java)

        val url = (requireActivity().applicationContext as MyApp).baseUrl

        binding.lySwipe.setOnRefreshListener {
            getData(url)
            binding.lySwipe.isRefreshing = false
        }

        getData(url)
    }

    private fun getData(url: String){
        binding.notifyProgressbar.visibility = View.VISIBLE
        notificationViewModel.getCallRoom(url).observe(viewLifecycleOwner, {data ->
            if (data.state == false){
                Toast.makeText(requireActivity(), data.message.toString(), Toast.LENGTH_SHORT).show()
                binding.notifyRecyclerview.visibility = View.GONE
            } else{
                if (data.data.isNullOrEmpty()){
                    binding.notifyRecyclerview.visibility = View.GONE
                    Toast.makeText(requireActivity(), "Data Kosong", Toast.LENGTH_SHORT).show()
                }else{
                    notificationAdapter.setData(data.data as List<DataRoomCall>)
                    with(binding.notifyRecyclerview){
                        binding.notifyRecyclerview.visibility = View.VISIBLE
                        layoutManager = LinearLayoutManager(requireActivity())
                        setHasFixedSize(true)
                        adapter = notificationAdapter
                    }
                }
            }
            binding.notifyProgressbar.visibility = View.GONE
        })
    }

    private fun setMainTitle() {
        GlobalBus
            .getBus()
            .post(TitleFragment("Notifikasi"))
    }

    override fun onStart() {
        super.onStart()
//        GlobalBus.getBus().register(this)
    }

    override fun onStop() {
        super.onStop()
        GlobalBus.getBus().unregister(this)
    }
}