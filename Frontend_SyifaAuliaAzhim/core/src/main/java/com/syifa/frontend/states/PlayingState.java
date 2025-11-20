package com.syifa.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.syifa.frontend.Background;
import com.syifa.frontend.GameManager;
import com.syifa.frontend.Ground;
import com.syifa.frontend.Player;

import com.syifa.frontend.commands.Command;
import com.syifa.frontend.commands.JetpackCommand;

import com.syifa.frontend.observers.ScoreUIObserver;

import com.syifa.frontend.factories.ObstacleFactory;

import com.syifa.frontend.obstacles.BaseObstacle;
import com.syifa.frontend.obstacles.HomingMissile;

import com.syifa.frontend.strategies.DifficultyStrategy;
import com.syifa.frontend.strategies.EasyDifficultyStrategy;
import com.syifa.frontend.strategies.MediumDifficultyStrategy;
import com.syifa.frontend.strategies.HardDifficultyStrategy;

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

    private float obstacleSpawnTimer = 0f;
    private float lastObstacleSpawnX = 0f;

    private static final float SPAWN_AHEAD_DISTANCE = 300f;
    private static final float CLUSTER_SPACING = 250f;

    private final OrthographicCamera camera;
    private final float cameraOffset = 0.2f;

    private final int screenWidth;
    private final int screenHeight;

    private int lastLoggedScore = -1;

    private DifficultyStrategy difficultyStrategy;

    public PlayingState(GameStateManager gsm) {
        this.gsm = gsm;

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        shapeRenderer = new ShapeRenderer();

        // Game objects
        player = new Player(new Vector2(100, screenHeight / 2f));
        ground = new Ground();
        background = new Background();

        // Patterns
        jetpackCommand = new JetpackCommand(player);

        scoreUIObserver = new ScoreUIObserver();
        GameManager.getInstance().addObserver(scoreUIObserver);

        obstacleFactory = new ObstacleFactory();

        // NOMOR 12 → SET DIFFICULTY AWAL
        setDifficulty(new EasyDifficultyStrategy());

        GameManager.getInstance().startGame();
    }

    // ============== NOMOR 12 — setDifficulty() ====================

    public void setDifficulty(DifficultyStrategy newStrategy) {
        this.difficultyStrategy = newStrategy;

        obstacleFactory.setWeights(newStrategy.getObstacleWeights());

        System.out.println("Difficulty changed to: "
            + newStrategy.getClass().getSimpleName());
    }

    // ======================== UPDATE ==========================

    @Override
    public void update(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jetpackCommand.execute();
        }

        // NOMOR 11 — jika mati langsung set GameOverState
        if (player.isDead()) {
            gsm.set(new GameOverState(gsm));
            return;
        }

        player.update(delta, false);
        updateCamera(delta);

        background.update(camera.position.x);
        ground.update(camera.position.x);

        player.checkBoundaries(ground, screenHeight);

        updateObstacles(delta);
        checkCollisions();

        int currentScoreMeters = (int) player.getDistanceTraveled();
        GameManager.getInstance().setScore(currentScoreMeters);

        updateDifficulty(currentScoreMeters);
    }

    // ==================== NOMOR 11 — updateDifficulty() ================

    private void updateDifficulty(int score) {

        if (score > 2000 && !(difficultyStrategy instanceof HardDifficultyStrategy)) {
            gsm.push(new DifficultyTransitionState(gsm, this, new HardDifficultyStrategy()));
        }
        else if (score > 1000 && !(difficultyStrategy instanceof MediumDifficultyStrategy)) {
            gsm.push(new DifficultyTransitionState(gsm, this, new MediumDifficultyStrategy()));
        }
    }

    // ===================== RENDER =====================

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

        shapeRenderer.setColor(Color.RED);

        for (BaseObstacle obs : obstacleFactory.getAllInUseObstacles()) {
            obs.render(shapeRenderer);
        }

        shapeRenderer.end();

        scoreUIObserver.render(GameManager.getInstance().getScore());
    }

    // ================== PRIVATE METHODS ======================

    private void updateCamera(float delta) {
        float cameraFocus = player.getPosition().x + screenWidth * cameraOffset;
        camera.position.x = cameraFocus;
        camera.update();
    }

    private void updateObstacles(float delta) {
        obstacleSpawnTimer += delta;

        if (obstacleSpawnTimer >= difficultyStrategy.getSpawnInterval()) {
            spawnObstacle();
            obstacleSpawnTimer = 0f;
        }

        float cameraLeftEdge = camera.position.x - (screenWidth / 2f);

        for (BaseObstacle obs : obstacleFactory.getAllInUseObstacles()) {

            if (obs instanceof HomingMissile) {
                HomingMissile hm = (HomingMissile) obs;
                hm.setTarget(player);
                hm.update(delta);
            }

            if (obs.isOffScreenCamera(cameraLeftEdge)) {
                obstacleFactory.releaseObstacle(obs);
            }
        }
    }

    private void spawnObstacle() {
        float cameraRightEdge = camera.position.x + (screenWidth / 2f);

        float spawnAhead = cameraRightEdge + SPAWN_AHEAD_DISTANCE;

        float spacedSpawn = lastObstacleSpawnX + difficultyStrategy.getMinGap();

        float baseSpawnX = Math.max(spawnAhead, spacedSpawn);

        for (int i = 0; i < difficultyStrategy.getDensity(); i++) {

            float spawnX = baseSpawnX + (i * CLUSTER_SPACING);

            obstacleFactory.createRandomObstacle(ground.getTopY(), spawnX, player.getHeight());

            lastObstacleSpawnX = spawnX;
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

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        if (spriteBatch != null) spriteBatch.dispose();
        obstacleFactory.releaseAllObstacles();
        scoreUIObserver.dispose();
        background.dispose();
    }
}
