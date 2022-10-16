package pt.pedrocorreia.android_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import pt.pedrocorreia.android_calculator.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private var firstOperator : Float? = null
    private var secondOperator: Float? = null
    private var operation : String? = null
    private var text : String? = null
    private var numberButton : Boolean = true
    private var lightMode : Boolean = true

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_button, menu)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("lightMode", lightMode)
        Log.d("NightMode", "guardei $lightMode")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lightMode = savedInstanceState.getBoolean("lightMode")
        Log.d("NightMode", "restaurei $lightMode")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id : Int = item.itemId
        if(id == R.id.light_mode){
            Log.d("NightMode", "antes $lightMode")
            if(lightMode) {
                lightMode = false
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            }else{
                lightMode = true
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
            Log.d("NightMode", "depois $lightMode")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

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
        binding.btnMinus.setOnClickListener(procOperation)
        binding.btnMultiply.setOnClickListener(procOperation)
        binding.btnDivide.setOnClickListener(procOperation)
        binding.btnPercent.setOnClickListener(procOperation)
        binding.btnInvert.setOnClickListener(procInvert)
        binding.btnPercent.setOnClickListener(procPercent)
        binding.btnEqual.setOnClickListener(procEqual)
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
        firstOperator = NumberFormat.getInstance().parse(binding.txtCalc.text!!.toString())
            .toFloat()
        numberButton = false
    }

    private val procEqual = View.OnClickListener {
        if(binding.txtCalc.text.toString().isNotEmpty()) {
            if(numberButton) {
                secondOperator = NumberFormat.getInstance().parse(binding.txtCalc.text.toString())
                    .toFloat()
                numberButton = false
                calculate()
                firstOperator = secondOperator
            }else {
                secondOperator = NumberFormat.getInstance().parse(binding.txtCalc.text.toString())
                    .toFloat()
                numberButton = false
                calculate()
            }

        }
    }

    private val procInvert = View.OnClickListener {
        text = binding.txtCalc.text.toString()
        val number = NumberFormat.getInstance().parse(text).toFloat()
        setCalcText(NumberFormat.getInstance().format(-1 * number).toString())
    }

    private val procPercent = View.OnClickListener {
        text = binding.txtCalc.text.toString()
        val number = NumberFormat.getInstance().parse(text).toFloat()
        setCalcText(NumberFormat.getInstance().format(0.01 * number).toString())
    }

    private fun setCalcText(txt: String){
        binding.txtCalc.text = txt
    }

    private fun calculate(){
        var result : Float? = null
        when(operation){
            "+" -> result = firstOperator?.plus(secondOperator!!)
            "-" -> result = firstOperator?.minus(secondOperator!!)
            "*" -> result = firstOperator?.times(secondOperator!!)
            "/" -> result = firstOperator?.div(secondOperator!!)
            "+/-" -> {
                result = firstOperator?.roundToInt()?.toFloat()
            }
        }

        setCalcText(NumberFormat.getInstance().format(result!!).toString())
    }
}