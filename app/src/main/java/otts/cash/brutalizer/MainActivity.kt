package otts.cash.brutalizer

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import otts.cash.brutalizer.log.L
import otts.cash.brutalizer.notification.NotificationPage

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }

    private var mActionBarTitle: AppCompatTextView? = null
    private var mNavigation: BottomNavigationView? = null
    private var mNotificationItem: MenuItem? = null

    private var mPermissionRequestDialog: AlertDialog? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_my_plan -> {
                addFragment(MyPlanPage.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                addFragment(MapPage.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_info -> {
                addFragment(InfoPage.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                addFragment(SettingsPage.newInstance())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment, stack: Boolean = false) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (stack) transaction.addToBackStack(fragment.tag)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mActionBarTitle = findViewById(R.id.toolbar_title)  // customized title
        setSupportActionBar(findViewById(R.id.toolbar))

        // hide the title toolbar built-in
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mNavigation = findViewById(R.id.navigation)
        mNavigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        addFragment(MyPlanPage.newInstance())

        checkAndRequestPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tool, menu)
        mNotificationItem = menu.findItem(R.id.action_notification)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_notification -> {
            addFragment(NotificationPage.newInstance(), true)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (mPermissionRequestDialog == null || !mPermissionRequestDialog!!.isShowing)
            checkAndRequestPermissions()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        val listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (!grantResults.isEmpty()) {
                    for (i in permissions.indices) {
                        perms[permissions[i]] = grantResults[i]
                    }
                    if (perms[Manifest.permission.ACCESS_FINE_LOCATION] != PackageManager.PERMISSION_GRANTED
                        || perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] != PackageManager.PERMISSION_GRANTED
                        || perms[Manifest.permission.READ_EXTERNAL_STORAGE] != PackageManager.PERMISSION_GRANTED
                    ) {
                        L.d(TAG, "Some permissions are not granted ask again ")
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        //                        // shouldShowRequestPermissionRationale will return true
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                            || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            showRequestPermissionDialog("Service Permissions are required for this app",
                                DialogInterface.OnClickListener { _, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE ->
                                            // proceed with logic by disabling the related features or quit the app.
                                            finish()
                                    }
                                })
                        } else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?")
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }//permission is denied (and never ask again is checked)
                        //shouldShowRequestPermissionRationale will return false
                    }
                }
            }
        }
    }

    private fun showRequestPermissionDialog(message: String, okListener: DialogInterface.OnClickListener) {
        mPermissionRequestDialog = AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, okListener)
            .setNegativeButton(android.R.string.cancel, okListener)
            .create()
        mPermissionRequestDialog?.show()
    }

    private fun explain(msg: String) {
        mPermissionRequestDialog = AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                //  permissionsClass.requestPermission(type,code);
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:otts.cash.brutalizer")
                    )
                )
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> finish() }
            .create()
        mPermissionRequestDialog?.show()
    }

    override fun onPause() {
        super.onPause()
    }

    fun setActionBar(titleRes: Int, backMode: Boolean = false) {
        mActionBarTitle?.setText(titleRes)
        supportActionBar!!.setDisplayHomeAsUpEnabled(backMode)
        mNotificationItem?.isVisible = !backMode
    }

    fun setNavigation(enable: Boolean) {
        mNavigation?.visibility = if (enable) View.VISIBLE else View.GONE
    }

}
