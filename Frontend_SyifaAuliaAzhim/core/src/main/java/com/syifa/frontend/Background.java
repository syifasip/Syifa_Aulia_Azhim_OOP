package com.syifa.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
    private Texture backgroundTexture;
    private TextureRegion backgroundRegion;
    private float width;
    private float height;
    private float currentCameraX = 0f;

    public Background() {
        // Load background image (2688x1536)
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundRegion = new TextureRegion(backgroundTexture);

        this.width = 2688f; // Full width of background image
        this.height = 1536f;
    }

    public void update(float cameraX) {
        // Store camera position for use in render
        this.currentCameraX = cameraX;
    }

    public void render(SpriteBatch batch) {
        // Calculate which background segments to render
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Scale to fit screen height while maintaining aspect ratio
        float scale = screenHeight / height;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        // Calculate starting position based on stored camera position
        float startX = (float)Math.floor(currentCameraX / scaledWidth) * scaledWidth;

        // Draw background tiles to cover entire screen and beyond
        for (float x = startX - scaledWidth; x < currentCameraX + screenWidth + scaledWidth; x += scaledWidth) {
            batch.draw(backgroundRegion, x, 0, scaledWidth, scaledHeight);
        }
    }

    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
