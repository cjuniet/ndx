package net.juniet.ndx;

// represents a dice roll expression

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiceRollModel {
    private final Random rng = new Random();
    private final Pattern reg = Pattern.compile("(\\d+)d(\\d+|F)([+-]\\d+)?");

    private int mNumberOfDice;
    private int mNumberOfFaces;
    private int mOffset;
    private int mAdjustment;
    private boolean mIsFUDGE;

    public DiceRollModel(String s) {
        parseFormula(s);
    }

    public int getNumberOfDice() { return mNumberOfDice; }
    public void setNumberOfDice(int n) { mNumberOfDice = n; }

    public int getNumberOfFaces() { return mNumberOfFaces; }
    public void setNumberOfFaces(int x) { mIsFUDGE = false; mNumberOfFaces = x; mOffset = 1; }

    public int getOffset() { return mOffset; }
    public void setOffset(int d) { mOffset = d; }

    public int getAdjustment() { return mAdjustment; }
    public void setAdjustment(int d) { mAdjustment = d; }

    public boolean isFUDGE() { return mIsFUDGE; }
    public void setFUDGE() { mIsFUDGE = true; mNumberOfFaces = 3; mOffset = -1; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mNumberOfDice).append("d");
        sb.append(mIsFUDGE ? "F" : mNumberOfFaces);
        if (mAdjustment > 0) {
            sb.append("+").append(mAdjustment);
        } else if (mAdjustment < 0) {
            sb.append(mAdjustment);
        }
        return sb.toString();
    }

    public boolean parseFormula(String s) {
        Matcher m = reg.matcher(s);
        if (!m.matches()) return false;

        setNumberOfDice(Integer.parseInt(m.group(1)));
        if (m.group(2).equals("F")) {
            setFUDGE();
        } else {
            setNumberOfFaces(Integer.parseInt(m.group(2)));
        }
        setAdjustment(m.group(3) != null ? Integer.parseInt(m.group(3)) : 0);
        return true;
    }

    public DiceRollResult getRollResult() {
        DiceRollResult result = new DiceRollResult(this);
        for (int i = 0; i < mNumberOfDice; ++i) {
            int r = rng.nextInt(mNumberOfFaces) + mOffset;
            result.addRoll(r);
        }
        result.addAdjustment(mAdjustment);
        return result;
    }
}