package otts.cash.brutalizer.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import otts.cash.brutalizer.MainActivity
import otts.cash.brutalizer.R

class NotificationPage : Fragment() {

    companion object {
        const val TAG = "NotificationPage"
        fun newInstance(): NotificationPage {
            return NotificationPage()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_notification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setActionBar(R.string.title_notifications, true)
        (activity as MainActivity).setNavigation(false)
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