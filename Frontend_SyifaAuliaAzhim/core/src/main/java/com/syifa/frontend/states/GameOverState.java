package com.syifa.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState implements GameState {
    private final GameStateManager gsm;
    private final BitmapFont font;

    public GameOverState(GameOverState gsm) {
        this.gsm = gsm;
        this.font =new BitmapFont();
    }


}
