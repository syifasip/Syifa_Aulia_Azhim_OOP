package com.syifa.frontend.states;

import java.util.Stack;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameStateManager {
    private final Stack<GameState> states;

    public GameStateManager() {
        states = new Stack<>();
    }

    public void push(GameState state) {
        states.push(state);
    }
    public void pop() {
        states.pop().dispose();
    }

    public void set(GameState state) {
        pop();
        push(state);
    }

    public void update(float delta) {
        states.peek().update(delta);
    }

    public void render(SpriteBatch batch) {
        states.peek().render(batch);
    }
}
