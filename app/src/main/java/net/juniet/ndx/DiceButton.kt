package net.juniet.ndx

import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.support.v7.widget.AppCompatButton
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast

class DiceButton : AppCompatButton, View.OnLongClickListener {
    var rollModel: DiceRollModel
    var isCustom: Boolean = false
        private set

    constructor(context: Context) : super(context) {
        setOnLongClickListener(this)
        rollModel = DiceRollModel("1d6")
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(attrs,
                R.styleable.DiceButton, 0, 0)
        try {
            rollModel = DiceRollModel(a.getString(R.styleable.DiceButton_rollModel)!!)
            isCustom = a.getBoolean(R.styleable.DiceButton_isCustom, false)
        } finally {
            a.recycle()
        }
        setOnLongClickListener(this)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.theme.obtainStyledAttributes(attrs,
                R.styleable.DiceButton, 0, 0)
        try {
            rollModel = DiceRollModel(a.getString(R.styleable.DiceButton_rollModel)!!)
            isCustom = a.getBoolean(R.styleable.DiceButton_isCustom, false)
        } finally {
            a.recycle()
        }
        setOnLongClickListener(this)
    }

    override fun onLongClick(view: View): Boolean {
        if (isCustom) {
            showFormula()
        } else {
            showPicker()
        }
        return true
    }

    private fun showPicker() {
        val d = Dialog(context)
        d.setTitle("Number Picker")
        d.setContentView(R.layout.dialog1)
        val np = d.findViewById<NumberPicker>(R.id.numberPicker1)
        np.minValue = 1
        np.maxValue = 100
        np.wrapSelectorWheel = false
        val bSet = d.findViewById<Button>(R.id.buttonSet)
        bSet.setOnClickListener {
            val n = np.value
            rollModel.numberOfDice = n
            text = if (n == 1) tag.toString() else rollModel.toString()
            d.dismiss()
        }
        d.show()
    }

    private fun showFormula() {
        val d = Dialog(context)
        d.setTitle("Formula Picker")
        d.setContentView(R.layout.dialog2)
        val name = d.findViewById<EditText>(R.id.editName)
        name.setText(text)
        val formula = d.findViewById<EditText>(R.id.editFormula)
        formula.setText(rollModel.toString())
        val bSet = d.findViewById<Button>(R.id.buttonSet)
        bSet.setOnClickListener {
            val newName = name.text.toString()
            val newFormula = formula.text.toString()
            if (newName.isNotEmpty() && newFormula.isNotEmpty() && rollModel.tryParseFormula(newFormula)) {
                text = newName
                d.dismiss()
            } else {
                Toast.makeText(context, context.getString(R.string.invalid_formula), Toast.LENGTH_LONG).show()
            }
        }
        d.show()
    }
}
