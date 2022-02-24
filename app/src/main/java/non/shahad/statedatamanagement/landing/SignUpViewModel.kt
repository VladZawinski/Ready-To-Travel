package non.shahad.statedatamanagement.landing

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SignUpViewModel constructor(

): ViewModel() {

    private val state = MutableStateFlow<SignUpState>(initialState())
    val uiState = state.asStateFlow()

    private val _event = Channel<SignUpSideEffect>(Channel.CONFLATED)
    val event = _event.receiveAsFlow()

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

    /**
     * Compilation error on map.forEach { t, u ->  }
     * Strange, isn't it?
     */
    fun validate() {
        viewModelScope.launch {
            publishEvent(SignUpSideEffect.Validated)
            val emptyEntities = state.value.credentials.filterValues { it.isEmpty() }

            if (emptyEntities.isEmpty()){
                hitToAPI()
                publishEvent(SignUpSideEffect.Validated)
            } else {
                val emptyIds = emptyEntities.keys.toList()
                publishEvent(SignUpSideEffect.ShowValidationError(emptyIds))
            }
        }
    }

    private fun checkEmailValidation(email: String){

    }

    private fun hitToAPI(){}

    fun persistCredentials(id: Int, value: String) {
        val credential = state.value.credentials.toMutableMap()
        credential[id] = value

        state.value = state.value.copy(
            credentials = credential
        )
    }

    fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private suspend fun publishEvent(event: SignUpSideEffect) = _event.send(event)

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
    val credentials: Map<Int,String>
)

sealed class SignUpSideEffect {
    object isValidating: SignUpSideEffect()
    object Validated: SignUpSideEffect()
    data class ShowToast(val message: String): SignUpSideEffect()
    data class ShowValidationError(val emptyEdIds: List<Int>): SignUpSideEffect()
}