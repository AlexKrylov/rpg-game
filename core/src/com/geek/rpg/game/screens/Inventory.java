package com.geek.rpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.geek.rpg.game.Assets;
import com.geek.rpg.game.GameSession;
import com.geek.rpg.game.ScreenManager;

public class Inventory implements Screen {
    private Texture backgroundTexture;
    private Texture buttonTexture;
    private BitmapFont font96;
    private BitmapFont font36;
    private Music music;
    private SpriteBatch batch;

    private Stage stage;
    private Skin skin;
    private float time;

    public Inventory(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        backgroundTexture = Assets.getInstance().getAssetManager().get("background.png", Texture.class);
        buttonTexture = Assets.getInstance().getAssetManager().get("menuBtn.png", Texture.class);
        music = Gdx.audio.newMusic(Gdx.files.internal("Jumping bat.wav"));
        music.setLooping(true);
        music.play();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("zorque.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 96;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = -3;
        parameter.shadowOffsetY = 3;
        parameter.color = Color.WHITE;
        font96 = generator.generateFont(parameter);
        parameter.size = 36;
        font36 = generator.generateFont(parameter);
        generator.dispose();
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        skin.add("textureButton", buttonTexture);
        skin.add("font36", font36);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("textureButton");
        textButtonStyle.font = font36;
        skin.add("tbs", textButtonStyle);

    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        font96.draw(batch, "INVENORY", 0, 600 + 20.0f * (float) Math.sin(time), 1280, 1, false);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        time += dt;
        stage.act(dt);
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().onResize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        music.dispose();
        backgroundTexture.dispose();
        font36.dispose();
        font96.dispose();
    }
}

