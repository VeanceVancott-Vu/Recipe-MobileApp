package com.example.dacs_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.RecipeReport
import com.example.dacs_3.repository.RecipeReportsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeReportsViewModel  : ViewModel() {

    private val repository = RecipeReportsRepository()

    private val _userReports = MutableStateFlow<List<RecipeReport>>(emptyList())
    val userReports: StateFlow<List<RecipeReport>> = _userReports.asStateFlow()

    private val _allReports = MutableStateFlow<List<RecipeReport>>(emptyList())
    val allReports: StateFlow<List<RecipeReport>> = _allReports.asStateFlow()

    private val _submitSuccess = MutableStateFlow<Boolean?>(null)
    val submitSuccess: StateFlow<Boolean?> = _submitSuccess.asStateFlow()



    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun submitReport(report: RecipeReport) {
        viewModelScope.launch {
            val success = repository.submitReport(report)
            _submitSuccess.value = success
            if (!success) _errorMessage.value = "Failed to submit report"
        }
    }

    fun loadReportsByUser(userId: String) {
        viewModelScope.launch {
            val reports = repository.getReportsByUser(userId)
            _userReports.value = reports
        }
    }

    fun loadAllReports() {
        viewModelScope.launch {
            val reports = repository.getAllReports()
            _allReports.value = reports
        }
    }

    fun updateReportStatus(reportId: String, newStatus: String) {
        viewModelScope.launch {
            val success = repository.updateReportStatus(reportId, newStatus)
            if (!success) {
                _errorMessage.value = "Failed to update status"
            } else {
                loadAllReports() // Refresh if needed
            }
        }
    }

    fun deleteReport(reportId: String) {
        viewModelScope.launch {
            val success = repository.deleteReport(reportId)
            if (!success) {
                _errorMessage.value = "Failed to delete report"
            } else {
                loadAllReports() // Refresh the list after deletion
            }
        }
    }

    fun deleteReports(reports: List<RecipeReport>) {
        viewModelScope.launch {
            reports.forEach { report ->
                deleteReport(report.id)
            }
        }
    }


}