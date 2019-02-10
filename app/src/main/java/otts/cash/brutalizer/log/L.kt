package otts.cash.brutalizer.log

import android.util.Log

class L {
    companion object {
        private const val TAG = "Brutalizer"
        fun d(tag: String = TAG, msg: String) {
            Log.d(tag, msg)
        }

        fun i(tag: String = TAG, msg: String) {
            Log.i(tag, msg)
        }

        fun w(tag: String = TAG, msg: String) {
            Log.w(tag, msg)
        }

        fun e(tag: String = TAG, msg: String) {
            Log.e(tag, msg)
        }
    }
}