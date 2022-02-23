package non.shahad.statedatamanagement.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import non.shahad.statedatamanagement.R

class GenderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context,attrs, defStyleAttr){

    private var selectedItemId: Int = Gender.FEMALE
    private val view: View = View.inflate(context, R.layout.layout_gender, this)

    fun setOnAction(onChange: (Int) -> Unit) {
        val normalShape = ContextCompat.getDrawable(context,R.drawable.normal_shape)
        val selectedShape = ContextCompat.getDrawable(context, R.drawable.gender_selected_shape)

        val maleView = view.findViewById<TextView>(R.id.maleView)
        val femaleView = view.findViewById<TextView>(R.id.femaleView)

        maleView.setOnClickListener {
            if (selectedItemId != Gender.MALE){
                selectedItemId = Gender.MALE
                onChange(Gender.MALE)
                maleView.background = selectedShape
                femaleView.background = normalShape
            }
        }

        femaleView.setOnClickListener {
            if (selectedItemId != Gender.FEMALE){
                selectedItemId = Gender.FEMALE
                onChange(Gender.FEMALE)
                maleView.background = normalShape
                femaleView.background = selectedShape
            }
        }
    }



}

object Gender {
    const val MALE = 2
    const val FEMALE = 1
}