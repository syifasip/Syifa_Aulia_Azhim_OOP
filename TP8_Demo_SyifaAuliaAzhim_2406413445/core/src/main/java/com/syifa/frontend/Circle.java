package com.syifa.frontend;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Circle extends Shape {
    public Circle() {
        type = "Circle";
    }

    @Override
    public void render(ShapeRenderer renderer) {
        renderer.circle(position.x, position.y,size / 2f);
    }
}
