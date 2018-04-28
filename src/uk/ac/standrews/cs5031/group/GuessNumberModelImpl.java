package uk.ac.standrews.cs5031.group;

import java.util.ArrayList;
import java.util.Random;

public class GuessNumberModelImpl implements GuessNumberModel{
    
    public static final int NUMBER_LENGTH = 5;
    
    private int[] _target;
    private int _numberOfPlayer;
    private ArrayList<GuessNumberImpl> _guessList = new ArrayList<GuessNumberImpl>();
    private boolean _isWin = false;
    
    public GuessNumberModelImpl(int numberOfPlayer) {
        this._numberOfPlayer = numberOfPlayer;
        generateTarget();
        
        for (int i=0; i<_target.length; i++)
            System.out.print(_target[i] + " ");
        System.out.println("");
    }
      
    private void generateTarget() {
        Random rnd = new Random();
        _target = new int[NUMBER_LENGTH];
        for (int i=0; i<NUMBER_LENGTH; i++) {
            _target[i] = rnd.nextInt(10);
        }
    }
    
    public int[] getTarget() {
        return _target;
    }
    
    public int getNumberOfPlayer() {
        return _numberOfPlayer;
    }
    
    public void addGuessNumber(int[] guess, int player, int round) {
        if (!_isWin) {
            GuessNumberImpl guessObj = new GuessNumberImpl(NUMBER_LENGTH, guess, player, round);
            guessObj.checkNumber(_target);
            _isWin = checkNumber(guessObj.getCorrectValue(), guessObj.getCorrectPosition());
            _guessList.add(guessObj);
        }
    }
    
    private boolean checkNumber(int correctValue, int correctPosition) {
        return (correctValue == NUMBER_LENGTH) && (correctPosition == NUMBER_LENGTH);
    }
    
    public GuessNumberImpl getLastGuessNumber() {
        return _guessList.get(_guessList.size() - 1);
    }
    
    public boolean isWin() {
        return _isWin;
    }
    
    public GuessNumberImpl getWinner() {
        GuessNumberImpl winner = null;
        if (_isWin) {
            winner = getLastGuessNumber();
        }
        return winner;
    }
}
