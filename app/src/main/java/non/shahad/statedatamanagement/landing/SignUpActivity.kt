package non.shahad.statedatamanagement.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import non.shahad.statedatamanagement.R
import non.shahad.statedatamanagement.base.DataBindingActivity
import non.shahad.statedatamanagement.databinding.ActivitySignUpBinding
import non.shahad.statedatamanagement.view.Gender
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

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

        viewBinding.birthdayEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()

                picker.show(supportFragmentManager,"tag")
                picker.addOnPositiveButtonClickListener {
                    val localDate = Date(it)
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    val date = format.format(localDate)
                    viewBinding.birthdayEditText.setText(date)
                }
            }

            false
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest {
                    Log.d("SignUpActivity", "onCreate: $it")
                }
            }

        }

        lifecycleScope.launchWhenStarted {

            viewModel.event.collectLatest {
                when(it){
                    is SignUpSideEffect.ShowValidationError -> {
                        it.emptyEdIds.forEach { id ->
                            val viewToShowError = formItems[id]
                            viewToShowError?.error = "Fill"
                        }
                    }

                    SignUpSideEffect.Validated -> {
                        Toast.makeText(this@SignUpActivity, "Validated", Toast.LENGTH_SHORT).show()
                    }

                    SignUpSideEffect.isValidating -> {

                    }
                }
            }
        }

        viewBinding.signUpBtn.setOnClickListener {
            viewModel.validate()
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