package com.app.daintybox.libs

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.SystemClock
import androidx.appcompat.app.AlertDialog

open class UtilsDainty {


    companion object {
        var progressDialog: ProgressDialog? = null
        private var mLastClickTime: Long = 0
        var progressDialogDg: ProgressDialog? = null

        fun isFirstClick(): Boolean {
            val flag_click: Boolean

            // mis-clicking prevention, using threshold of 1000 ms
            flag_click = SystemClock.elapsedRealtime() - mLastClickTime >= 1000
            mLastClickTime = SystemClock.elapsedRealtime()
            return flag_click
        }

        fun reset_click_time() {
            mLastClickTime = 0
        }

        /**
         *
         * Mostrar un loading
         *
         * @param activity actividad actual
         * @param msg mensaje que ira en el loading
         */
        fun show_loading_dialog(activity: Activity?, msg: String?) {

            // Oculto cualquier otro loading que haya
            progressDialog?.dismiss()

            progressDialog = ProgressDialog(activity)
            progressDialog!!.setMessage(msg)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }

        fun hide_loading_dialog() {
            if (progressDialog != null) {
                try {
                    progressDialog!!.dismiss()
                } catch (e: IllegalArgumentException) {
                    //Crashlytics.logException(e);
                }
            }
        }


        fun show_accept_msg(
            context: Context?,
            msg: String?,
            callbackOK: DialogInterface.OnClickListener?
        ) {
            AlertDialog.Builder(context!!).setMessage(msg).setPositiveButton("Aceptar", callbackOK)
                .show()
        }

        fun show_accept_msg(context: Context?, msg: String?) {
            AlertDialog.Builder(context!!).setMessage(msg).setPositiveButton("Aceptar", null).show()
        }

        /**
         * This method converts dp unit to equivalent pixels, depending on device density.
         *
         * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
         * @param context Context to get resources and device specific display metrics
         * @return A float value to represent px equivalent to dp depending on device density
         */
        fun convertDpToPixel(dp: Int, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi / 160f)
        }

        /**
         * This method converts device specific pixels to density independent pixels.
         *
         * @param px A value in px (pixels) unit. Which we need to convert into db
         * @param context Context to get resources and device specific display metrics
         * @return A float value to represent dp equivalent to px value
         */
        fun convertPixelsToDp(px: Float, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return px / (metrics.densityDpi / 160f)
        }

    }
}