package com.syifa.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    // atribut internal
    private ShapeRenderer shapeRenderer;
    private Player player;
    private Ground ground;
    private GameManager gameManager;

    // camera system
    private OrthographicCamera camera;
    private float cameraOffset = 0.2f;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        gameManager = GameManager.getInstance();

        // buat camera ortografis sesuai ukuran layar
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(w, h);
        camera.setToOrtho(false);

        // inisialisasi player dan ground
        player = new Player(new Vector2(100f, Gdx.graphics.getHeight() / 2f));
        ground = new Ground();

        // mulai game
        gameManager.startGame();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // update game logic
        update(delta);

        // render
        ScreenUtils.clear(0.1f, 0.12f, 0.16f, 1f); // latar bebas (gelap biru)
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        ground.renderShape(shapeRenderer);
        player.renderShape(shapeRenderer);
        shapeRenderer.end();
    }

    // update internal
    private void update(float delta) {
        boolean isFlying = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        // update player
        player.update(delta, isFlying);

        // update camera & ground
        updateCamera(delta);
        ground.update(camera.position.x);

        // cek batas (ceiling = layar tinggi)
        player.checkBoundaries(ground, Gdx.graphics.getHeight());

        // update score berdasarkan jarak
        int newScore = (int) player.getDistanceTraveled();
        gameManager.setScore(newScore);
    }

    // update posisi kamera agar selalu mengikuti player
    private void updateCamera(float delta) {
        // cameraFocus: titik fokus sumbu-x pada camera berdasar posisi player dan offset
        float cameraFocus = player.getPosition().x + cameraOffset * Gdx.graphics.getWidth();
        camera.position.x = cameraFocus;
        // biarkan posisi y kamera di tengah layar; jika ingin mengikuti y, dapat diubah
        camera.update();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
