package com.arlib.compose.test.ui.medicinelist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arlib.compose.test.model.AssociatedDrugItem
import com.arlib.compose.test.repo.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: MedicineRepository) :
    ViewModel() {

    var selectedDrug: AssociatedDrugItem? = null
    private val _dataLoadStateFlow = MutableStateFlow<DataLoadState>(DataLoadState.Start)
    val dataLoadStateFlow: StateFlow<DataLoadState> get() = _dataLoadStateFlow

    init {
        fetchData()
    }

    fun refresh(){
        fetchData()
    }
    private fun fetchData() {
        _dataLoadStateFlow.value = DataLoadState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.execute()
            if (result.isSuccessful) {
                result.body()?.let {
                    Log.d("SURESH","$it")
                     _dataLoadStateFlow.value = DataLoadState.Success(it)
                } ?: kotlin.run {
                    _dataLoadStateFlow.value = DataLoadState.Failed("Failed parse response body")
                }

            } else {
                _dataLoadStateFlow.value = DataLoadState.Failed("Api failed to return response")
            }
        }
    }

    sealed class DataLoadState {
        object Start : DataLoadState()
        object Loading : DataLoadState()
        data class Success(val data: Any) : DataLoadState()
        data class Failed(val msg: String) : DataLoadState()
    }

    fun getTimeWiseGreeting(): String {
        val hrs = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
        var msg = ""

        if (hrs >= 0) msg = "Good night!" // REALLY early

        if (hrs > 4) msg = "Morning dear" // REALLY early

        if (hrs > 6) msg = "Good morning!" // After 6am

        if (hrs > 12) msg = "Good afternoon!" // After 12pm

        if (hrs > 17) msg = "Good evening!" // After 5pm

        if (hrs > 22) msg = "Good Night!" // After 10pm

        return msg
    }

    fun getDrugItmemsFromResponse(d: JSONObject): ArrayList<AssociatedDrugItem> {
        val dataArray: ArrayList<AssociatedDrugItem> = arrayListOf()
        val problemArry = d.getJSONArray("problems")
        for (i in 0 until problemArry.length()) {
            val problemItem = problemArry.getJSONObject(i)
            problemItem.keys().forEach {
                val problemName = it
                val diseasesArray = problemItem.getJSONArray(it)
                for (j in 0 until diseasesArray.length()) {
                    val diseasesItem = diseasesArray.getJSONObject(j)
                    diseasesItem.keys().forEach { medicationsKeys ->
                        if (medicationsKeys.toString().startsWith("medications")) {
                            val medications = diseasesItem.getJSONArray(medicationsKeys)
                            for (k in 0 until medications.length()) {
                                val medicationsClasses = medications.getJSONObject(k)
                                medicationsClasses.keys().forEach { medicationsClassesKeys ->
                                    if (medicationsClassesKeys.toString()
                                            .startsWith("medicationsClasses")
                                    ) {
                                        val classNames = medicationsClasses.getJSONArray(
                                            medicationsClassesKeys
                                        )
                                        for (l in 0 until classNames.length()) {
                                            val className = classNames.getJSONObject(l)
                                            className.keys().forEach { classNameKeys ->
                                                if (classNameKeys.toString()
                                                        .startsWith("className")
                                                ) {
                                                    val drugArray =
                                                        className.getJSONArray(classNameKeys)
                                                    for (m in 0 until drugArray.length()) {
                                                        val drugItem =
                                                            drugArray.getJSONObject(m)
                                                        drugItem.keys()
                                                            .forEach { drugItemKeys ->
                                                                val drug = AssociatedDrugItem()
                                                                val associatedDrugsArray =
                                                                    drugItem.getJSONArray(
                                                                        drugItemKeys
                                                                    )
                                                                for (n in 0 until associatedDrugsArray.length()) {

                                                                    val assosDrug =
                                                                        associatedDrugsArray.getJSONObject(
                                                                            n
                                                                        )
                                                                    assosDrug.keys()
                                                                        .forEach { asocoatedDrugKeys ->

                                                                            if (asocoatedDrugKeys == "name") {

                                                                                drug.name =
                                                                                    assosDrug.getString(
                                                                                        asocoatedDrugKeys
                                                                                    )
                                                                            } else if (asocoatedDrugKeys == "dose") {
                                                                                drug.dose =
                                                                                    assosDrug.getString(
                                                                                        asocoatedDrugKeys
                                                                                    )

                                                                            } else if (asocoatedDrugKeys == "strength") {
                                                                                drug.strength =
                                                                                    assosDrug.getString(
                                                                                        asocoatedDrugKeys
                                                                                    )
                                                                            }
                                                                        }
                                                                }
                                                                drug.problemName= problemName
                                                                dataArray.add(drug)
                                                            }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return dataArray
    }
}
class MainActivityViewModelFactory(private val repository : MedicineRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}