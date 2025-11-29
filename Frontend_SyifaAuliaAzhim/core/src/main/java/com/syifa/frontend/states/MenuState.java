package com.syifa.frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;   // ✔ FIX
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.syifa.frontend.GameManager;

public class MenuState implements GameState {

    private GameStateManager gsm;
    private Stage stage;
    private Skin skin;
    private TextField nameField;
    private TextButton startButton;

    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;
        this.stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        createBasicSkin();
        buildUI();
    }

    private void createBasicSkin() {
        skin = new Skin();

        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // --- TEXTURES ---
        Pixmap pixmapWhite = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapWhite.setColor(Color.WHITE);
        pixmapWhite.fill();
        Texture texWhite = new Texture(pixmapWhite);
        skin.add("white", texWhite);

        Pixmap pixmapGray = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapGray.setColor(Color.LIGHT_GRAY);
        pixmapGray.fill();
        Texture texGray = new Texture(pixmapGray);
        skin.add("gray", texGray);

        Pixmap pixmapDark = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapDark.setColor(Color.DARK_GRAY);
        pixmapDark.fill();
        Texture texDark = new Texture(pixmapDark);
        skin.add("dark_gray", texDark);

        // --- LABEL STYLE ---
        Label.LabelStyle lblStyle = new Label.LabelStyle();
        lblStyle.font = font;
        lblStyle.fontColor = Color.WHITE;
        skin.add("default", lblStyle);

        // --- TEXTFIELD STYLE (FIXED) ---
        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = font;
        tfStyle.fontColor = Color.WHITE;
        tfStyle.cursor = new TextureRegionDrawable(new TextureRegion(texWhite));    // ✔ FIX
        tfStyle.selection = new TextureRegionDrawable(new TextureRegion(texGray));  // ✔ FIX
        tfStyle.background = new TextureRegionDrawable(new TextureRegion(texDark)); // ✔ FIX
        skin.add("default-textfield", tfStyle);

        // --- TEXTBUTTON STYLE ---
        TextButton.TextButtonStyle tbStyle = new TextButton.TextButtonStyle();
        tbStyle.font = font;
        tbStyle.up = new TextureRegionDrawable(new TextureRegion(texGray));
        tbStyle.down = new TextureRegionDrawable(new TextureRegion(texWhite));
        tbStyle.over = new TextureRegionDrawable(new TextureRegion(texDark));
        skin.add("default-textbutton", tbStyle);

        pixmapWhite.dispose();
        pixmapGray.dispose();
        pixmapDark.dispose();
    }

    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("NETLAB JOYRIDE", skin, "default");
        title.setFontScale(2f);
        title.setAlignment(Align.center);

        Label prompt = new Label("Enter Your Name:", skin, "default");

        nameField = new TextField("", skin, "default-textfield");
        nameField.setMessageText("Username...");
        nameField.setAlignment(Align.center);
        nameField.setMaxLength(20);

        startButton = new TextButton("START GAME", skin, "default-textbutton");
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String inputName = nameField.getText();
                if (inputName == null || inputName.trim().isEmpty()) {
                    inputName = "Guest";
                }

                GameManager.getInstance().registerPlayer(inputName);

                gsm.set(new PlayingState(gsm));
            }
        });

        table.add(title).padBottom(30f).width(400f).height(80f).row();
        table.add(prompt).padBottom(10f).row();
        table.add(nameField).width(300f).height(40f).padBottom(20f).row();
        table.add(startButton).width(220f).height(50f);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
