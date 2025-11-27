package com.syifa.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.syifa.frontend.services.BackendServices;
import com.syifa.frontend.observers.ScoreManager;

public class GameManager {

    private static GameManager instance;

    private ScoreManager scoreManager;
    private boolean gameActive;

    private BackendServices backendService;
    private String currentPlayerId = null;
    private int coinsCollected = 0;

    private GameManager() {
        scoreManager = new ScoreManager();
        gameActive = false;

        backendService = new BackendServices();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void startGame() {
        scoreManager.setScore(0);
        coinsCollected = 0;        // RESET koin
        gameActive = true;
        System.out.println("Game Started!");
    }

    public void endGame() {

        if (currentPlayerId == null) {
            Gdx.app.error("GameManager", "Cannot submit score: Player ID is null!");
            return;
        }

        int distance = scoreManager.getScore();
        int finalScore = distance + (coinsCollected * 10);

        backendService.submitScore(
            currentPlayerId,
            finalScore,
            coinsCollected,
            distance,
            new BackendServices.RequestCallback() {

                @Override
                public void onSuccess(String response) {
                    Gdx.app.log("GameManager", "Score submitted successfully: " + response);
                }

                @Override
                public void onError(String error) {
                    Gdx.app.error("GameManager", "Failed submitting score: " + error);
                }
            }
        );
    }

    public void registerPlayer(String username) {

        backendService.createPlayer(username, new BackendServices.RequestCallback() {

            @Override
            public void onSuccess(String response) {
                try {
                    JsonReader reader = new JsonReader();
                    JsonValue json = reader.parse(response);

                    currentPlayerId = json.getString("playerId");

                    Gdx.app.log("GameManager", "Player registered! ID = " + currentPlayerId);

                } catch (Exception e) {
                    Gdx.app.error("GameManager", "Parsing Error: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                Gdx.app.error("GameManager", "Register Error: " + error);
            }
        });
    }

    public void addCoin() {
        coinsCollected++;
        Gdx.app.log("COIN", "COIN COLLECTED! Total: " + coinsCollected);
    }

    public int getCoins() {
        return coinsCollected;
    }

    public void setScore(int distance) {
        if (gameActive) {
            scoreManager.setScore(distance);
        }
    }

    public int getScore() {
        return scoreManager.getScore();
    }

    public void addObserver(com.syifa.frontend.observers.Observer observer) {
        scoreManager.addObserver(observer);
    }

    public void removeObserver(com.syifa.frontend.observers.Observer observer) {
        scoreManager.removeObserver(observer);
    }
}
