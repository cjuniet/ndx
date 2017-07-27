package net.juniet.ndx

// represents dice roll results

import java.util.ArrayList

class DiceRollResult(private val mModel: DiceRollModel) {
    private val rolls: ArrayList<Int> = ArrayList<Int>(mModel.numberOfDice)
    private var sum: Int = 0

    fun addRoll(r: Int) {
        rolls.add(r)
        sum += r
    }

    fun addAdjustment(mAdjustment: Int) {
        sum += mAdjustment
    }

    fun fudge2string(n: Int): String {
        return when(n) {
            -1 -> "-"
            0 -> "o"
            +1 -> "+"
            else -> "?"
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (rolls.size == 1 && mModel.adjustment == 0) {
            sb.append(if (mModel.isFUDGE) fudge2string(sum) else Integer.toString(sum))
        } else {
            sb.append(Integer.toString(sum))
            sb.append(" [")
            for (i in rolls.indices) {
                if (i > 0 && !mModel.isFUDGE) sb.append(",")
                sb.append(if (mModel.isFUDGE) fudge2string(rolls[i]) else rolls[i].toString())
            }
            sb.append("]")
        }
        return sb.toString()
    }
}