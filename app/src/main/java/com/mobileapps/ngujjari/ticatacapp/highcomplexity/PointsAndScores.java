package com.mobileapps.ngujjari.ticatacapp.highcomplexity;

public class PointsAndScores {
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    int score;
	Move move;

    PointsAndScores(int score, Move point) {
        this.score = score;
        this.move = point;
    }


}
