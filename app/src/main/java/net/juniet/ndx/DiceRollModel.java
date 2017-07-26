package net.juniet.ndx;

// represents a dice roll expression

class DiceRollModel {
    private int mNumberOfDice;
    private int mNumberOfFaces;
    private boolean mIsFUDGE;

    DiceRollModel(String s) {
        parseFormula(s);
    }

    public int getNumberOfDice() { return mNumberOfDice; }
    public void setNumberOfDice(int n) { mNumberOfDice = n; }

    public int getNumberOfFaces() { return mNumberOfFaces; }
    public void setNumberOfFaces(int x) { mIsFUDGE = false; mNumberOfFaces = x; }

    public boolean isFUDGE() { return mIsFUDGE; }
    public void setFUDGE() { mIsFUDGE = true; mNumberOfFaces = 3; }

    public String toString() { return mNumberOfDice + "d" + (mIsFUDGE ? "F" : mNumberOfFaces); }

    public void parseFormula(String s) {
        String f = s.toUpperCase();
        String sn = f.substring(0, f.indexOf('D'));
        String sx = f.substring(f.indexOf('D')+1);
        setNumberOfDice(Integer.parseInt(sn));
        if (sx.equals("F")) {
            setFUDGE();
        } else {
            setNumberOfFaces(Integer.parseInt(sx));
        }
    }
}