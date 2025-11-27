package com.syifa.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.syifa.frontend.*;
import com.syifa.frontend.commands.Command;
import com.syifa.frontend.commands.JetpackCommand;
import com.syifa.frontend.factories.ObstacleFactory;
import com.syifa.frontend.factories.CoinFactory;
import com.syifa.frontend.obstacles.BaseObstacle;
import com.syifa.frontend.obstacles.HomingMissile;
import com.syifa.frontend.coins.Coin;
import com.syifa.frontend.observers.ScoreUIObserver;
import com.syifa.frontend.strategies.*;

public class PlayingState implements GameState {

    private final GameStateManager gsm;

    private final ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private final Player player;
    private final Ground ground;
    private final Background background;
    private final Command jetpackCommand;
    private final ScoreUIObserver scoreUIObserver;

    private final ObstacleFactory obstacleFactory;
    private final CoinFactory coinFactory;

    private float obstacleSpawnTimer = 0f;
    private float coinSpawnTimer = 0f;

    private float lastObstacleSpawnX = 0f;
    private static final float SPAWN_AHEAD_DISTANCE = 300f;
    private static final float OBSTACLE_CLUSTER_SPACING = 250f;

    private final OrthographicCamera camera;
    private final float cameraOffset = 0.2f;

    private final int screenWidth;
    private final int screenHeight;

    private int lastLoggedScore = -1;

    private DifficultyStrategy difficultyStrategy;

    public PlayingState(GameStateManager gsm) {
        this.gsm = gsm;
        this.shapeRenderer = new ShapeRenderer();

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        this.player = new Player(new Vector2(100, screenHeight / 2f));
        this.ground = new Ground();
        this.background = new Background();

        this.jetpackCommand = new JetpackCommand(player);
        this.scoreUIObserver = new ScoreUIObserver();
        GameManager.getInstance().addObserver(scoreUIObserver);

        this.obstacleFactory = new ObstacleFactory();
        this.coinFactory = new CoinFactory(); // Tambahan

        setDifficulty(new EasyDifficultyStrategy());

        GameManager.getInstance().startGame();
    }

    public void setDifficulty(DifficultyStrategy strategy) {
        this.difficultyStrategy = strategy;
        this.obstacleFactory.setWeights(strategy.getObstacleWeights());
    }

    @Override
    public void update(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jetpackCommand.execute();
        }

        if (player.isDead()) {
            GameManager.getInstance().endGame(); // Tambahan
            gsm.set(new GameOverState(gsm));
            return;
        }

        player.update(delta, false);
        updateCamera(delta);
        background.update(camera.position.x);
        ground.update(camera.position.x);
        player.checkBoundaries(ground, screenHeight);

        updateObstacles(delta);
        updateCoins(delta);
        checkCollisions();
        checkCoinCollisions();

        int scoreMeters = (int) player.getDistanceTraveled();
        GameManager.getInstance().setScore(scoreMeters);

        if (scoreMeters > lastLoggedScore) {
            System.out.println("Distance: " + scoreMeters + "m");
            lastLoggedScore = scoreMeters;
        }

        updateDifficulty(scoreMeters);
    }

    private void updateDifficulty(int s) {
        if (s > 2000 && !(difficultyStrategy instanceof HardDifficultyStrategy)) {
            gsm.push(new DifficultyTransitionState(gsm, this, new HardDifficultyStrategy()));
        } else if (s > 1000 && !(difficultyStrategy instanceof MediumDifficultyStrategy)) {
            gsm.push(new DifficultyTransitionState(gsm, this, new MediumDifficultyStrategy()));
        }
    }

    private void updateCamera(float delta) {
        float focus = player.getPosition().x + screenWidth * cameraOffset;
        camera.position.x = focus;
        camera.update();
    }

    private void updateObstacles(float delta) {
        obstacleSpawnTimer += delta;

        if (obstacleSpawnTimer >= difficultyStrategy.getSpawnInterval()) {
            spawnObstacle();
            obstacleSpawnTimer = 0;
        }

        float cameraLeft = camera.position.x - screenWidth / 2f;

        for (BaseObstacle obs : obstacleFactory.getAllInUseObstacles()) {
            if (obs instanceof HomingMissile) {
                ((HomingMissile) obs).setTarget(player);
                ((HomingMissile) obs).update(delta);
            }
            if (obs.isOffScreenCamera(cameraLeft)) {
                obstacleFactory.releaseObstacle(obs);
            }
        }
    }

    private void spawnObstacle() {
        float cameraRight = camera.position.x + screenWidth / 2f;
        float spawnX = Math.max(
                cameraRight + SPAWN_AHEAD_DISTANCE,
                lastObstacleSpawnX + difficultyStrategy.getMinGap()
        );

        for (int i = 0; i < difficultyStrategy.getDensity(); i++) {
            float x = spawnX + i * OBSTACLE_CLUSTER_SPACING;
            obstacleFactory.createRandomObstacle(ground.getTopY(), x, player.getHeight());
            lastObstacleSpawnX = x;
        }
    }

    // 🔥 NEW: update coin logic
    private void updateCoins(float delta) {

        coinSpawnTimer += delta;

        if (coinSpawnTimer > 0.5f) {
            float spawnX = camera.position.x + screenWidth;
            coinFactory.createCoinPattern(spawnX, ground.getTopY());
            coinSpawnTimer = 0f;
        }

        float cameraLeft = camera.position.x - screenWidth / 2f;

        for (Coin c : coinFactory.getActiveCoins()) {
            c.update(delta);
            if (c.isOffScreenCamera(cameraLeft)) {
                coinFactory.releaseCoin(c);
            }
        }
    }

    private void checkCollisions() {
        Rectangle collider = player.getCollider();
        for (BaseObstacle obs : obstacleFactory.getAllInUseObstacles()) {
            if (obs.isColliding(collider)) {
                player.die();
                return;
            }
        }
    }

    // 🔥 NEW: coin collision
    private void checkCoinCollisions() {
        Rectangle collider = player.getCollider();
        for (Coin c : coinFactory.getActiveCoins()) {
            if (c.isColliding(collider)) {
                c.setInactive();
                GameManager.getInstance().addCoin();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (spriteBatch == null) spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        background.render(spriteBatch);
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        player.renderShape(shapeRenderer);

        for (BaseObstacle obs : obstacleFactory.getAllInUseObstacles()) {
            shapeRenderer.setColor(Color.RED);
            obs.render(shapeRenderer);
        }

        // 🔥 NEW: render coins
        for (Coin c : coinFactory.getActiveCoins()) {
            shapeRenderer.setColor(Color.YELLOW);
            c.renderShape(shapeRenderer);
        }

        shapeRenderer.end();

        scoreUIObserver.render(
                GameManager.getInstance().getScore(),
                GameManager.getInstance().getCoins()
        );
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        if (spriteBatch != null) spriteBatch.dispose();

        obstacleFactory.releaseAllObstacles();
        coinFactory.releaseAll(); // NEW
        scoreUIObserver.dispose();
        background.dispose();
    }
}
