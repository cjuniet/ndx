package net.juniet.ndx;

import android.app.Dialog;
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
    private static final char[] f2c = {'-','o','+'};
    private final String VERSION = "0.3";

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
        for (int i = 0; i < table.getChildCount(); ++i) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); ++j) {
                DiceButton bt = (DiceButton) row.getChildAt(j);
                String label = settings.getString("label_" + bt.getTag(), bt.getTag().toString());
                bt.setText(label);
                String formula = settings.getString("formula_" + bt.getTag(), bt.getRollModel().toString());
                bt.setRollModel(new DiceRollModel(formula));
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
        editor.putString("version", VERSION);
        editor.putBoolean("showHelp", false);

        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        for (int i = 0; i < table.getChildCount(); ++i) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); ++j) {
                DiceButton bt = (DiceButton) row.getChildAt(j);
                editor.putString("label_" + bt.getTag(), bt.getText().toString());
                editor.putString("formula_" + bt.getTag(), bt.getRollModel().toString());
            }
        }

        editor.apply();
    }

    public void rollDice(View view) {
        DiceRollModel model = ((DiceButton)view).getRollModel();
        if (model.isFUDGE()) {
            addRoll(rollNdF(model.getNumberOfDice()));
        } else {
            addRoll(rollNdX(model.getNumberOfDice(), model.getNumberOfFaces()));
        }
    }

    private void addRoll(String s) {
        final TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(s);
    }

    private void clearRolls(TextView text) {
        text.setText(null);
        rollsCount = 0;
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
