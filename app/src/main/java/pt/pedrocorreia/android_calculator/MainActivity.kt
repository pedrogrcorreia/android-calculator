package pt.pedrocorreia.android_calculator

import android.graphics.Color
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
import java.lang.Math.cbrt
import java.text.NumberFormat
import kotlin.math.*
import kotlin.random.Random

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

        // Landscape orientation buttons
        binding.btnSin?.setOnClickListener(procTrig)
        binding.btnCos?.setOnClickListener(procTrig)
        binding.btnTan?.setOnClickListener(procTrig)

        binding.btnEuler?.setOnClickListener(procEuler)
        binding.btnLn?.setOnClickListener(procLogs)
        binding.btnLog?.setOnClickListener(procLogs)

        binding.btnSquare?.setOnClickListener(procPowers)
        binding.btnCube?.setOnClickListener(procPowers)
        binding.btnPower?.setOnClickListener(procPowers)

        binding.btnSqrt?.setOnClickListener(procRoots)
        binding.btnCbrt?.setOnClickListener(procRoots)
        binding.btnXrt?.setOnClickListener(procRoots)

        binding.btnFraction?.setOnClickListener(procFraction)
        binding.btnFactorial?.setOnClickListener(procFactorial)
        binding.btnRandom?.setOnClickListener(procRandom)
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
        firstOperator = NumberFormat.getInstance().parse(binding.txtCalc.text!!.toString())!!
            .toFloat()
        numberButton = false
    }

    private val procEqual = View.OnClickListener {
        if(binding.txtCalc.text.toString().isNotEmpty()) {
            if(numberButton) {
                secondOperator = NumberFormat.getInstance().parse(binding.txtCalc.text.toString())!!
                    .toFloat()
                numberButton = false
                calculate()
                firstOperator = secondOperator
            }else {
                secondOperator = NumberFormat.getInstance().parse(binding.txtCalc.text.toString())!!
                    .toFloat()
                numberButton = false
                calculate()
            }

        }
    }

    private val procInvert = View.OnClickListener {
        if(binding.txtCalc.text.toString().isNotEmpty()) {
            text = binding.txtCalc.text.toString()
            val number: Float = NumberFormat.getInstance().parse(text!!)!!.toFloat()
            setCalcText(NumberFormat.getInstance().format(-1 * number).toString())
        }
    }

    private val procPercent = View.OnClickListener {
        if(binding.txtCalc.text.toString().isNotEmpty()) {
            text = binding.txtCalc.text.toString()
            val number: Float = NumberFormat.getInstance().parse(text!!)!!.toFloat()
            setCalcText(NumberFormat.getInstance().format(0.01 * number).toString())
        }
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
            "pow" -> {
                result = firstOperator?.pow(secondOperator!!)
                binding.btnPower?.isHovered = false
            }
            "root" -> {
                result = firstOperator?.pow(1/secondOperator!!)
                binding.btnXrt?.isHovered = false
            }
        }

        setCalcText(NumberFormat.getInstance().format(result!!).toString())
    }

    // Landscape orientation buttons functions
    private val procTrig = View.OnClickListener {
        if(binding.txtCalc.text.toString().isNotEmpty()) {
            text = binding.txtCalc.text.toString()
            var number: Float = NumberFormat.getInstance().parse(text!!)!!.toFloat()
            number = Math.toRadians(number.toDouble()).toFloat()
            var result : Float = 0F
            when(it){
                binding.btnSin -> result = sin(number)
                binding.btnCos -> result = cos(number)
                binding.btnTan -> result = tan(number)
            }

            numberButton = false

            setCalcText(NumberFormat.getInstance().format(result).toString())
        }
    }

    private val procEuler = View.OnClickListener {
        setCalcText(NumberFormat.getInstance().format(E).toString())
        numberButton = false
    }

    private val procLogs = View.OnClickListener {
        if(binding.txtCalc.text.toString().isNotEmpty()){
            text = binding.txtCalc.text.toString()
            var number: Float = NumberFormat.getInstance().parse(text!!)!!.toFloat()
            var result : Float = 0F
            when(it){
                binding.btnLn -> result = ln(number)
                binding.btnLog -> result = log10(number)
            }

            numberButton = false

            setCalcText(NumberFormat.getInstance().format(result).toString())
        }
    }

    private val procPowers = View.OnClickListener{
        if(binding.txtCalc.text.toString().isNotEmpty()){
            text = binding.txtCalc.text.toString()
            val number: Float = NumberFormat.getInstance().parse(text!!)!!.toFloat()
            var result : Float = 0F
            when(it){
                binding.btnSquare -> result = number.pow(2)
                binding.btnCube -> result = number.pow(3)
                binding.btnPower -> {
                    firstOperator = number
                    binding.btnPower?.isHovered = true
                    operation = "pow"
                    numberButton = false
                    return@OnClickListener
                }
            }

            numberButton = false

            setCalcText(NumberFormat.getInstance().format(result).toString())
        }
    }

    private val procRoots = View.OnClickListener{
        if(binding.txtCalc.text.toString().isNotEmpty()){
            text = binding.txtCalc.text.toString()
            val number: Float = NumberFormat.getInstance().parse(text!!)!!.toFloat()
            var result : Float = 0F
            when(it){
                binding.btnSqrt -> result = sqrt(number)
                binding.btnCbrt -> result = cbrt(number.toDouble()).toFloat()
                binding.btnXrt -> {
                    firstOperator = number
                    binding.btnXrt?.isHovered = true
                    operation = "root"
                    numberButton = false
                    return@OnClickListener
                }
            }

            numberButton = false

            setCalcText(NumberFormat.getInstance().format(result).toString())
        }
    }

    private val procFraction = View.OnClickListener {
        if(binding.txtCalc.text.toString().isNotEmpty()){
            text = binding.txtCalc.text.toString()
            val number: Float = NumberFormat.getInstance().parse(text!!)!!.toFloat()
            var result = 1/number
            setCalcText(NumberFormat.getInstance().format(result).toString())
            numberButton = false
        }
    }

    private val procFactorial = View.OnClickListener {
        if(binding.txtCalc.text.toString().isNotEmpty()){
            text = binding.txtCalc.text.toString()
            val number: Float = NumberFormat.getInstance().parse(text!!)!!.toFloat()
            var result = 1

            for(i in number.toInt() downTo 1 step 1){
                result *= i
            }

            setCalcText(NumberFormat.getInstance().format(result).toString())
            numberButton = false
        }
    }

    private val procRandom = View.OnClickListener {
        setCalcText(NumberFormat.getInstance().format(Random.nextDouble(1000.0)).toString())
        numberButton = false
    }
}