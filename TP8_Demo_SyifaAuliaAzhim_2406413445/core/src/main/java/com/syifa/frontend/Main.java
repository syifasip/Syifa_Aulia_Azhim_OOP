package com.syifa.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.Random;

public class Main extends ApplicationAdapter {
    ShapeRenderer shapeRenderer;
    ShapePool shapePool;
    ShapeFactory shapeFactory;
    ArrayList<Shape> activeShapes;
    private Random random;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        shapePool = new ShapePool();
        shapeFactory = new ShapeFactory(shapePool);
        activeShapes = new ArrayList<>();
        random = new Random();

        System.out.println("Press 1=Circle, 2=Square, R=Release");
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)|| Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
            createShape("Circle");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            createShape("Square");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            releaseAllShapes();
        }
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Shape s : activeShapes) {
            s.render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    private void createShape(String type) {
        if (activeShapes.size() >= 3) {
            System.out.println("Maximum 3 shapes active!");
            return;
        }

        Shape shape = shapeFactory.createShape(type);
        if (shape != null) {
            float x = random.nextFloat() * (Gdx.graphics.getWidth() - 100) + 50;
            float y = random.nextFloat() * (Gdx.graphics.getHeight() - 100) + 50;
            shape.setPosition(x, y);

            activeShapes.add(shape);
        }
    }

    private void releaseAllShapes() {
        for (Shape s : activeShapes) {
            shapePool.release(s);
        }
        activeShapes.clear();
        System.out.println("All shapes returned to pool");
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
