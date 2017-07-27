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
    companion object {
         val PREFS_NAME = "preferences"
         val PREFS_VERSION = "0.3"
    }

    private var rollsCount = 0
    private lateinit var mTextView: TextView
    private lateinit var mTable: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = getSharedPreferences(PREFS_NAME, 0)

        mTextView = findViewById(R.id.rollResults) as TextView
        mTextView.movementMethod = ScrollingMovementMethod()
        mTextView.setOnLongClickListener { _ ->
            clearRollLogs()
            true
        }

        mTable = findViewById(R.id.tableLayout) as TableLayout
        for (i in 0..mTable.childCount - 1) {
            val row = mTable.getChildAt(i) as TableRow
            for (j in 0..row.childCount - 1) {
                val bt = row.getChildAt(j) as DiceButton
                bt.loadSettings(settings)
            }
        }

        if (settings.getBoolean("showHelp", true)) {
            Toast.makeText(applicationContext, getString(R.string.help), Toast.LENGTH_LONG).show()
        }
    }

    override fun onStop() {
        saveSettings()
        super.onStop()
    }

    fun saveSettings() {
        val settings = getSharedPreferences(PREFS_NAME, 0)

        val editor = settings.edit()
        editor.putString("version", PREFS_VERSION)
        editor.putBoolean("showHelp", false)
        editor.apply()

        for (i in 0..mTable.childCount - 1) {
            val row = mTable.getChildAt(i) as TableRow
            for (j in 0..row.childCount - 1) {
                val bt = row.getChildAt(j) as DiceButton
                bt.saveSettings(settings)
            }
        }
    }

    fun rollDice(view: View) {
        val model = (view as DiceButton).rollModel
        val result = model.rollResult
        addRollLog(model, result)
    }

    private fun addRollLog(model: DiceRollModel, result: DiceRollResult) {
        ++rollsCount

        val sb = StringBuilder()
        if (rollsCount > 1 ) sb.append("\n\n")
        sb.append("#").append(rollsCount)
        sb.append(": ").append(model.toString())
        sb.append(" = ").append(result.toString())
        mTextView.append(sb.toString())
    }

    private fun clearRollLogs() {
        mTextView.text = null
        rollsCount = 0
    }
}
