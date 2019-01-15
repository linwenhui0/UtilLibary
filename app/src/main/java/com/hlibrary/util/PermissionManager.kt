package com.hlibrary.util

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.util.Log


class PermissionManager {

    private var activity: Activity?

    var permissionGrant: PermissionGrant? = null

    companion object {
        private const val TAG = "PermissionUtils"
        private const val CODE_MULTI_PERMISSION = 100


        private fun shouldShowRationale(activity: Activity, requestCode: Int, requestPermission: String) {
            //TODO
//            val permissionsHint = activity.getResources().getString(R.string.permission_hint, requestPermission.replace("android.permission.", ""))
//            showMessageOKCancel(activity, permissionsHint, DialogInterface.OnClickListener { dialog, which ->
            ActivityCompat.requestPermissions(activity,
                    arrayOf(requestPermission),
                    requestCode)
            Logger.getInstance().d(TAG, "showMessageOKCancel requestPermissions:$requestPermission")
//            })
        }

        private fun showMessageOKCancel(context: Activity, message: String, okListener: DialogInterface.OnClickListener, positiveButtonText: String = "OK") {
            AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(positiveButtonText, okListener)
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show()
        }

        private fun openSettingActivity(activity: Activity, message: String) {

            showMessageOKCancel(activity, message, DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                Log.d(TAG, "getPackageName(): " + activity.packageName)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }, "去设置手动开启")
        }


        /**
         * @param activity
         * @param isShouldRationale true: return no granted and shouldShowRequestPermissionRationale permissions, false:return no granted and !shouldShowRequestPermissionRationale
         * @return
         */
        private fun getNoGrantedPermission(activity: Activity, permissions: Array<String>, isShouldRationale: Boolean): ArrayList<String>? {

            val noGrantPermissions = ArrayList<String>()
            permissions.forEach {
                //TODO checkSelfPermission
                var checkSelfPermission = -1
                try {
                    checkSelfPermission = ActivityCompat.checkSelfPermission(activity, it)
                } catch (e: RuntimeException) {
                    Logger.getInstance().e(TAG, "RuntimeException:" + e.message)
                    return null
                }

                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                    Logger.getInstance().i(TAG, "getNoGrantedPermission ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED:$it")
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                        Logger.getInstance().d(TAG, "shouldShowRequestPermissionRationale if")
                        if (isShouldRationale) {
                            noGrantPermissions.add(it)
                        }
                    } else {
                        if (!isShouldRationale) {
                            noGrantPermissions.add(it)
                        }
                        Logger.getInstance().d(TAG, "shouldShowRequestPermissionRationale else")
                    }

                }
            }

            return noGrantPermissions
        }

    }

    constructor(activity: Activity, permissionGrant: PermissionGrant) {
        this.activity = activity
        this.permissionGrant = permissionGrant
    }


    /**
     * Requests permission.
     *
     * @param activity
     * @param requestCode request code, e.g. if you need request CAMERA permission,parameters is PermissionUtils.CODE_CAMERA
     */
    fun requestPermission(requestCode: Int, permission: String) {

        if (activity == null) {
            return
        }

        Logger.getInstance().i(TAG, "requestPermission requestCode:$requestCode permission:$permission")
        if (requestCode < 0) {
            Logger.getInstance().w(TAG, "requestPermission illegal requestCode:$requestCode")
            permissionGrant?.onPermissionError(Exception("requestPermission illegal requestCode:$requestCode"))
            return
        }

        val checkSelfPermission: Int
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity!!, permission)
        } catch (e: RuntimeException) {
            Logger.getInstance().e(TAG, "RuntimeException:" + e.message)
            permissionGrant?.onPermissionError(e)
            return
        }

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            Logger.getInstance().i(TAG, "ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED")
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {
                Logger.getInstance().i(TAG, "requestPermission shouldShowRequestPermissionRationale")
                shouldShowRationale(activity!!, requestCode, permission)
            } else {
                Logger.getInstance().d(TAG, "requestCameraPermission else")
                ActivityCompat.requestPermissions(activity!!, arrayOf(permission), requestCode)
            }

        } else {
            Logger.getInstance().d(TAG, "ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED")
            permissionGrant?.onPermissionGranted(permission)
        }
    }

    /**
     * 一次申请多个权限
     */
    fun requestMultiPermissions(permissions: Array<String>) {

        val permissionsList = getNoGrantedPermission(activity!!, permissions, false)
        val shouldRationalePermissionsList = getNoGrantedPermission(activity!!, permissions, true)

        //TODO checkSelfPermission
        if (permissionsList == null || shouldRationalePermissionsList == null) {
            permissionGrant?.onPermissionError(Exception("Please CheckSelPermission!"))
            return
        }
        Logger.getInstance().d(TAG, "requestMultiPermissions permissionsList:${permissionsList.size} ,shouldRationalePermissionsList:${shouldRationalePermissionsList.size}")

        if (permissionsList.size > 0) {
            ActivityCompat.requestPermissions(activity!!, permissionsList.toTypedArray(),
                    CODE_MULTI_PERMISSION)
            Logger.getInstance().d(TAG, "showMessageOKCancel requestPermissions")
        } else if (shouldRationalePermissionsList.size > 0) {
//            showMessageOKCancel(activity!!, "should open those permission",
//                    DialogInterface.OnClickListener { dialog, which ->
            ActivityCompat.requestPermissions(activity!!, shouldRationalePermissionsList.toTypedArray(),
                    CODE_MULTI_PERMISSION)
            Logger.getInstance().d(TAG, "showMessageOKCancel requestPermissions")
//                    })
        } else {
            permissionGrant?.onPermissionGranted(CODE_MULTI_PERMISSION.toString())
        }

    }

    fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (activity == null) {
            return
        }
        Logger.getInstance().d(TAG, "requestPermissionsResult permission:", permissions, ",grantResults:", grantResults)
        if (permissions.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Logger.getInstance().i(TAG, "onRequestPermissionsResult PERMISSION_GRANTED")
            //TODO success, do something, can use callback
            permissionGrant?.onPermissionGranted(permissions[0])
        } else {

//            var permissionError = StringBuilder()
            var permissionErrors = ArrayList<String>()
            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                    if (permissionError.count() > 0) {
//                        permissionError.append("\n")
//                    }
                    permissionErrors.add(permissions[i].replace("android.permission.", ""))
//                    permissionError.append(activity?.resources?.getString(R.string.permission_hint, permissions[i].replace("android.permission.", "")))
                }
            }
            //TODO hint user this permission function
            Logger.getInstance().i(TAG, "onRequestPermissionsResult permissionErrors ", permissionErrors)
            if (permissionErrors.isEmpty())
                permissionGrant?.onPermissionGranted(requestCode.toString())
            else {
                permissionGrant?.onPermissionDenied(permissionErrors)
//                openSettingActivity(activity!!, permissionError.toString())
            }

        }
    }

}

interface PermissionGrant {
    fun onPermissionGranted(permission: String)
    fun onPermissionDenied(permissions: ArrayList<String>)
    fun onPermissionError(e: Exception)
}