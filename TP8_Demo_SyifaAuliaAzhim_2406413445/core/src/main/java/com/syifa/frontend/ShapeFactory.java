package com.syifa.frontend;

public class ShapeFactory {
    ShapePool pool;

    public ShapeFactory(ShapePool pool) {
        this.pool = pool;
    }
    public Shape createShape(String type) {
        Shape shape = pool.obtain(type);
        if (shape != null) {
            return shape;
        }
        if (type.equalsIgnoreCase("Circle")) {
            return new Circle();
        } else if (type.equalsIgnoreCase("Square")) {
            return new Square();
        }
        return null;
    }
}
