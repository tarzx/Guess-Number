package uk.ac.standrews.cs5031.group;

public interface GuessNumber {
    
    public int[] getGuess();
    
    public int getCorrectValue();
    
    public int getCorrectPosition();
    
    public int getPlayer();
    
    public int getRound();

    public void checkNumber(int[] _target);    
    
}
