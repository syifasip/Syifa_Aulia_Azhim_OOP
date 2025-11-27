package com.syifa.frontend.observers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreUIObserver implements Observer {

    private final BitmapFont font;
    private final SpriteBatch batch;

    public ScoreUIObserver() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        batch = new SpriteBatch();
    }

    @Override
    public void update(int score) {
        System.out.println("[ScoreUIObserver] Skor diperbarui: " + score);
        Gdx.app.log("ScoreUI", "Score updated: " + score);
    }

    public void render(int score) {
        batch.begin();
        font.draw(batch, "Score: " + score, 20, 460); // kiri atas layar
        font.draw(batch, "Score: " + score, 10, Gdx.graphics.getHeight() - 20);
        batch.end();
    }

    public void dispose() {
//        if (font != null)
            font.dispose();
//        if (batch != null)
            batch.dispose();
    }
}
