package com.example.calculator

import kotlin.math.sqrt

/**
 * Handles the mathematical logic for the calculator.
 * Kept separate from MainActivity to follow the lab requirements.
 */
class CalculatorEngine {

    // Helper to format numbers (removes .0 from whole numbers)
    fun formatResult(result: Double): String {
        return if (result % 1.0 == 0.0) {
            result.toInt().toString()
        } else {
            result.toString()
        }
    }

    fun calculate(num1: Double, num2: Double, operator: String): String {
        var result = 0.0
        when (operator) {
            "+" -> result = num1 + num2
            "-" -> result = num1 - num2
            "*" -> result = num1 * num2
            "/" -> {
                if (num2 == 0.0) return "Error" // Prevent divide by zero crash
                result = num1 / num2
            }
            "%" -> result = num1 % num2
            else -> return "Error"
        }
        return formatResult(result)
    }

    fun calculateSquareRoot(num: Double): String {
        if (num < 0) return "Error"
        return formatResult(sqrt(num))
    }

    fun calculateOneOverX(num: Double): String {
        if (num == 0.0) return "Error"
        return formatResult(1.0 / num)
    }
}