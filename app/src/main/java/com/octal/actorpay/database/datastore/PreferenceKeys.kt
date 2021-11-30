package com.octal.actorpay.database.datastore
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */
import androidx.datastore.preferences.core.*

object PreferenceKeys {
    //Demo Keyt Genration
    val BOOLEAN_KEY : Preferences.Key<Boolean> = booleanPreferencesKey("boolean_key")
    val STRING_KEY : Preferences.Key<String> = stringPreferencesKey("string_key")
    val INTEGER_KEY : Preferences.Key<Int> = intPreferencesKey("integer_key")
    val DOUBLE_KEY : Preferences.Key<Double> = doublePreferencesKey("double_key")
    val LONG_KEY : Preferences.Key<Long> = longPreferencesKey("long_key")
    //val LIST_MODEL_KEY : Preferences.Key<List<CustomModel>> = preferencesKey<List<CustomModel>>("list_model_key")



    val IS_APP_LOGGED_IN : Preferences.Key<Boolean> = booleanPreferencesKey("app_logged_in")
    val FIRST_NAME : Preferences.Key<String> = stringPreferencesKey("first_name")
    val LAST_NAME : Preferences.Key<String> = stringPreferencesKey("last_name")
    val USERID : Preferences.Key<String> = stringPreferencesKey("userId")
    val EMAIL : Preferences.Key<String> = stringPreferencesKey("email")
    val MOBILE : Preferences.Key<String> = stringPreferencesKey("phone")
    val COUNTRY_CODE : Preferences.Key<String> = stringPreferencesKey("countryCode")
    val ACCESS_TOKEN : Preferences.Key<String> = stringPreferencesKey("access_token")
    val REFRESH_TOKEN : Preferences.Key<String> = stringPreferencesKey("refresh_token")
}