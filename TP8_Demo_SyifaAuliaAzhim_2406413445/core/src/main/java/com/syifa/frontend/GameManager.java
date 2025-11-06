package com.syifa.frontend;

public class GameManager {
    private static GameManager instance;

    private int score;
    private boolean gameActive;

    private GameManager() {
        score = 0;
        gameActive = false;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void startGame() {
        score = 0;
        gameActive = true;
        System.out.println("Game Started!");
    }

    public void setScore(int newScore) {
        if (gameActive) {
            score = newScore;
        }
    }

    public void resetScore() {
        score = 0;
    }

    // Getters
    public int getScore() { return score; }
}
