package livs.code.frontoffice.data.remote.respons

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PecahanUang(
    var seratus_ribu: Int = 0,
    var lima_puluh_ribu: Int = 0,
    var dua_puluh_ribu: Int = 0,
    var sepuluh_ribu: Int = 0,
    var lima_ribu: Int = 0,
    var dua_ribu: Int = 0,
    var seribu: Int = 0,
    var lima_ratus: Int = 0,
    var dua_ratus: Int = 0,
    var seratus: Int = 0,
    var lima_puluh: Int = 0,
    var dua_lima: Int = 0
)