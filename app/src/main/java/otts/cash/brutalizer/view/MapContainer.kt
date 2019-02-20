package otts.cash.brutalizer.view

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MapContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    /**
     * Reference to a GoogleMap object
     */
    private lateinit var mMap: GoogleMap

    /**
     * A currently selected marker
     */
    private var mMarker: Marker? = null

    /**
     * Our custom view which is returned from either the InfoWindowAdapter.getInfoContents
     * or InfoWindowAdapter.getInfoWindow
     */
    private var mInfoWindow: View? = null

    fun initMap(map: GoogleMap) {
        mMap = map
    }

    fun setMarkerWithInfoWindow(marker: Marker, infoWindow: View) {
        mMarker = marker
        mInfoWindow = infoWindow
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        var result = false

        // Make sure that the infoWindow is shown and we have all the needed references
        if (mMarker != null && mMarker!!.isInfoWindowShown && mInfoWindow != null) {
            // Get a marker position on the screen
            val point: Point = mMap.projection.toScreenLocation(mMarker!!.position)

            // Make a copy of the MotionEvent and adjust it's location
            // so it is relative to the infoWindow left top corner
            val copyEv: MotionEvent = MotionEvent.obtain(ev)
            copyEv.offsetLocation(
                -point.x + (mInfoWindow!!.width / 2f),
                -point.y + mInfoWindow!!.height / 1f)
            // Dispatch the adjusted MotionEvent to the infoWindow
            result = mInfoWindow!!.dispatchTouchEvent(copyEv)
            copyEv.recycle()
        }
        // If the infoWindow consumed the touch event, then just return true.
        // Otherwise pass this event to the super class and return it's result
        return result || super.dispatchTouchEvent(ev)
    }


}