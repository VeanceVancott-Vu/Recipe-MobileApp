package com.example.dacs_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.CommentReport
import com.example.dacs_3.repository.CommentReportsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentReportsViewModel : ViewModel() {

    private val repository = CommentReportsRepository()

    private val _reportStatus = MutableStateFlow<ReportStatus>(ReportStatus.Idle)
    val reportStatus: StateFlow<ReportStatus> = _reportStatus.asStateFlow()

    private val _userReports = MutableStateFlow<List<CommentReport>>(emptyList())
    val userReports: StateFlow<List<CommentReport>> = _userReports.asStateFlow()


    private val _allReports = MutableStateFlow<List<CommentReport>>(emptyList())
    val allReports: StateFlow<List<CommentReport>> = _allReports.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun submitReport(report: CommentReport) {
        viewModelScope.launch {
            _reportStatus.value = ReportStatus.Loading
            val result = repository.submitCommentReport(report)
            _reportStatus.value = if (result.isSuccess) {
                ReportStatus.Success
            } else {
                ReportStatus.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun fetchReportsByUser(userId: String) {
        viewModelScope.launch {
            val result = repository.getReportsByUser(userId)
            if (result.isSuccess) {
                _userReports.value = result.getOrDefault(emptyList())
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun fetchReports() {
        viewModelScope.launch {
            val result = repository.getAllReports()
            if (result.isSuccess) {
                _allReports.value = result.getOrDefault(emptyList())
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun updateReportStatus(reportId: String, newStatus: String) {
        viewModelScope.launch {
            val result = repository.updateReportStatus(reportId, newStatus)
            if (result.isFailure) {
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun resetStatus() {
        _reportStatus.value = ReportStatus.Idle
    }

    sealed class ReportStatus {
        object Idle : ReportStatus()
        object Loading : ReportStatus()
        object Success : ReportStatus()
        data class Error(val message: String) : ReportStatus()
    }
}
