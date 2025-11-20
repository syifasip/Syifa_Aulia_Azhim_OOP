package com.syifa.frontend.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.syifa.frontend.strategies.DifficultyStrategy;

public class DifficultyTransitionState implements GameState {

    private final GameStateManager gsm;
    private final PlayingState playingState;
    private final DifficultyStrategy newStrategy;

    private final BitmapFont font;
    private float timer = 2.0f;

    public DifficultyTransitionState(GameStateManager gsm, PlayingState playingState, DifficultyStrategy newStrategy) {
        this.gsm = gsm;
        this.playingState = playingState;
        this.newStrategy = newStrategy;
        this.font = new BitmapFont();
    }

    @Override
    public void update(float delta) {
        timer -= delta;

        if (timer <= 0) {
            playingState.setDifficulty(newStrategy);
            gsm.pop();
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        // Render gameplay as paused background
        playingState.render(batch);

        batch.begin();
        font.getData().setScale(2f);

        font.draw(batch,
            "DIFFICULTY INCREASED!",
            Gdx.graphics.getWidth() / 2f - 200,
            Gdx.graphics.getHeight() / 2f + 40);

        font.draw(batch,
            newStrategy.getClass().getSimpleName(),
            Gdx.graphics.getWidth() / 2f - 100,
            Gdx.graphics.getHeight() / 2f - 10);

        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
