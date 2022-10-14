package pt.pedrocorreia.android_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import pt.pedrocorreia.android_calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    var firstOperator : Int? = null
    var secondOperator: Int? = null
    var operation : String? = null
    var text : String? = null
    var numberButton : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnOne.setOnClickListener(this)
        binding.btnTwo.setOnClickListener(this)
        binding.btnThree.setOnClickListener(this)
        binding.btnFour.setOnClickListener(this)
        binding.btnFive.setOnClickListener(this)
        binding.btnSix.setOnClickListener(this)
        binding.btnSeven.setOnClickListener(this)
        binding.btnEight.setOnClickListener(this)
        binding.btnNine.setOnClickListener(this)
        binding.btnZero.setOnClickListener(this)
        binding.btnDot.setOnClickListener(this)
        binding.btnClear.setOnClickListener {
            binding.txtCalc.text = ""
            firstOperator = null
            secondOperator = null
            operation = null
        }
        binding.btnPlus.setOnClickListener(procOperation)
    }

    override fun onClick(view: View?) {
        if(numberButton) {
            text = binding.txtCalc.text.toString()
            setCalcText(text.plus((view as Button).text))
        } else {
            setCalcText((view as Button).text.toString())
            numberButton = true
        }

    }

    private val procOperation = View.OnClickListener {
        operation = (it as Button).text.toString()
        numberButton = false
    }

    private fun setCalcText(txt: String){
        binding.txtCalc.text = txt
    }
}