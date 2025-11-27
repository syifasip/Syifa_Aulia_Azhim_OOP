package com.syifa.frontend.pools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.syifa.frontend.Coin;

public class CoinPool extends Pool<Coin> {

    @Override
    protected Coin newObject() {
        return new Coin(new Vector2(0, 0));
    }

    @Override
    protected void reset(Coin coin) {
        coin.setActive(false);
    }

    public Coin obtain(float x, float y) {
        Coin c = super.obtain();
        c.setPosition(x, y);
        c.setActive(true);
        return c;
    }
}
