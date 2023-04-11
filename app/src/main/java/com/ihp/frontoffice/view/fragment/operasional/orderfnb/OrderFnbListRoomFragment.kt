package com.ihp.frontoffice.view.fragment.operasional.orderfnb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ihp.frontoffice.MyApp
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.databinding.FragmentOrderFnbListRoomBinding
import com.ihp.frontoffice.view.fragment.operasional.OperasionalFragmentDirections
import com.ihp.frontoffice.viewmodel.RoomViewModel
import es.dmoral.toasty.Toasty

class OrderFnbListRoomFragment : Fragment() {

    private var _binding: FragmentOrderFnbListRoomBinding?=null
    private val binding get() = _binding!!
    private lateinit var BASE_URL: String
    private lateinit var USER_FO: User
    private lateinit var roomViewModel: RoomViewModel
    private val listRoomAdapter = ListRoomAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentOrderFnbListRoomBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BASE_URL = (requireActivity().applicationContext as MyApp).baseUrl
        USER_FO = (requireActivity().applicationContext as MyApp).userFo
        roomViewModel = ViewModelProvider(requireActivity()).get(RoomViewModel::class.java)
        roomViewModel.init(BASE_URL)
        getRoomCheckin()

        binding.fabScan.setOnClickListener {
            Navigation.findNavController(view)
                    .navigate(
                            OrderFnbListRoomFragmentDirections
                                    .actionOrderFnbListRoomFragmentToOrderFnbRoomFragment())
        }

        binding.swipe.setOnRefreshListener {
            getRoomCheckin()
            binding.swipe.isRefreshing = false
        }
        with(binding.rvListRoom) {
            layoutManager = GridLayoutManager(requireActivity(), 3)
            setHasFixedSize(true)
            adapter = listRoomAdapter
        }
    }

    fun getRoomCheckin(){
        roomViewModel.getRoomCheckin("").observe(viewLifecycleOwner,{data->
            if(data.isOkay){
                listRoomAdapter.setData(data.rooms)
            }else{
                Toasty.error(requireActivity(), data.message.toString(), Toasty.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}