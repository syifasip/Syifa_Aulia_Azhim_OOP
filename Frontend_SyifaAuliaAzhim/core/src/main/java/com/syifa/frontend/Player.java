package com.syifa.frontend;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {
    // atribut yang hanya diakses di dalam class ini
    private Vector2 position;
    private Vector2 velocity;
    private float gravity = 2000f;
    private float force = 4500f;
    private float maxVerticalSpeed = 700f;
    private Rectangle collider;
    private float width = 64f;
    private float height = 64f;

    // sistem kecepatan
    private float baseSpeed = 300f;
    private float distanceTraveled = 0f;

    // konstruktor publik
    public Player(Vector2 startPosition) {
        this.position = new Vector2(startPosition);
        this.velocity = new Vector2(baseSpeed, 0f);
        this.collider = new Rectangle(position.x, position.y, width, height);
    }

    // update publik: menggabungkan beberapa private step
    public void update(float delta, boolean isFlying) {
        updateDistanceAndSpeed(delta);
        applyGravity(delta);
        if (isFlying) {
            fly(delta);
        }
        updatePosition(delta);
        updateCollider();
    }

    // menambah jarak tempuh berdasarkan kecepatan saat ini
    private void updateDistanceAndSpeed(float delta) {
        // distance berdasarkan kecepatan horizontal dikalikan waktu
        distanceTraveled += velocity.x * delta;
    }

    // update posisi berdasarkan velocity
    private void updatePosition(float delta) {
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
    }

    // menerapkan gravitasi dan menjaga kecepatan vertikal dalam batas
    private void applyGravity(float delta) {
        velocity.y -= gravity * delta;

        // pastikan kecepatan horizontal tetap baseSpeed
        velocity.x = baseSpeed;

        // clamp vertical speed
        if (velocity.y > maxVerticalSpeed) {
            velocity.y = maxVerticalSpeed;
        }
        if (velocity.y < -maxVerticalSpeed) {
            velocity.y = -maxVerticalSpeed;
        }
    }

    // mekanik terbang: menambah dorongan ke atas
    private void fly(float delta) {
        velocity.y += force * delta;
    }

    // update collider agar sesuai posisi
    private void updateCollider() {
        collider.x = position.x;
        collider.y = position.y;
        collider.width = width;
        collider.height = height;
    }

    // cek batasan: jika menabrak ground atau melewati ceiling
    public void checkBoundaries(Ground ground, float ceilingY) {
        // ground collision
        if (ground.isColliding(collider)) {
            // tempatkan player tepat di atas ground
            float topY = ground.getTopY();
            position.y = topY; // ground Y adalah 0, getTopY() adalah tinggi -> top of ground relative to bottom (y=0)
            // Namun instruksi ingin player tidak menembus; jika getTopY() mengembalikan tinggi ground (50),
            // maka posisi y player harus = ground height
            // set kecepatan vertikal 0 agar tidak bergerak
            velocity.y = 0f;
            updateCollider();
        }

        // ceiling check (jangan melewati batas atas)
        if (position.y + height > ceilingY) {
            position.y = ceilingY - height;
            velocity.y = 0f;
            updateCollider();
        }

        // jangan jatuhkan player di bawah 0 (sebagai safety)
        if (position.y < 0f) {
            position.y = 0f;
            velocity.y = 0f;
            updateCollider();
        }
    }

    // render player (warna bebas)
    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(0.2f, 0.6f, 0.9f, 1f);
        shapeRenderer.rect(position.x, position.y, width, height);
    }

    // getters publik sesuai spesifikasi
    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getCollider() {
        return collider;
    }

    // jarak yang ditempuh dibagi 10 sesuai instruksi
    public float getDistanceTraveled() {
        return distanceTraveled / 10f;
    }

    // getter baseSpeed (jika butuh)
    public float getBaseSpeed() {
        return baseSpeed;
    }
}
