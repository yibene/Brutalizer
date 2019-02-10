package otts.cash.brutalizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class MyPlanPage : Fragment() {

    companion object {
        const val TAG = "MyPlanPage"
        fun newInstance(): MyPlanPage {
            return MyPlanPage()
        }
        val DICE_IMAGE: IntArray = intArrayOf(
            R.drawable.dice_1,
            R.drawable.dice_2,
            R.drawable.dice_3,
            R.drawable.dice_4,
            R.drawable.dice_5,
            R.drawable.dice_6
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.page_my_plan, container, false)
        val diceImage = v.findViewById<ImageView>(R.id.dice_image)
        val rollButton = v.findViewById<Button>(R.id.roll)
        rollButton.setOnClickListener {
            diceImage.setImageResource(DICE_IMAGE[Random.nextInt(6)])
        }
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setActionBar(R.string.title_my_plan)
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