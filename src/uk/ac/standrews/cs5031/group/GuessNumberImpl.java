package uk.ac.standrews.cs5031.group;

public class GuessNumberImpl implements GuessNumber {
    
    private int _length;
    private int[] _guess;
    private int _correctValue;
    private int _correctPosition;
    private int _player;
    private int _round;
    
    public GuessNumberImpl(int length, int[] guess, int player, int round) {
        this._length = length;
        this._guess = guess;
        this._player = player;
        this._round = round;
    }
    
    public int[] getGuess() {
        return _guess;
    }
    
    public int getCorrectValue() {
        return _correctValue;
    }
    
    public int getCorrectPosition() {
        return _correctPosition;
    }
    
    public int getPlayer() {
        return _player;
    }
    
    public int getRound() {
        return _round;
    }
    
    public void checkNumber(int[] target) {
        _correctValue = 0;
        _correctPosition = 0;
        
        int[] copyTarget = new int[_length];
        System.arraycopy(target, 0, copyTarget, 0, _length);
        
        for (int idx=0; idx<_length; idx++) {
            if (_guess[idx] == target[idx]) {
                _correctPosition++;
            }
            
            for (int i=0; i<_length; i++) {
                if (_guess[idx] == copyTarget[i]) {
                    _correctValue++;
                    copyTarget[i] = -1;                    
                    break;
                }
            }
        }
    }
    
}
