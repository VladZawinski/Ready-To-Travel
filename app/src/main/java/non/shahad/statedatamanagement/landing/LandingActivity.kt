package non.shahad.statedatamanagement.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import non.shahad.statedatamanagement.R
import non.shahad.statedatamanagement.base.DataBindingActivity
import non.shahad.statedatamanagement.common.setSafeClickListener
import non.shahad.statedatamanagement.common.switchTo
import non.shahad.statedatamanagement.databinding.ActivityLandingBinding

class LandingActivity(
    override val layoutRes: Int = R.layout.activity_landing
) : DataBindingActivity<ActivityLandingBinding>() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        viewBinding.authActions.createAccountBtn.setSafeClickListener {
            switchTo(SignUpActivity::class.java)
        }
    }

}