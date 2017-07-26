package net.juniet.ndx;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class DiceButton extends AppCompatButton implements View.OnLongClickListener {
    private DiceRollModel mRollModel;
    private boolean mIsCustom;

    public DiceRollModel getRollModel() { return mRollModel; }
    public void setRollModel(DiceRollModel m) { mRollModel = m; }
    public boolean isCustom() { return mIsCustom; }

    public DiceButton(Context context) {
        super(context);
        setOnLongClickListener(this);
    }

    public DiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DiceButton, 0, 0);
        try {
            mRollModel = new DiceRollModel(a.getString(R.styleable.DiceButton_rollModel));
            mIsCustom = a.getBoolean(R.styleable.DiceButton_isCustom, false);
        } finally {
            a.recycle();
        }
        setOnLongClickListener(this);
    }

    public DiceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DiceButton, 0, 0);
        try {
            mRollModel = new DiceRollModel(a.getString(R.styleable.DiceButton_rollModel));
            mIsCustom = a.getBoolean(R.styleable.DiceButton_isCustom, false);
        } finally {
            a.recycle();
        }
        setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        if (isCustom()) {
            showFormula();
        } else {
            showPicker();
        }
        return true;
    }

    private void showPicker() {
        final Dialog d = new Dialog(getContext());
        d.setTitle("Number Picker");
        d.setContentView(R.layout.dialog1);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        np.setMinValue(1);
        np.setMaxValue(100);
        np.setWrapSelectorWheel(false);
        Button bSet = d.findViewById(R.id.buttonSet);
        bSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int n = np.getValue();
                getRollModel().setNumberOfDice(n);
                setText(n == 1 ? getTag().toString() : getRollModel().toString());
                d.dismiss();
                callOnClick();
            }
        });
        d.show();
    }

    private void showFormula() {
        final Dialog d = new Dialog(getContext());
        d.setTitle("Formula Picker");
        d.setContentView(R.layout.dialog2);
        final EditText name = d.findViewById(R.id.editName);
        name.setText(getText());
        final EditText formula = d.findViewById(R.id.editFormula);
        formula.setText(getRollModel().toString());
        Button bSet = d.findViewById(R.id.buttonSet);
        bSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
                final String n = name.getText().toString();
                if (n.length() > 0) setText(n);
                final String f = formula.getText().toString();
                if (f.length() > 0) getRollModel().parseFormula(f);
                callOnClick();
            }
        });
        d.show();
    }
}
