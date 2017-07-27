package net.juniet.ndx

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val PREFS_NAME = "preferences"
    private val PREFS_VERSION = "0.3"
    private var rollsCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = getSharedPreferences(PREFS_NAME, 0)

        val textView = findViewById(R.id.rollResults) as TextView
        textView.movementMethod = ScrollingMovementMethod()
        textView.setOnLongClickListener { v ->
            clearRollLogs(v as TextView)
            true
        }

        val table = findViewById(R.id.tableLayout) as TableLayout
        for (i in 0..table.childCount - 1) {
            val row = table.getChildAt(i) as TableRow
            for (j in 0..row.childCount - 1) {
                val bt = row.getChildAt(j) as DiceButton
                val label = settings.getString("label_" + bt.tag, bt.tag.toString())
                bt.text = label
                val formula = settings.getString("formula_" + bt.tag, bt.rollModel.toString())
                bt.rollModel = DiceRollModel(formula)
            }
        }

        if (settings.getBoolean("showHelp", true)) {
            Toast.makeText(applicationContext, getString(R.string.help), Toast.LENGTH_LONG).show()
        }
    }

    override fun onStop() {
        super.onStop()

        val settings = getSharedPreferences(PREFS_NAME, 0)
        val editor = settings.edit()
        editor.putString("version", PREFS_VERSION)
        editor.putBoolean("showHelp", false)

        val table = findViewById(R.id.tableLayout) as TableLayout
        for (i in 0..table.childCount - 1) {
            val row = table.getChildAt(i) as TableRow
            for (j in 0..row.childCount - 1) {
                val bt = row.getChildAt(j) as DiceButton
                editor.putString("label_" + bt.tag, bt.text.toString())
                editor.putString("formula_" + bt.tag, bt.rollModel.toString())
            }
        }

        editor.apply()
    }

    fun rollDice(view: View) {
        val model = (view as DiceButton).rollModel
        val result = model.rollResult
        addRollLog(model, result)
    }

    private fun addRollLog(model: DiceRollModel, result: DiceRollResult) {
        val sb = StringBuilder()
        sb.append("\n\n#").append(++rollsCount)
        sb.append(": ").append(model.toString())
        sb.append(" = ").append(result.toString())

        val text = findViewById(R.id.rollResults) as TextView
        text.append(sb.toString())
    }

    private fun clearRollLogs(text: TextView) {
        text.text = null
        rollsCount = 0
    }
}
