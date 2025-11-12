package com.syifa.frontend.obstacles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.Player;

public class HomingMissile extends BaseObstacle {
    private Player target;
    private Vector2 velocity;
    private float speed = 200f;
    private float width = 40f;
    private float height = 20f;

    public HomingMissile(Vector2 startPosition) {
        super(startPosition,0);
        this.velocity = new Vector2();
    }

    @Override
    public void initialize(Vector2 startPosition, int length) {
        super.initialize(startPosition, length);
        this.velocity.set(0, 0);
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public boolean isTargetingPlayer() {
        if (target == null) return false;
        float playerCenterX = target.getPosition().x + target.getWidth() / 2f;
        float missileCenterX = position.x + width / 2f;
        return playerCenterX <= missileCenterX;
    }

    public void update(float delta) {
        if (target == null || !active) return;

        if (isTargetingPlayer()) {
            Vector2 targetPosition = target.getPosition(); // Ambil Posisi Player
            velocity.set(targetPosition).sub(position).nor().scl(speed); // Mengatur velocity untuk mendekati player
        }

        // Always move with current velocity
        position.add(velocity.x * delta, velocity.y * delta);
        updateCollider();
    }

    @Override
    protected void updateCollider() {
        collider = new Rectangle(position.x, position.y, width, height);
    }

    @Override
    protected void drawShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(position.x, position.y, width, height);
    }

    @Override
    protected float getRenderWidth() {
        return width;
    }
}
