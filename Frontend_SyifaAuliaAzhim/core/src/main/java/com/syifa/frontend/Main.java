package com.syifa.frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.syifa.frontend.commands.Command;
import com.syifa.frontend.commands.JetpackCommand;
import com.syifa.frontend.commands.RestartCommand;
import com.syifa.frontend.factories.ObstacleFactory;
import com.syifa.frontend.observers.ScoreUIObserver;
import com.syifa.frontend.obstacles.BaseObstacle;
import com.syifa.frontend.obstacles.HomingMissile;

public class Main extends Game {
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    // Game objects
    private Player player;
    private Ground ground;
    private GameManager gameManager;

    private Background background;

    private Command jetpackCommand;
    private Command restartCommand;

    private ScoreUIObserver scoreUIObserver;

    private ObstacleFactory obstacleFactory;
    private float obstacleSpawnTimer;
    private float lastObstacleSpawnX = 0f;
    private static final float OBSTACLE_SPAWN_INTERVAL = 2.5f;
    private static final int OBSTACLE_DENSITY = 1;
    private static final float SPAWN_AHEAD_DISTANCE = 300f;
    private static final float MIN_OBSTACLE_GAP = 200f;
    private static final float OBSTACLE_CLUSTER_SPACING = 250f;

    private OrthographicCamera camera;
    private float cameraOffset = 0.2f;

    private int screenWidth;
    private int screenHeight;
    private int lastLoggedScore = -1;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        this.gameManager = GameManager.getInstance();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        player = new Player(new Vector2(100, screenHeight / 2f));
        ground = new Ground();

        jetpackCommand = new JetpackCommand(player);
        restartCommand = new RestartCommand(player, gameManager);

        scoreUIObserver = new ScoreUIObserver();

        gameManager.addObserver(scoreUIObserver);

        background = new Background();

        obstacleFactory = new ObstacleFactory();
        obstacleSpawnTimer = 0f;

        gameManager.startGame();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        update(delta);
        renderGame(shapeRenderer);
    }

    private void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jetpackCommand.execute();
        }

        if (player.isDead()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                restartCommand.execute();
            }
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
        int previousScoreMeters = gameManager.getScore();
        if (currentScoreMeters > previousScoreMeters) {
            if (currentScoreMeters != lastLoggedScore) {
                System.out.println("Distance: " + currentScoreMeters + "m");
                lastLoggedScore = currentScoreMeters;
            }
            gameManager.setScore(currentScoreMeters);
        }
    }

    private void renderGame(ShapeRenderer shapeRenderer) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        if (spriteBatch == null) {
            spriteBatch = new SpriteBatch();
        }

        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        background.render(spriteBatch);
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        ground.renderShape(shapeRenderer);
        player.renderShape(shapeRenderer);

        shapeRenderer.setColor(Color.RED);
        for (BaseObstacle obstacle : obstacleFactory.getAllInUseObstacles()) {
            obstacle.render(shapeRenderer);
        }
        shapeRenderer.end();

        scoreUIObserver.render(gameManager.getScore());
    }

    private void updateCamera(float delta) {
        float cameraFocus = player.getPosition().x + screenWidth * cameraOffset;
        camera.position.x = cameraFocus;
        camera.update();
    }

    private void updateObstacles(float delta) {
        obstacleSpawnTimer += delta;
        if (obstacleSpawnTimer >= OBSTACLE_SPAWN_INTERVAL) {
            spawnObstacle();
            obstacleSpawnTimer = 0f;
        }

        float cameraLeftEdge = camera.position.x - screenWidth / 2f;
        for (BaseObstacle obstacle : obstacleFactory.getAllInUseObstacles()) {
            if (obstacle instanceof HomingMissile) {
                ((HomingMissile) obstacle).setTarget(player);
                ((HomingMissile) obstacle).update(delta);
            }
            if (obstacle.isOffScreenCamera(cameraLeftEdge)) {
                obstacleFactory.releaseObstacle(obstacle);
            }
        }
    }

    private void spawnObstacle() {
        float cameraRightEdge = camera.position.x + screenWidth / 2f;
        float spawnAheadOfCamera = cameraRightEdge + SPAWN_AHEAD_DISTANCE;
        float spawnAfterLastObstacle = lastObstacleSpawnX + MIN_OBSTACLE_GAP;
        float baseSpawnX = Math.max(spawnAheadOfCamera, spawnAfterLastObstacle);

        for (int i = 0; i < OBSTACLE_DENSITY; i++) {
            float spawnX = baseSpawnX + (i * OBSTACLE_CLUSTER_SPACING);
            obstacleFactory.createRandomObstacle(ground.getTopY(), spawnX, player.getHeight());
            lastObstacleSpawnX = spawnX;
        }
    }

    private void checkCollisions() {
        Rectangle playerCollider = player.getCollider();
        for (BaseObstacle obstacle : obstacleFactory.getAllInUseObstacles()) {
            if (obstacle.isColliding(playerCollider)) {
                System.out.println("==============================================");
                System.out.println("GAME OVER!");
                System.out.println("Press SPACE to restart");
                System.out.println("==============================================");
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
