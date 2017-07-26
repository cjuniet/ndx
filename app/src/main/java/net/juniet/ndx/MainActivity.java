package net.juniet.ndx;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "preferences";
    private final int MAX_ROWS = 4;
    private final int MAX_COLS = 4;
    private static final char[] f2c = {'-','o','+'};
    private final Random rng = new Random();
    private int rollsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        TextView textView = (TextView) findViewById(R.id.rollResults);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clearRolls((TextView) v);
                return true;
            }
        });

        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        for (int i = 0; i < 2; ++i) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < MAX_COLS; ++j) {
                Button bt = (Button) row.getChildAt(j);
                String formula = settings.getString("formula"+i+j, String.format("1%s", bt.getHint()));
                bt.setText(formula);
                bt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showPicker(v);
                        return true;
                    }
                });
            }
        }
        for (int i = 2; i < MAX_ROWS; ++i) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < MAX_COLS; ++j) {
                Button bt = (Button) row.getChildAt(j);
                String label = settings.getString("label"+i+j, bt.getText().toString());
                bt.setText(label);
                String formula = settings.getString("formula"+i+j, bt.getHint().toString());
                bt.setHint(formula);
                bt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showFormula(v);
                        return true;
                    }
                });
            }
        }

        if (settings.getBoolean("showHelp", true)) {
            Toast.makeText(getApplicationContext(), getString(R.string.help), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("showHelp", false);

        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        for (int i = 0; i < 2; ++i) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < MAX_COLS; ++j) {
                Button bt = (Button) row.getChildAt(j);
                editor.putString("formula"+i+j, bt.getText().toString());
            }
        }        for (int i = 2; i < MAX_ROWS; ++i) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < MAX_COLS; ++j) {
                Button bt = (Button) row.getChildAt(j);
                editor.putString("label"+i+j, bt.getText().toString());
                editor.putString("formula"+i+j, bt.getHint().toString());
            }
        }

        editor.apply();
    }

    public void rollStandard(View view) {
        final Button btn = (Button) view;
        addRoll(getRollFormula(btn.getText().toString()));
    }

    public void rollCustom(View view) {
        final Button btn = (Button) view;
        addRoll(getRollFormula(btn.getHint().toString()));
    }

    private void addRoll(String s) {
        final TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(s);
    }

    private void clearRolls(TextView text) {
        text.setText(null);
        rollsCount = 0;
    }

    private void showPicker(View view) {
        final Button btn = (Button) view;
        final Dialog d = new Dialog(MainActivity.this);
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
                d.dismiss();
                btn.setText(String.format("%s%s", Integer.toString(n), btn.getHint()));
                btn.callOnClick();
            }
        });
        d.show();
    }

    private void showFormula(View view) {
        final Button btn = (Button) view;
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Formula Picker");
        d.setContentView(R.layout.dialog2);
        final EditText name = d.findViewById(R.id.editName);
        name.setText(btn.getText());
        final EditText formula = d.findViewById(R.id.editFormula);
        formula.setText(btn.getHint());
        Button bSet = d.findViewById(R.id.buttonSet);
        bSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
                final String n = name.getText().toString();
                if (n.length() > 0) btn.setText(n);
                final String f = formula.getText().toString();
                if (f.length() > 0) btn.setHint(f);
                btn.callOnClick();
            }
        });
        d.show();
    }

    private String getRollFormula(String formula) {
        String f = formula.toUpperCase();
        String sn = f.substring(0, f.indexOf('D'));
        String sx = f.substring(f.indexOf('D')+1);
        int n = 0;
        int x = 0;
        try {
            n = Integer.parseInt(sn);
            if (!sx.equals("F")) x = Integer.parseInt(sx);
        } catch (NumberFormatException nfe) {
            return "[format error: " + formula + "]\n";
        }
        return (sx.equals("F") ? rollNdF(n) : rollNdX(n, x));
    }

    private String rollNdF(int n) {
        return rollNdX(n, 3, -1, true);
    }
    private String rollNdX(int n, int x) {
        return rollNdX(n, x, 1, false);
    }
    private String rollNdX(int n, int x, int offset) {
        return rollNdX(n, x, offset, false);
    }
    private String rollNdX(int n, int x, int offset, boolean isFate) {
        int sum = 0;
        int[] rolls = new int[n];
        for (int i = 0; i < n; ++i) {
            int r = rng.nextInt(x) + offset;
            rolls[i] = r;
            sum += r;
        }
        ++rollsCount;
        return formatRollResult(n, x, sum, rolls, isFate);
    }

    private String formatRollResult(int n, int x, int sum, int[] rolls, boolean isFate) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n#");
        sb.append(rollsCount);
        sb.append(": ");
        sb.append(Integer.toString(n));
        sb.append("d");
        sb.append(isFate ? "F" : Integer.toString(x));
        sb.append(" = ");
        sb.append(isFate && n == 1 ? f2c[sum+1] : Integer.toString(sum));

        if (rolls.length > 1) {
            sb.append(" [");
            for (int i = 0; i < rolls.length; ++i) {
                if (i > 0 && !isFate) sb.append(",");
                sb.append(isFate ? f2c[rolls[i]+1] : Integer.toString(rolls[i]));
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
