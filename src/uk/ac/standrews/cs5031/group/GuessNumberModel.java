package uk.ac.standrews.cs5031.group;

public interface GuessNumberModel {

    public int[] getTarget();
    
    public int getNumberOfPlayer();
    
    public void addGuessNumber(int[] guess, int player, int round);
    
    public GuessNumberImpl getLastGuessNumber();
    
    public boolean isWin();
    
    public GuessNumberImpl getWinner();
    
}
