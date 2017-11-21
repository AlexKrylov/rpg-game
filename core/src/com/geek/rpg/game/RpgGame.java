package com.geek.rpg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RpgGame extends Game {
    private SpriteBatch batch;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    /**
     * 1) јвтоматизаци¤ процесса создани¤ и добавлени¤ спец. эффектов в игру - переработка класса SpecialFX по примеру actions и effects. ћожет быть по примеру создани¤ фабрики дл¤ Unit'ов.
     * 2) лично мой косяк при изменении разрешения текстур хелзбар и селектор немного сбиваются
     так же сбиваются эти тикстуры у юнитов с flip = true(разрешение у всех текстур одинаковое),
     кроме громоздких проверок решить проблему не удалось, подскажите как быть. Предлагаю
     ввести какие нибудь константы или типо того))) для фикирования вспомогательных текстур относительно
     текстуры юнита
     *3) если юниты игрока умирают то боты лупят их до бесконечности, опять же предлагаю проверку
     *
     *
     *
     *
     *
     *
     */

    @Override
    public void create() {
        batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.MENU);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        this.getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}