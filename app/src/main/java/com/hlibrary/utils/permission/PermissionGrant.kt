package com.hlibrary.utils.permission

interface PermissionGrant {
    fun onPermissionGranted(permission: String)
    fun onPermissionDenied(permissions: ArrayList<String>)
    fun onPermissionError(e: Exception)
}