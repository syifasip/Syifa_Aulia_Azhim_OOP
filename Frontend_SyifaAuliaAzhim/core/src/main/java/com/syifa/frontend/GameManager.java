package com.syifa.frontend;

public class GameManager {
    // instance hanya dapat diakses di dalam class ini
    private static GameManager instance;

    // atribut internal
    private int score;
    private boolean gameActive;

    // constructor private agar hanya diakses di dalam class ini
    private GameManager() {
        this.score = 0;
        this.gameActive = false;
    }

    // method static untuk mendapatkan instance (singleton)
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // mulai game: reset score dan set gameActive
    public void startGame() {
        this.score = 0;
        this.gameActive = true;
        System.out.println("Game Started!");
    }

    // update score hanya jika game aktif
    public void setScore(int newScore) {
        if (gameActive) {
            this.score = newScore;
        }
    }

    // getter score
    public int getScore() {
        return score;
    }

    // setter/getter tambahan jika perlu
    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean active) {
        this.gameActive = active;
    }
}
