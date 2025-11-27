package com.syifa.frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.syifa.frontend.states.GameStateManager;
import com.syifa.frontend.states.MenuState;

public class Main extends Game {
    private GameStateManager gsm;
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        gsm = new GameStateManager();

        // Mulai dari MENU, bukan langsung gameplay
        gsm.push(new MenuState(gsm));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float delta = Gdx.graphics.getDeltaTime();
        gsm.update(delta);
        gsm.render(spriteBatch);
    }

    @Override
    public void dispose() {
        super.dispose();

        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
    }
}
