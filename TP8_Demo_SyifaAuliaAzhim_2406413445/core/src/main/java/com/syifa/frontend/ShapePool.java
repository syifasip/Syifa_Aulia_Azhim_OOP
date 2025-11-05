package com.syifa.frontend;

import java.awt.*;
import java.util.ArrayList;

public class ShapePool {
    ArrayList<Shape> circlePool = new ArrayList<>();
    ArrayList<Shape> squarePool = new ArrayList<>();
    int MAX_POOL = 3;

    public Shape obtain(String type) {
        ArrayList<Shape> pool = getPool(type);
        if (pool != null && !pool.isEmpty()) {
            return pool.remove(0);
        }
        return null;
    }
    public void release(Shape shape) {
        if (shape == null) return;

        ArrayList<Shape> pool = getPool(shape.getType());
        if (pool != null && pool.size() < MAX_POOL) {
            shape.reset();
            pool.add(shape);
        }
    }
    public ArrayList<Shape> getPool(String type) {
        if (type.equalsIgnoreCase("Circle")) {
            return circlePool;
        } else if (type.equalsIgnoreCase("Square")) {
            return squarePool;
        }
        return null;
    }
}
