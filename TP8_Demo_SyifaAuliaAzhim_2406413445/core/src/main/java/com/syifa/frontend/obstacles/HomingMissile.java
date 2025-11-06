package com.syifa.frontend.obstacles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.syifa.frontend.Player;

public class HomingMissile extends BaseObstacle {

    private Player target;        // objek player yang dikejar
    private Vector2 velocity;     // vektor kecepatan
    private float speed = 200f;   // kecepatan konstan
    private float width = 40f;    // lebar misil
    private float height = 20f;   // tinggi misil

    // Constructor
    public HomingMissile(Vector2 startPosition) {
        super(startPosition, 0); // length tidak digunakan
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

        float targetMidX = target.getPosition().x + (target.getWidth() / 2f);
        float missileMidX = position.x + (width / 2f);
        return !(targetMidX > missileMidX);
    }

    public void update(float delta) {
        if (target == null || !active) return;

        if (isTargetingPlayer()) {
            Vector2 targetPosition = new Vector2(target.getPosition());
            velocity.set(targetPosition).sub(position).nor().scl(speed);
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;
            updateCollider();
        }
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

    @Override
    protected float getRenderHeight() {
        return height;
    }
}
