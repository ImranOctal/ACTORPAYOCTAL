package com.octal.actorpay.database.prefrence

import android.content.Context
import android.content.SharedPreferences

/*
* Copyright (c) Ishant Sharma
* Android Developer
*/
class SharedPre(context: Context) {
    var mContext: Context
    var isSavedAd: Boolean
        get() = GetDataBoolean(IS_ADD_MOB_SAVED)
        set(value) {
            SetDataBoolean(IS_ADD_MOB_SAVED, value)
        }
    var isLoggedIn: Boolean
        get() = GetDataBoolean(IS_LOGGED_IN)
        set(value) {
            SetDataBoolean(IS_LOGGED_IN, value)
        }
    var isGoogleLoggedIn: Boolean
        get() = GetDataBoolean(IS_LOGGED_IN_VIA_GOOGLE)
        set(value) {
            SetDataBoolean(IS_LOGGED_IN_VIA_GOOGLE, value)
        }

    var isFacebookLoggedIn: Boolean
        get() = GetDataBoolean(IS_LOGGED_IN_VIA_FACEBOOK)
        set(value) {
            SetDataBoolean(IS_LOGGED_IN_VIA_FACEBOOK, value)
        }


    fun setEmailLoggedIn(value: Boolean) {
        SetDataBoolean(IS_LOGGED_IN_VIA_EMAIL, value)
    }

    fun isEmailLogged(): Boolean {
        return GetDataBoolean(IS_LOGGED_IN_VIA_EMAIL)
    }

    fun isRegister(): Boolean {
        return GetDataBoolean(IS_REGISTER)
    }

    fun setIsregister(value: Boolean) {
        SetDataBoolean(IS_REGISTER, value)
    }

    fun isLogout(): Boolean {
        return GetDataBoolean(Is_LOGOUT)
    }

    fun setIsLogout(value: Boolean) {
        SetDataBoolean(Is_LOGOUT, value)
    }

    fun setIsAppBackground(b: Boolean) {
        SetDataBoolean(APP_BACKGROUND, b)
    }

    fun IsAppBackgrounded(): Boolean {
        return GetDataBoolean(APP_BACKGROUND)
    }

    fun setUserId(uid: String) {
        SetDataString(USER_ID, uid)
    }

    val userId: String?
        get() = GetDataString(USER_ID)

    fun setUserMobile(userMobile: String) {
        SetDataString(MOBILE_NO, userMobile)
    }

    val userMobile: String?
        get() = GetDataString(MOBILE_NO)

    fun setName(name: String) {
        SetDataString(NAME, name)
    }

    val name: String?
        get() = GetDataString(NAME)

    fun setClientId(id: String) {
        SetDataString(CLIENT_ID, id)
    }

    val clientId: String?
        get() = GetDataString(CLIENT_ID)

    fun setFaceBookAccessToken(accessToken: String) {
        SetDataString(FB_ACCESS_TOKEN, accessToken)
    }

    val faceBookAccessToken: String?
        get() = GetDataString(FB_ACCESS_TOKEN)

    fun setEmailProfile(googleProfile: String) {
        SetDataString(OUR_PROFILE, googleProfile)
    }

    val emailProfile: String?
        get() = GetDataString(OUR_PROFILE)

    fun setClientProfile(profileClient: String) {
        SetDataString(CLIENT_PROFILE, profileClient)
    }

    val clientProfile: String?
        get() = GetDataString(CLIENT_PROFILE)

    fun setUserEmail(email: String) {
        SetDataString(EMAIL, email)
    }

    val userEmail: String?
        get() = GetDataString(EMAIL)
    val firebaseDeviceToken: String?
        get() = GetDataString(FIREBASE_TOKEN)

    fun setFirebaseToken(token: String) {
        SetDataString(FIREBASE_TOKEN, token)
    }

    var isNotificationMuted: Boolean
        get() = GetDataBoolean(NOTIFICATION_MUTED)
        set(notificationMuted) {
            SetDataBoolean(NOTIFICATION_MUTED, notificationMuted)
        }
    val notificationSound: String?
        get() = GetDataString(RINGTON_PATH)

    fun setNotificationSound(uri: String) {
        SetDataString(RINGTON_PATH, uri)
    }

    fun setJwtToken(points: String) {
        SetDataString(JWT_TOKEN, points)
    }

    fun setRefreshToken(points: String) {
        SetDataString(REFRESH_TOKEN, points)
    }

    fun setTokenType(points: String) {
        SetDataString(TOKEN_TYPE, points)
    }


    val jwtToken: String?
        get() = GetDataString(JWT_TOKEN)
    val refreshToken: String?
        get() = GetDataString(REFRESH_TOKEN)
    val tokenType: String?
        get() = GetDataString(TOKEN_TYPE)

    fun setFont(points: Float) {
        SetDataFloat(FONT, points)
    }

    val GetFont: Float?
        get() = GetDataFloat(FONT)


    fun setTheme(theme: String) {
        SetDataString(THEMES, theme)

    }

    val GetCurrentTheme: String?
        get() = GetDataString(THEMES)

    var isDarkModeEnable: Boolean
        get() = GetDataBoolean(DARK_MODE)
        set(darkmode) {
            SetDataBoolean(DARK_MODE, darkmode)
        }
    var isFontApplied: Boolean
        get() = GetDataBoolean(ISFONTAPPLICABLE)
        set(fontApplied) {
            SetDataBoolean(ISFONTAPPLICABLE, fontApplied)
        }
    var isPlanActivated: Boolean
        get() = GetDataBoolean(ISPLANACTIVATED)
        set(plan) {
            SetDataBoolean(ISPLANACTIVATED, plan)
        }
    var PlanName: String
        get() = GetDataString(PLAN_NAME) ?: "No Plan Available"
        set(planname) {
            SetDataString(PLAN_NAME, planname)
        }

    //--------------------------------------Boolean Values--------------------------------------------
    //------------------------------------------------------------------------------------------------
    private fun GetDataString(key: String): String? {
        var cbValue: String? = null
        try {
            cbValue = getSharedPreferences(mContext).getString(key, "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cbValue
    }

    private fun GetDataStringZero(key: String): String? {
        var cbValue: String? = null
        try {
            cbValue = getSharedPreferences(mContext).getString(key, "0.0")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cbValue
    }

    private fun SetDataString(key: String, value: String) {
        val edit = getSharedPreferences(mContext).edit()
        edit.putString(key, value)
        edit.commit()
    }

    private fun GetDataInt(key: String): Int {
        return getSharedPreferences(mContext).getInt(key, 0)
    }

    private fun GetDataFloat(key: String): Float {
        return getSharedPreferences(mContext).getFloat(key, 0.0f)
    }

    private fun SetDataFloat(key: String, value: Float) {
        val edit = getSharedPreferences(mContext).edit()
        edit.putFloat(key, value)
        edit.commit()
    }

    private fun SetDataInt(key: String, value: Int) {
        val edit = getSharedPreferences(mContext).edit()
        edit.putInt(key, value)
        edit.commit()
    }

    private fun GetDataLong(key: String): Long {
        return getSharedPreferences(mContext).getLong(key, 0)
    }

    private fun SetDataLong(key: String, value: Long) {
        val sp = getSharedPreferences(mContext)
        val edit = sp.edit()
        edit.putLong(key, value)
        edit.commit()
    }

    private fun GetDataBoolean(key: String): Boolean {
        val cbValue = getSharedPreferences(mContext).getBoolean(key, false)
        return cbValue
    }

    private fun SetDataBoolean(key: String, value: Boolean) {
        val edit = getSharedPreferences(mContext).edit()
        edit.putBoolean(key, value)
        edit.commit()
    }

    fun Logout() {

        getSharedPreferences(mContext).edit().clear().commit()
        LogoutPrefrences()
    }

    private fun LogoutPrefrences() {
        removePreferences(NAME, mContext)
        removePreferences(EMAIL, mContext)
        removePreferences(OUR_PROFILE, mContext)
        removePreferences(CLIENT_PROFILE, mContext)
        removePreferences(CLIENT_ID, mContext)
        removePreferences(MOBILE_NO, mContext)
        removePreferences(IS_ADD_MOB_SAVED, mContext)
        removePreferences(IS_LOGGED_IN, mContext)
        removePreferences(IS_REGISTER, mContext)
        removePreferences(USER_ID, mContext)
        removePreferences(FIREBASE_TOKEN, mContext)
        removePreferences(RINGTON_PATH, mContext)
        removePreferences(NOTIFICATION_MUTED, mContext)
        removePreferences(IS_LOGGED_IN_VIA_EMAIL, mContext)
        removePreferences(IS_LOGGED_IN_VIA_GOOGLE, mContext)
        removePreferences(IS_LOGGED_IN_VIA_FACEBOOK, mContext)
        setIsLogout(true)
    }

    companion object {
        private const val ITI = "Sample"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val OUR_PROFILE = "myProfilePicture"
        private const val CLIENT_PROFILE = "profile"
        private const val CLIENT_ID = "clientId"
        private const val MOBILE_NO = "mobile_no"
        private const val APP_BACKGROUND = "app_in_background"
        private const val IS_ADD_MOB_SAVED = "isSvedAddMobData"
        private const val IS_LOGGED_IN = "login"
        private const val IS_REGISTER = "register"
        private const val USER_ID = "userId"
        private const val FIREBASE_TOKEN = "firebaseToken"
        private const val RINGTON_PATH = "rington"
        private const val NOTIFICATION_MUTED = "notification_muted"
        private const val IS_LOGGED_IN_VIA_EMAIL = "emailLoggedin"
        private const val IS_LOGGED_IN_VIA_GOOGLE = "googleLoggedIn"
        private const val IS_LOGGED_IN_VIA_FACEBOOK = "facebookLoggedin"
        private const val FB_ACCESS_TOKEN = "facebookToken"
        private const val JWT_TOKEN = "jwt"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val TOKEN_TYPE = "token_type"
        private const val Is_LOGOUT = "logout"
        private const val THEMES = "themePref"
        private const val DARK_MODE = "darkmoded"
        private const val FONT = "fontsize"
        private const val ISFONTAPPLICABLE = "fontApplied"
        private const val ISPLANACTIVATED = "planActivated"
        private const val PLAN_NAME = "planName"
        private var Instance: SharedPre? = null

        @Synchronized
        fun getInstance(context: Context): SharedPre? {
            if (Instance == null) {
                Instance = SharedPre(context)
            }
            return Instance
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(ITI, Context.MODE_PRIVATE)
        }

        private fun removePreferences(key: String, cntxt: Context) {
            getSharedPreferences(cntxt).edit().remove(key).commit()
        }
    }

    init {
        if (Instance != null) {
            throw RuntimeException("Use getInstance() method to get the single instance of this class( Mr.professional - Ishant ).")
        }
        mContext = context
    }
}