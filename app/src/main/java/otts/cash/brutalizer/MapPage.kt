package otts.cash.brutalizer

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import otts.cash.brutalizer.view.MapContainer
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

const val REQUEST_CODE_LOCATION = 123

class MapPage : Fragment() {

    companion object {
        const val TAG = "MapPage"
        fun newInstance(): MapPage {
            return MapPage()
        }
    }

    private lateinit var mMap: GoogleMap
    private lateinit var mMapContainer: MapContainer

    private val mOnMapReadyListener = object: MySupportMapFragment.OnMapReadyListener {
        override fun onMapReady(map: GoogleMap?) {
            mMap = map ?: return
            enableMyLocation()
            with (mMap.uiSettings) {
                isMyLocationButtonEnabled = false
                isCompassEnabled = false
                isRotateGesturesEnabled = false
                setAllGesturesEnabled(false)
                isScrollGesturesEnabled = true
                isZoomGesturesEnabled = true
            }
            mMapContainer.initMap(mMap)
        }
    }

    /**
     * enableMyLocation() will enable the location of the map if the user has given permission
     * for the app to access their device location.
     * Android Studio requires an explicit check before setting map.isMyLocationEnabled to true
     * but we are using EasyPermissions to handle it so we can suppress the "MissingPermission"
     * check.
     */
    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun enableMyLocation() {
        if (hasLocationPermission()) {
            mMap.isMyLocationEnabled = true
        } else {
            EasyPermissions.requestPermissions(activity!!, "We need location permission to provide you more specific information",
                REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.page_map, container, false)
        val mapFragment: MySupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as MySupportMapFragment?
        mapFragment?.setOnMapReadyListener(mOnMapReadyListener)
        mMapContainer = view as MapContainer
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setActionBar(R.string.title_map)
        (activity as MainActivity).setNavigation(true)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}