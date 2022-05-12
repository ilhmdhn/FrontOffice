package livs.code.frontoffice.view.fragment.reporting.kas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import livs.code.frontoffice.data.remote.respons.DataStatusKas
import livs.code.frontoffice.view.fragment.reporting.kas.pembayaran.ReportKasPembayaranFragment
import livs.code.frontoffice.view.fragment.reporting.kas.penjualan.ReportKasPenjualanFragment
import livs.code.frontoffice.view.fragment.reporting.kas.statuskamar.ReportKasStatusKamarFragment

class ReportKasSectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    var tanggal: String = ""
    var shift: String = ""
    var username: String = ""
    var data = DataStatusKas()

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> {
                fragment = ReportKasPembayaranFragment()
                fragment.arguments = Bundle().apply {
                    putString(ReportKasPembayaranFragment.DATA_TANGGAL, tanggal)
                    putString(ReportKasPembayaranFragment.DATA_SHIFT, shift)
                    putString(ReportKasPembayaranFragment.DATA_USERNAME, username)
                }
            }
            1 -> {
                fragment = ReportKasPenjualanFragment()
                fragment.arguments = Bundle().apply {
                    putString(ReportKasPembayaranFragment.DATA_TANGGAL, tanggal)
                    putString(ReportKasPembayaranFragment.DATA_SHIFT, shift)
                    putString(ReportKasPembayaranFragment.DATA_USERNAME, username)                }
            }
            2 -> {
                fragment = ReportKasStatusKamarFragment()
                fragment.arguments = Bundle().apply {
                    putString(ReportKasPembayaranFragment.DATA_TANGGAL, tanggal)
                    putString(ReportKasPembayaranFragment.DATA_SHIFT, shift)
                    putString(ReportKasPembayaranFragment.DATA_USERNAME, username)                }
            }
        }
        return fragment as Fragment
    }
}