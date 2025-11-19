package com.syifa.frontend;

import com.syifa.frontend.observers.ScoreManager;


public class GameManager {
    private static GameManager instance;

    private ScoreManager scoreManager;
    private boolean gameActive;

    private GameManager() {
        scoreManager = new ScoreManager();
        gameActive = false;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void startGame() {
        scoreManager.setScore(0);
        gameActive = true;
        System.out.println("Game Started!");
    }

    public void setScore(int newScore) {
        if (gameActive) {
            scoreManager.setScore(newScore);
        }
    }

    // Getters
    public int getScore() { return scoreManager.getScore(); }

    // Delegate observer methods to ScoreManager
    public void addObserver(com.syifa.frontend.observers.Observer observer) {
        scoreManager.addObserver(observer);
    }

    public void removeObserver(com.syifa.frontend.observers.Observer observer) {
        scoreManager.removeObserver(observer);
    }
}
