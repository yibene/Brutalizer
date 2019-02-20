package otts.cash.brutalizer

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

class MySupportMapFragment : SupportMapFragment() {

    interface OnMapReadyListener {
        fun onMapReady(map: GoogleMap?)
    }

    private lateinit var mOnMapReadyListener: OnMapReadyListener

    fun setOnMapReadyListener(onMapReadyListener: OnMapReadyListener) {
        mOnMapReadyListener = onMapReadyListener
    }

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        this.getMapAsync { map -> mOnMapReadyListener.onMapReady(map) }
    }

}