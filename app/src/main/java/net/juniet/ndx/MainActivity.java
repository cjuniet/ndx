package net.juniet.ndx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final char[] f2c = {'-','o','+'};
    private final Random rng = new Random();
    private int rollsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.rollResults);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clearRolls((TextView) v);
                return true;
            }
        });
    }

    public void roll1D4(View view) {
        addRoll(rollNdX(1, 4));
    }
    public void roll1D6(View view) {
        addRoll(rollNdX(1, 6));
    }
    public void roll1D8(View view) {
        addRoll(rollNdX(1, 8));
    }
    public void roll1D10(View view) {
        addRoll(rollNdX(1, 10, 0));
    }
    public void roll1D12(View view) {
        addRoll(rollNdX(1, 12));
    }
    public void roll1D20(View view) {
        addRoll(rollNdX(1, 20));
    }
    public void roll1D100(View view) {
        addRoll(rollNdX(1, 100, 0));
    }
    public void roll1DF(View view) {
        addRoll(rollNdF(1));
    }

    public void rollCustom1(View view) {
        addRoll(rollNdF(4));
    }
    public void rollCustom2(View view) {
        addRoll(rollNdX(4, 6));
    }
    public void rollCustom3(View view) {
        addRoll(rollNdX(20, 100, 0));
    }
    public void rollCustom4(View view) {
        addRoll(rollNdX(1, 3));
    }
    public void rollCustom5(View view) {
        addRoll(rollNdF(4));
    }
    public void rollCustom6(View view) {
        addRoll(rollNdX(4, 6));
    }
    public void rollCustom7(View view) {
        addRoll(rollNdX(20, 100, 0));
    }
    public void rollCustom8(View view) {
        addRoll(rollNdX(1, 3));
    }

    private void addRoll(String s) {
        TextView text = (TextView) findViewById(R.id.rollResults);
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
                if (i > 0) sb.append(",");
                sb.append(isFate ? f2c[rolls[i]+1] : Integer.toString(rolls[i]));
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
