package non.shahad.statedatamanagement.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import non.shahad.statedatamanagement.R
import non.shahad.statedatamanagement.base.DataBindingActivity
import non.shahad.statedatamanagement.databinding.ActivitySignUpBinding
import non.shahad.statedatamanagement.view.Gender

class SignUpActivity(
    override val layoutRes: Int = R.layout.activity_sign_up
) : DataBindingActivity<ActivitySignUpBinding>(){

    private val viewModel by viewModels<SignUpViewModel>()

    lateinit var formItems: Map<Int,TextInputEditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        formItems = createFormItems().also { map ->
            map.forEach { (key, v) ->
                v.doAfterTextChanged {
                    viewModel.persistCredentials(key,it.toString())
                }
            }
        }

        viewBinding.genderView.setOnAction {
            if(it == Gender.MALE){
                viewModel.persistCredentials(CredentialIds.GENDER, "male")
            } else {
                viewModel.persistCredentials(CredentialIds.GENDER, "female")
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest {
                    Log.d("SignUpActivity", "onCreate: $it")
                }

                viewModel.event.collectLatest {
                    when(it){
                        is SignUpSideEffect.ShowValidationError -> {
                            it.emptyEdIds.forEach { id ->
                                val viewToShowError = formItems[id]
                                viewToShowError?.error = "Fill"
                            }
                        }
                        SignUpSideEffect.Validated -> {
                            Log.d("Events_", "Validated")
                        }
                        SignUpSideEffect.isValidating -> {
                            Log.d("Events_", "Validating")
                        }
                    }
                }
            }
        }
    }

    private fun createFormItems() = mapOf<Int,TextInputEditText>(
        CredentialIds.FIRST_NAME to viewBinding.firstNameEd,
        CredentialIds.LAST_NAME to viewBinding.lastNameEd,
        CredentialIds.EMAIL to viewBinding.emailEd,
        CredentialIds.DOB to viewBinding.birthdayEditText,
        CredentialIds.NATIONALITY to viewBinding.nationalityEd,
        CredentialIds.RESIDENCE to viewBinding.countryOfResidenceEd,
        CredentialIds.MOBILE to viewBinding.mobileNoEd
    )
}