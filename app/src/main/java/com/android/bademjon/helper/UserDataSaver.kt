package com.android.bademjon.helper

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.android.bademjon.model.UserDataModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")

class UserDataSaver(private val context: Context) : ViewModel() {

    companion object {
        private val nameKey = stringPreferencesKey("name")
        private val weightKey = stringPreferencesKey("weight")
        private val heightKey = stringPreferencesKey("height")
        private val ageKey = stringPreferencesKey("age")
        private val wristKey = stringPreferencesKey("wrist")
        private val rateKey = stringPreferencesKey("rate")
        private val genderKey = stringPreferencesKey("gender")
        private val diseaseKey = stringPreferencesKey("disease")
        private var sav=false
        private var lod=false
    }

    suspend fun saver(userData: UserDataModel){
            context.dataStore.edit {
                it[nameKey] = userData.name.value
                it[weightKey] = userData.weight.value.toString()
                it[heightKey] = userData.height.value.toString()
                it[ageKey] = userData.age.value.toString()
                it[wristKey] = userData.wrist.value.toString()
                it[rateKey] = userData.rate.value.toString()
                it[genderKey] = userData.gender.value.toString()
                it[diseaseKey] = userData.disease.value.toString()
            }

    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun geter(userData: UserDataModel) {
        /*
        userData.name.value=context.dataStore.data.map {
            it[nameKey] ?: ""
        }.collectAsState(initial = "0").value

        userData.weight.intValue=context.dataStore.data.map {
            it[weightKey] ?: 0
        }.collectAsState(initial = "0").value.toString().toInt()

        userData.height.intValue=context.dataStore.data.map {
            it[heightKey] ?: "0"
        }.collectAsState(initial = "0").value.toString().toInt()

        userData.age.intValue=context.dataStore.data.map {
            it[ageKey] ?: "0"
        }.collectAsState(initial = "0").value.toString().toInt()

        userData.wrist.intValue=context.dataStore.data.map {
            it[wristKey] ?: "0"
        }.collectAsState(initial = "0").value.toString().toInt()

        userData.rate.intValue=context.dataStore.data.map {
            it[rateKey] ?: "0"
        }.collectAsState(initial = "0").value.toString().toInt()

        userData.gender.intValue=context.dataStore.data.map {
            it[genderKey] ?: "0"
        }.collectAsState(initial = "0").value.toString().toInt()

        userData.disease.intValue=context.dataStore.data.map {
            it[diseaseKey] ?: "0"
        }.collectAsState(initial = "0").value.toString().toInt()
*/

        /*
        userData.name=rememberPreference(nameKey,"")

        userData.height=rememberPreference(heightKey,"0")

        userData.weight=rememberPreference(weightKey,"0")

        userData.age=rememberPreference(ageKey,"0")

        userData.wrist=rememberPreference(wristKey,"0")

        userData.rate=rememberPreference(rateKey,"0")

        userData.gender=rememberPreference(genderKey,"0")

        userData.disease=rememberPreference(diseaseKey,"0")

         */

        userData.name.value=rememberPreference(nameKey,"").value

        userData.height=rememberPreference(heightKey,"0")

        userData.weight=rememberPreference(weightKey,"0")

        userData.age=rememberPreference(ageKey,"0")

        userData.wrist=rememberPreference(wristKey,"0")

        userData.rate=rememberPreference(rateKey,"0")

        userData.gender=rememberPreference(genderKey,"0")

        userData.disease=rememberPreference(diseaseKey,"0")

    }

}


@Composable
fun <T> rememberPreference(
    key: Preferences.Key<T>,
    defaultValue: T,
): MutableState<T> {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = remember {
        context.dataStore.data
            .map {
                it[key] ?: defaultValue
            }
    }.collectAsState(initial = defaultValue)

    return remember {
        object : MutableState<T> {
            override var value: T
                get() = state.value
                set(value) {
                    coroutineScope.launch {
                        context.dataStore.edit {
                            it[key] = value
                        }
                    }
                }

            override fun component1() = value
            override fun component2(): (T) -> Unit = { value = it }
        }
    }
}

