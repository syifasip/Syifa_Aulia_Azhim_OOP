package com.syifa.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
    private Texture backgroundTexture;
    private TextureRegion backgroundRegion;
    private float width, height;
    private float currentCameraX = 0f;

    public Background() {
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundRegion = new TextureRegion(backgroundTexture);
        width = 2688f;
        height = 1536f;
    }

    public void update(float cameraX) {
        this.currentCameraX = cameraX;
    }

    public void render(SpriteBatch batch) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float scale = screenHeight / height;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        float startX = -((currentCameraX % scaledWidth));

        for (float x = startX - scaledWidth; x < screenWidth; x += scaledWidth) {
            batch.draw(backgroundRegion, x, 0, scaledWidth, scaledHeight);
        }
    }

    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
