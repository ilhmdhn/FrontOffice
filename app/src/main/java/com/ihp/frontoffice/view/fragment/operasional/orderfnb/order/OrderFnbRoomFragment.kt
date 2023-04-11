package com.ihp.frontoffice.view.fragment.operasional.orderfnb.order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.databinding.FragmentOrderFnbListRoomBinding
import com.ihp.frontoffice.databinding.FragmentOrderFnbRoomBinding
import com.ihp.frontoffice.viewmodel.OtherViewModel
import com.ihp.frontoffice.viewmodel.RoomViewModel


class OrderFnbRoomFragment : Fragment() {

    private var _binding: FragmentOrderFnbRoomBinding? = null
    private val binding get() = _binding!!

    private lateinit var BASE_URL: String
    private lateinit var USER_FO: User

    private lateinit var otherViewModel: OtherViewModel
    val fnbPagingAdapter = FnbPagingAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentOrderFnbRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BASE_URL = (requireActivity().applicationContext as MyApp).baseUrl
        USER_FO = (requireActivity().applicationContext as MyApp).userFo
        otherViewModel = ViewModelProvider(requireActivity()).get(OtherViewModel::class.java)

        otherViewModel.fnbPaging(BASE_URL).observe(requireActivity(), {data->
            fnbPagingAdapter.submitData( lifecycle,data)
        })

        with(binding.rvFnb){
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = fnbPagingAdapter
            setHasFixedSize(true)
        }
    }
}