package non.shahad.statedatamanagement.landing

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

class SignUpViewModel constructor(

): ViewModel() {

    private val _uiState = MutableStateFlow<SignUpState>(initialState())
    val uiState: StateFlow<SignUpState> = _uiState

    private val _event: MutableSharedFlow<SignUpSideEffect> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private fun initialState() = SignUpState(
        credentials = mapOf(
            CredentialIds.FIRST_NAME to "",
            CredentialIds.LAST_NAME to "",
            CredentialIds.EMAIL to "",
            CredentialIds.DOB to "",
            CredentialIds.GENDER to "",
            CredentialIds.NATIONALITY to "",
            CredentialIds.RESIDENCE to "",
            CredentialIds.MOBILE to ""
        )
    )

    fun persistCredentials(id: Int, value: String) {
        val credential = _uiState.value.credentials.toMutableMap()
        credential[id] = value

        _uiState.value = _uiState.value.copy(
            credentials = credential
        )
    }

}

object CredentialIds {
    const val FIRST_NAME = 1
    const val LAST_NAME = 2
    const val EMAIL = 3
    const val DOB = 4
    const val GENDER = 5
    const val NATIONALITY = 6
    const val RESIDENCE = 7
    const val MOBILE = 8
}

data class SignUpState(
    val isSigningUp: Boolean = false,
    val credentials: Map<Int,String>
)

sealed class SignUpSideEffect {
    object isValidating: SignUpSideEffect()
    object Validated: SignUpSideEffect()
    data class ShowValidationError(val emptyEdIds: List<Int>): SignUpSideEffect()
}