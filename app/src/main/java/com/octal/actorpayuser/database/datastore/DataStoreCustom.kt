package com.octal.actorpayuser.database.datastore
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* */
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.octal.actorpayuser.database.datastore.PreferenceKeys.IS_APP_INTRO
import com.octal.actorpayuser.database.datastore.PreferenceKeys.IS_APP_LOGGED_IN
import com.octal.actorpayuser.database.datastore.PreferenceKeys.NOTIFICATION_MUTED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreCustom(val context:Context/*private val dataStore: DataStore<Preferences>*/) :DataStoreBase {
    private  val USER_PREFERENCES_NAME = "ActorPayDatastore"
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    override fun giveRepository() : String {
        return this.toString()
    }

    override suspend fun logOut() {
        context.dataStore.edit {preference ->
           preference.clear()
        }
    }
    override suspend fun setPhoneNumber(mobileNumber: String) {
        context.dataStore.edit { preference ->
            preference.set(PreferenceKeys.MOBILE, mobileNumber)
        }
    }

    override suspend fun setCountryCode(countryCode: String) {
        context.dataStore.edit { preference ->
            preference.set(PreferenceKeys.COUNTRY_CODE, countryCode)
        }
    }

    override suspend fun setFirstName(name: String) {
        context.dataStore.edit { preference ->
            preference.set(PreferenceKeys.FIRST_NAME, name)
        }
    }

    override suspend fun setLastName(name: String) {
        context.dataStore.edit { preference ->
            preference.set(PreferenceKeys.LAST_NAME, name)
        }
    }

    override suspend fun setUserId(userId: String) {
        context.dataStore.edit { preferences -> preferences.set(PreferenceKeys.USERID,userId) }
    }

    override suspend fun setEmail(email: String) {
        context.dataStore.edit { preferences -> preferences.set(PreferenceKeys.EMAIL,email) }
    }

    override suspend fun setIsLoggedIn(value: Boolean) {
        context.dataStore.edit { mutablePreferences: MutablePreferences -> mutablePreferences.set(IS_APP_LOGGED_IN,value) }
    }

    override suspend fun setIsIntro(value: Boolean) {
        context.dataStore.edit { mutablePreferences: MutablePreferences -> mutablePreferences.set(
            IS_APP_INTRO,value) }
    }

    override suspend fun setAccessToken(value: String) {

        context.dataStore.edit { preferences -> preferences.set(PreferenceKeys.ACCESS_TOKEN,value) }
    }

    override suspend fun setRefreshToken(value: String) {

        context.dataStore.edit { preferences -> preferences.set(PreferenceKeys.REFRESH_TOKEN,value) }
    }
    override suspend fun setDeviceToken(value: String) {

        context.dataStore.edit { preferences -> preferences.set(PreferenceKeys.DEVICE_TOKEN,value) }
    }

    override suspend fun setNotificationMuted(value: Boolean) {
        context.dataStore.edit { mutablePreferences: MutablePreferences -> mutablePreferences.set(
            NOTIFICATION_MUTED,value) }
    }

    override suspend fun setNotificationSound(value: String) {
        context.dataStore.edit { preferences -> preferences.set(PreferenceKeys.NOTIFICATION_SOUND,value) }
    }

    override fun getBoolean() : Flow<Boolean> {
        return getBooleanData(PreferenceKeys.BOOLEAN_KEY)
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return getBooleanData(IS_APP_LOGGED_IN)
    }

    override fun isIntro(): Flow<Boolean> {
        return getBooleanData(IS_APP_INTRO)
    }

    override fun getString() : Flow<String> {
        return getString(PreferenceKeys.STRING_KEY)
    }

    override fun getUserId(): Flow<String> {
        return getString(PreferenceKeys.USERID)
    }

    override fun getEmail(): Flow<String> {
        return getString(PreferenceKeys.EMAIL)
    }

    override fun getMobileNumber(): Flow<String> {
       return getString(PreferenceKeys.MOBILE)
    }

    override fun getFirstName(): Flow<String> {
        return getString(PreferenceKeys.FIRST_NAME)
    }

    override fun getLastName(): Flow<String> {
        return getString(PreferenceKeys.LAST_NAME)
    }

    override fun getCountryCode(): Flow<String> {
        return getString(PreferenceKeys.COUNTRY_CODE)
    }

    override fun getAccessToken(): Flow<String> {
        return getString(PreferenceKeys.ACCESS_TOKEN)
    }

    override fun getRefreshToken(): Flow<String> {
        return getString(PreferenceKeys.REFRESH_TOKEN)
    }

    override fun getDeviceToken(): Flow<String> {
        return getString(PreferenceKeys.DEVICE_TOKEN)
    }

    override suspend fun getSuspendDeviceToken(lamda:(String)->Unit) {
         getSuspendString(PreferenceKeys.DEVICE_TOKEN){
             lamda.invoke(it)
         }
    }

    override fun getNotificationMuted(): Flow<Boolean> {
        return getBooleanData(NOTIFICATION_MUTED)
    }

    override fun getNotificationSound(): Flow<String> {
        return getString(PreferenceKeys.NOTIFICATION_SOUND)
    }

    //Predefine Function to get Data Using Keys
    fun getString(key:Preferences.Key<String> ):Flow<String>{
        return context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            preference.get(key) ?: ""
        }
    }
   suspend fun getSuspendString(key:Preferences.Key<String>,lamda:(String)->Unit){
       context.dataStore.data.catch { exception ->
           if (exception is IOException) {
               emit(emptyPreferences())
           } else {
               throw exception
           }
       }.map { preference ->
           lamda.invoke(preference.get(key) ?: "Null")
       }
    }
     fun getLong(key:Preferences.Key<Long>) : Flow<Long> {
        return context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            preference.get(key) ?: 0L
        }
    }
     fun getDouble(key:Preferences.Key<Double>) : Flow<Double> {
        return context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            preference.get(key) ?: 0.00
        }
    }
     fun getBooleanData(key:Preferences.Key<Boolean>) : Flow<Boolean> {
        return context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            preference.get(key) ?: false
        }
    }
     fun getIntegerData(key:Preferences.Key<Int>) : Flow<Int> {
        return context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            preference.get(key) ?: 0
        }
    }

    //endregion
}