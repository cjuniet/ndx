package net.juniet.ndx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final Random rng = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.rollResults);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void roll1D4(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(1, 4, 1));
    }
    public void roll1D6(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(1, 6, 1));
    }
    public void roll1D8(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(1, 8, 1));
    }
    public void roll1D10(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(1, 10, 0));
    }
    public void roll1D12(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(1, 12, 1));
    }
    public void roll1D20(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(1, 20, 1));
    }
    public void roll1D100(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(1, 100, 0));
    }
    public void roll1DF(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdF(1));
    }

    public void rollCustom1(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdF(4));
    }
    public void rollCustom2(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(4, 6, 1));
    }
    public void rollCustom3(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(20, 100, 0));
    }
    public void rollCustom4(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(1, 3, 1));
    }
    public void rollCustom5(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdF(4));
    }
    public void rollCustom6(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(4, 6, 1));
    }
    public void rollCustom7(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(20, 100, 0));
    }
    public void rollCustom8(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.append(rollNdX(1, 3, 1));
    }

    public void clearLogs(View view) {
        TextView text = (TextView) findViewById(R.id.rollResults);
        text.setText(null);
    }

    private String rollNdX(int n, int x, int offset) {
        StringBuilder sb = new StringBuilder();
        int sum = 0;
        if (n > 1) {
            sb.append(" [");
            for (int i = 0; i < n; ++i) {
                int r = rng.nextInt(x) + offset;
                sum += r;
                sb.append(r);
                if (i < n-1) sb.append(",");
            }
            sb.append("]");
        } else {
            sum = rng.nextInt(x) + offset;
        }
        sb.insert(0, Integer.toString(sum));
        sb.insert(0, " = ");
        sb.insert(0, Integer.toString(x));
        sb.insert(0, "D");
        sb.insert(0, Integer.toString(n));
        sb.append("\n\n");
        return sb.toString();
    }

    private String rollNdF(int n) {
        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        int sum = 0;
        for (int i = 0; i < n; ++i) {
            switch (rng.nextInt(3)) {
                case 0:
                    --sum;
                    sb.append("-");
                    break;
                case 1:
                    sb.append("o");
                    break;
                case 2:
                    ++sum;
                    sb.append("+");
                    break;
            }
        }
        sb.append("]");
        sb.insert(0, Integer.toString(sum));
        sb.insert(0, "DF = ");
        sb.insert(0, Integer.toString(n));
        sb.append("\n\n");
        return sb.toString();
    }
}
