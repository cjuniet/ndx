package net.juniet.ndx;

// represents a dice roll result

import java.util.ArrayList;

public class DiceRollResult {
    private static final char[] f2c = {'-','o','+'};
    private final DiceRollModel mModel;
    private ArrayList<Integer> rolls;
    private int sum;

    public DiceRollResult(DiceRollModel model) {
        mModel = model;
        rolls = new ArrayList<>(model.getNumberOfDice());
        sum = 0;
    }

    public void addRoll(int r) {
        rolls.add(r);
        sum += r;
    }

    public void addAdjustment(int mAdjustment) {
        sum += mAdjustment;
    }

    public String toString() {
        final boolean isFUDGE = mModel.isFUDGE();
        StringBuilder sb = new StringBuilder();
        if (rolls.size() == 1 && mModel.getAdjustment() == 0) {
            sb.append(isFUDGE ? f2c[sum+1] : Integer.toString(sum));
        } else {
            sb.append(Integer.toString(sum));
            sb.append(" [");
            for (int i = 0; i < rolls.size(); ++i) {
                if (i > 0 && !isFUDGE) sb.append(",");
                sb.append(isFUDGE ? f2c[rolls.get(i)+1] : rolls.get(i).toString());
            }
            sb.append("]");
        }
        return sb.toString();
    }
}