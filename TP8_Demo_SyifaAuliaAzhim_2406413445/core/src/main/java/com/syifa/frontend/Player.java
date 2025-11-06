package com.syifa.frontend;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {
    private Vector2 position;
    private Vector2 velocity;
    private float gravity = 2000f;
    private float force = 4500f;
    private float maxVerticalSpeed = 700f;
    private Rectangle collider;
    private float width = 64f;
    private float height = 64f;

    // Speed system
    private float baseSpeed = 300f;
    private float distanceTraveled = 0f;
    private boolean isDead = false;
    private Vector2 startPosition;

    public Player(Vector2 startPosition) {
        this.startPosition = new Vector2(startPosition);
        position = new Vector2(startPosition);

        collider = new Rectangle(
            position.x,
            position.y,
            width,
            height
        );
        velocity = new Vector2(baseSpeed, 0);
    }

    public void update(float delta, boolean isFlying) {
        if (isDead) {
            return;
        }

        updateDistanceAndSpeed(delta);
        applyGravity(delta);
        if (isFlying) {
            fly(delta);
        }
        updatePosition(delta);
        updateCollider();
    }


    private void updateDistanceAndSpeed(float delta) {
        // Track distance traveled
        distanceTraveled += velocity.x * delta;
    }

    private void updatePosition(float delta) {
        // Move forward constantly
        position.x += velocity.x * delta;
        // Apply vertical movement (gravity/jetpack)
        position.y += velocity.y * delta;
    }

    private void applyGravity(float delta) {
        velocity.y -= gravity * delta;
        // Keep forward speed constant with current speed
        velocity.x = baseSpeed;

        // Clamp vertical velocity to max speed
        if (velocity.y < -maxVerticalSpeed) {
            velocity.y = -maxVerticalSpeed;
        } else if (velocity.y > maxVerticalSpeed) {
            velocity.y = maxVerticalSpeed;
        }
    }

    private void fly(float delta) {
        velocity.y += force * delta;
    }

    private void updateCollider() {
        collider.setPosition(position.x, position.y);
    }

    public void checkBoundaries(Ground ground, float ceilingY) {
        // Ground collision
        if (ground.isColliding(collider)) {
            position.y = ground.getTopY();
            velocity.y = 0;
        }

        // Ceiling collision
        if (position.y + height > ceilingY) {
            position.y = ceilingY - height;
            velocity.y = 0;
        }
    }

    public void die() {
        isDead = true;
        velocity.set(0, 0); // hentikan pergerakan player
    }

    public void reset() {
        isDead = false;
        position.set(startPosition);
        velocity.set(baseSpeed, 0);
        distanceTraveled = 0f;
    }

    public boolean isDead() {
        return isDead;
    }

    // Debug
    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(0f, 1f, 0f, 1f);
        shapeRenderer.rect(position.x, position.y, width, height);
    }

    // Getters
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

    public float getDistanceTraveled() {
        return distanceTraveled / 10f;
    }
}
