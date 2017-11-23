package com.geek.rpg.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.geek.rpg.game.screens.BattleScreen;
import com.geek.rpg.game.screens.MenuScreen;
import com.geek.rpg.game.screens.SecondMenuScreen;

public class ScreenManager {
    public enum ScreenType {
        MENU, BATTLE, SECONDMENU
    }

    private static final ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private RpgGame rpgGame;
    private Viewport viewport;
    private BattleScreen battleScreen;
    private MenuScreen menuScreen;
    private SecondMenuScreen secondMenuScreen;

    public Viewport getViewport() {
        return viewport;
    }

    public void init(RpgGame rpgGame, SpriteBatch batch) {
        this.rpgGame = rpgGame;
        this.battleScreen = new BattleScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.secondMenuScreen = new SecondMenuScreen(batch);
        this.viewport = new FitViewport(1280, 720);
        this.viewport.update(1280, 720, true);
        this.viewport.apply();
    }

    public void onResize(int width, int height) {
        viewport.update(width, height, true);
        viewport.apply();
    }

    private ScreenManager() {
    }

    public void switchScreen(ScreenType type) {
        Screen screen = rpgGame.getScreen();
        Assets.getInstance().clear();
        if (screen != null) {
            screen.dispose();
        }
        switch (type) {
            case MENU:
                Assets.getInstance().loadAssets(ScreenType.MENU);
                rpgGame.setScreen(menuScreen);
                break;
            case BATTLE:
                Assets.getInstance().loadAssets(ScreenType.BATTLE);
                rpgGame.setScreen(battleScreen);
                break;
            case SECONDMENU:
                Assets.getInstance().loadAssets(ScreenType.SECONDMENU);
                rpgGame.setScreen(secondMenuScreen);
        }
    }

    public void dispose() {
        Assets.getInstance().getAssetManager().dispose();
    }
}
