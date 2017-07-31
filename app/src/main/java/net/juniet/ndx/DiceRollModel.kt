package net.juniet.ndx

// represents dice roll formulae

import java.util.Random
import java.util.regex.Pattern

class DiceRollModel(formula: String) {
    private val rng = Random()
    private val reg = Pattern.compile("(\\d+)[d|D](\\d+|f|F)([+-]\\d+)?")

    var numberOfDice: Int = 0
    private var mNumberOfFaces: Int = 0
    var offset: Int = 0
    var adjustment: Int = 0
    var isFUDGE: Boolean = false
        private set

    init {
        tryParseFormula(formula)
    }

    var numberOfFaces: Int
        get() = mNumberOfFaces
        set(x) {
            isFUDGE = false
            mNumberOfFaces = x
            offset = 1
        }

    fun setFUDGE() {
        isFUDGE = true
        mNumberOfFaces = 3
        offset = -1
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(numberOfDice).append("d")
        sb.append(if (isFUDGE) "F" else mNumberOfFaces)
        if (adjustment > 0) {
            sb.append("+").append(adjustment)
        } else if (adjustment < 0) {
            sb.append(adjustment)
        }
        return sb.toString()
    }

    fun tryParseFormula(s: String): Boolean {
        val m = reg.matcher(s)
        if (!m.matches()) return false

        val n = Integer.parseInt(m.group(1))
        if (n < 1 || n > 100) return false

        if (m.group(2) == "f" || m.group(2) == "F") {
            numberOfDice = n
            setFUDGE()
        } else {
            val x = Integer.parseInt(m.group(2))
            if (n < 1 || n > 100) return false
            numberOfDice = n
            numberOfFaces = x
        }
        adjustment = if (m.group(3) != null) Integer.parseInt(m.group(3)) else 0
        return true
    }

    val rollResult: DiceRollResult
        get() {
            val result = DiceRollResult(this)
            for (i in 0..numberOfDice - 1) {
                val r = rng.nextInt(mNumberOfFaces) + offset
                result.addRoll(r)
            }
            result.addAdjustment(adjustment)
            return result
        }
}