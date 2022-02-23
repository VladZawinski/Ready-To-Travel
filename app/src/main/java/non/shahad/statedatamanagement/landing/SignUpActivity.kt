package non.shahad.statedatamanagement.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import non.shahad.statedatamanagement.R
import non.shahad.statedatamanagement.base.DataBindingActivity
import non.shahad.statedatamanagement.databinding.ActivitySignUpBinding

class SignUpActivity(
    override val layoutRes: Int = R.layout.activity_sign_up
    ) : DataBindingActivity<ActivitySignUpBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(viewBinding.toolbar)
    }
}