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
import com.syifa.frontend.factories.CoinFactory;
import com.syifa.frontend.factories.ObstacleFactory;
import com.syifa.frontend.observers.ScoreUIObserver;
import com.syifa.frontend.obstacles.BaseObstacle;
import com.syifa.frontend.obstacles.HomingMissile;
import com.syifa.frontend.strategies.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
    private float coinSpawnTimer = 0f;
    private final List<CoinPattern> coinPatterns;
    private final Random random;


    private float obstacleSpawnTimer;
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
        this.random = new Random();


        this.coinFactory = new CoinFactory();
        this.coinPatterns = new ArrayList<>();
        this.coinPatterns.add(new LinePattern());
        this.coinPatterns.add(new RectanglePattern());


        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);


        player = new Player(new Vector2(100, screenHeight / 2f));
        ground = new Ground();
        background = new Background();


        jetpackCommand = new JetpackCommand(player);


        scoreUIObserver = new ScoreUIObserver();
        GameManager.getInstance().addObserver(scoreUIObserver);


        obstacleFactory = new ObstacleFactory();
        setDifficulty(new EasyDifficultyStrategy());


        obstacleSpawnTimer = 0f;


        GameManager.getInstance().startGame();
    }


    public void setDifficulty(DifficultyStrategy newStrategy) {
        this.difficultyStrategy = newStrategy;
        this.obstacleFactory.setWeights(newStrategy.getObstacleWeights());
        System.out.println("Difficulty changed to: " + newStrategy.getClass().getSimpleName());
    }


    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jetpackCommand.execute();
        }


        if (player.isDead()) {
            GameManager.getInstance().endGame();
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


        int currentScoreMeters = (int) player.getDistanceTraveled();
        GameManager.getInstance().setScore(currentScoreMeters);


        if (currentScoreMeters > lastLoggedScore) {
            lastLoggedScore = currentScoreMeters;
        }


        updateDifficulty(currentScoreMeters);
    }


    private void updateDifficulty(int score) {
        if (score > 1000 && !(difficultyStrategy instanceof HardDifficultyStrategy)) {
            if (score > 2000) {
                gsm.push(new DifficultyTransitionState(gsm, this, new HardDifficultyStrategy()));
            } else if (!(difficultyStrategy instanceof MediumDifficultyStrategy)) {
                gsm.push(new DifficultyTransitionState(gsm, this, new MediumDifficultyStrategy()));
            }
        }
    }


    @Override
    public void render(SpriteBatch batch) {
        if (spriteBatch == null) {
            spriteBatch = new SpriteBatch();
        }


        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        background.render(spriteBatch);
//        spriteBatch.end();
//
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


//        player.renderShape(shapeRenderer);
        //baru
        player.render(spriteBatch);
        for (BaseObstacle obstacle : obstacleFactory.getAllInUseObstacles()) {
            // Cek jika obstacle punya kemampuan render gambar (seperti HomingMissile)
            obstacle.render(spriteBatch);
        }
        spriteBatch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // batas


        shapeRenderer.setColor(Color.RED);
        for (BaseObstacle obstacle : obstacleFactory.getAllInUseObstacles()) {
//            obstacle.render(shapeRenderer);
            //baru
            if (!(obstacle instanceof HomingMissile)) {
                obstacle.render(shapeRenderer);
            }
            //batas
        }


        for (Coin coin : coinFactory.getActiveCoins()) {
            coin.renderShape(shapeRenderer);
        }


        shapeRenderer.end();


        scoreUIObserver.render(GameManager.getInstance().getScore(), GameManager.getInstance().getCoins());


    }


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
        float spawnAfterLastObstacle = lastObstacleSpawnX + difficultyStrategy.getMinGap();
        float baseSpawnX = Math.max(spawnAheadOfCamera, spawnAfterLastObstacle);


        for (int i = 0; i < difficultyStrategy.getDensity(); i++) {
            float spawnX = baseSpawnX + (i * OBSTACLE_CLUSTER_SPACING);
            obstacleFactory.createRandomObstacle(ground.getTopY(), spawnX, player.getHeight());
            lastObstacleSpawnX = spawnX;
        }
    }


    private void updateCoins(float delta) {
        coinSpawnTimer += delta;
        float spawnInterval = 2f + random.nextFloat() * 2f;
        if (coinSpawnTimer >= spawnInterval) {
            spawnCoins();
            coinSpawnTimer = 0f;
        }
        float cameraLeft = camera.position.x - screenWidth / 2f;
        for (Coin coin : coinFactory.getActiveCoins()) {
            coin.update(delta);
            if (coin.isOffScreenCamera(cameraLeft)) {
                coinFactory.releaseCoin(coin);
            }
        }
    }



    private void spawnCoins() {
        float cameraRightEdge = camera.position.x + screenWidth / 2f;
        float spawnX = cameraRightEdge + 100f;


        if (!coinPatterns.isEmpty()) {
            CoinPattern pattern = coinPatterns.get(random.nextInt(coinPatterns.size()));
            pattern.spawn(coinFactory, ground.getTopY(), spawnX, screenHeight);
        }
    }


    private void checkCollisions() {
        Rectangle playerCollider = player.getCollider();
        for (BaseObstacle obstacle : obstacleFactory.getAllInUseObstacles()) {
            if (obstacle.isColliding(playerCollider)) {
                player.die();
                return;
            }
        }
    }


    private void checkCoinCollisions() {
        Rectangle playerCollider = player.getCollider();
        for (Coin coin : coinFactory.getActiveCoins()) {
            if (coin.isColliding(playerCollider)) {
                coin.setActive(false);
                GameManager.getInstance().addCoin();
            }
        }
    }


    @Override
    public void dispose() {
        shapeRenderer.dispose();
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
        obstacleFactory.releaseAllObstacles();
        scoreUIObserver.dispose();
        background.dispose();
        coinFactory.releaseAll();
    }
}
