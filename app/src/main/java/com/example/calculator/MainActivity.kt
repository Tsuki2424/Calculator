package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private val engine = CalculatorEngine()

    // Variables to hold the state of the calculator
    private var currentInput: String = ""
    private var firstOperand: Double = 0.0
    private var currentOperator: String = ""
    private var isNewInput: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)

        // Number buttons
        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener { onNumberClick((it as Button).text.toString()) }
        }

        // Operator buttons (+, -, *, /)
        val operatorButtons = listOf(R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv)
        for (id in operatorButtons) {
            findViewById<Button>(id).setOnClickListener { onOperatorClick((it as Button).text.toString()) }
        }

        // Decimal dot
        findViewById<Button>(R.id.btnDot).setOnClickListener { onDotClick() }

        // Equals
        findViewById<Button>(R.id.btnEq).setOnClickListener { onEqualsClick() }

        // Clear (C)
        findViewById<Button>(R.id.btnC).setOnClickListener { clearAll() }

        // Clear Entry (CE)
        findViewById<Button>(R.id.btnCE).setOnClickListener { clearEntry() }

        // Backspace
        findViewById<Button>(R.id.btnBack).setOnClickListener { onBackspaceClick() }

        // Sign Change (+/-)
        findViewById<Button>(R.id.btnSign).setOnClickListener { onSignChangeClick() }

        // Square Root
        findViewById<Button>(R.id.btnSqrt).setOnClickListener { onSquareRootClick() }

        // 1/x
        findViewById<Button>(R.id.btn1OverX).setOnClickListener { onOneOverXClick() }

        // Percent
        findViewById<Button>(R.id.btnPercent).setOnClickListener { onPercentClick() }
    }

    private fun onNumberClick(number: String) {
        if (isNewInput) {
            currentInput = number
            isNewInput = false
        } else {
            // Prevent multiple leading zeros
            if (currentInput == "0") currentInput = number else currentInput += number
        }
        tvDisplay.text = currentInput
    }

    private fun onDotClick() {
        if (isNewInput) {
            currentInput = "0."
            isNewInput = false
        } else if (!currentInput.contains(".")) {
            currentInput += "."
        }
        tvDisplay.text = currentInput
    }

    private fun onOperatorClick(op: String) {
        if (currentInput.isNotEmpty() && currentInput != "Error") {
            if (currentOperator.isNotEmpty() && !isNewInput) {
                // Chain operations (e.g., 5 + 3 + 2)
                calculateIntermediate()
            } else {
                firstOperand = currentInput.toDouble()
            }
            currentOperator = op
            isNewInput = true
        }
    }

    private fun calculateIntermediate() {
        val secondOperand = currentInput.toDouble()
        val result = engine.calculate(firstOperand, secondOperand, currentOperator)
        tvDisplay.text = result
        if (result == "Error") {
            firstOperand = 0.0
            currentInput = ""
            currentOperator = ""
        } else {
            firstOperand = result.toDouble()
            currentInput = result
        }
    }

    private fun onEqualsClick() {
        if (currentOperator.isNotEmpty() && !isNewInput) {
            val secondOperand = currentInput.toDouble()
            val result = engine.calculate(firstOperand, secondOperand, currentOperator)
            tvDisplay.text = result
            currentInput = if (result == "Error") "" else result
            currentOperator = ""
            isNewInput = true
        }
    }

    private fun clearAll() {
        currentInput = ""
        firstOperand = 0.0
        currentOperator = ""
        isNewInput = true
        tvDisplay.text = getString(R.string.zero)
    }

    private fun clearEntry() {
        currentInput = ""
        isNewInput = true
        tvDisplay.text = getString(R.string.zero)
    }

    private fun onBackspaceClick() {
        if (!isNewInput && currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput.isEmpty() || currentInput == "-") {
                tvDisplay.text = getString(R.string.zero)
                isNewInput = true
            } else {
                tvDisplay.text = currentInput
            }
        }
    }

    private fun onSignChangeClick() {
        if (currentInput.isNotEmpty() && currentInput != "0" && currentInput != "Error") {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-$currentInput"
            }
            tvDisplay.text = currentInput
        }
    }

    private fun onSquareRootClick() {
        if (currentInput.isNotEmpty() && currentInput != "Error") {
            val result = engine.calculateSquareRoot(currentInput.toDouble())
            tvDisplay.text = result
            currentInput = if (result == "Error") "" else result
            isNewInput = true
        }
    }

    private fun onOneOverXClick() {
        if (currentInput.isNotEmpty() && currentInput != "Error") {
            val result = engine.calculateOneOverX(currentInput.toDouble())
            tvDisplay.text = result
            currentInput = if (result == "Error") "" else result
            isNewInput = true
        }
    }

    private fun onPercentClick() {
        if (currentInput.isNotEmpty() && currentInput != "Error") {
            val currentNumber = currentInput.toDouble()

            // Default: just divide by 100 (for * and /)
            var result = currentNumber / 100.0

            // Smart behavior: calculate % based on the first number (for + and -)
            if (currentOperator == "+" || currentOperator == "-") {
                result = firstOperand * (currentNumber / 100.0)
            }

            currentInput = engine.formatResult(result)
            tvDisplay.text = currentInput
            isNewInput = false
        }
    }
}