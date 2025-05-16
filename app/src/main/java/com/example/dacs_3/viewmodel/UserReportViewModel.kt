import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacs_3.model.UserReport
import com.example.dacs_3.repository.UserReportRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class UserReportViewModel : ViewModel() {

    private val repository = UserReportRepository()

    private val _userReports = MutableStateFlow<List<UserReport>>(emptyList())
    val userReports: StateFlow<List<UserReport>> = _userReports

    private val _selectedUserReport = MutableStateFlow<UserReport?>(null)
    val selectedUserReport: StateFlow<UserReport?> = _selectedUserReport

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun submitUserReport(report: UserReport) {
        viewModelScope.launch {
            val result = repository.submitUserReport(report)
            if (result.isFailure) {
                _errorMessage.value = result.exceptionOrNull()?.message
            } else {
                fetchAllReports()
            }
        }
    }

    fun fetchUserReportById(reportId: String) {
        viewModelScope.launch {
            val report = repository.fetchUserReportById(reportId)
            _selectedUserReport.value = report
            if (report == null) {
                _errorMessage.value = "Failed to fetch user report"
            }
        }
    }

    fun deleteUserReport(reportId: String) {
        viewModelScope.launch {
            val success = repository.deleteUserReport(reportId)
            if (!success) {
                _errorMessage.value = "Failed to delete user report"
            } else {
                fetchAllReports()
            }
        }
    }

    fun updateUserReportStatus(reportId: String, newStatus: String) {
        viewModelScope.launch {
            val success = repository.updateUserReportStatus(reportId, newStatus)
            if (!success) {
                _errorMessage.value = "Failed to update report status"
            } else {
                fetchAllReports()
            }
        }
    }

    fun fetchAllReports() {
        viewModelScope.launch {
            _userReports.value = repository.fetchAllUserReports()
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
