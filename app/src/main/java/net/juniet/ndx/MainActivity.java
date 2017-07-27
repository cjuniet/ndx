package net.juniet.ndx;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "preferences";
    private final String VERSION = "0.3";
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
                clearRollLogs((TextView) v);
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
        DiceRollResult result = model.getRollResult();
        addRollLog(model, result);
    }

    private void addRollLog(DiceRollModel model, DiceRollResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n#").append(++rollsCount);
        sb.append(": ").append(model.toString());
        sb.append(" = ").append(result.toString());

        final TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(sb.toString());
    }

    private void clearRollLogs(TextView text) {
        text.setText(null);
        rollsCount = 0;
    }
}
