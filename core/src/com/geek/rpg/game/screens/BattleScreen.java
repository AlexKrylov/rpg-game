package com.geek.rpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.geek.rpg.game.Assets;
import com.geek.rpg.game.GameSession;
import com.geek.rpg.game.Hero;
import com.geek.rpg.game.InfoSystem;
import com.geek.rpg.game.MyInputProcessor;
import com.geek.rpg.game.ScreenManager;
import com.geek.rpg.game.SpecialFXEmitter;
import com.geek.rpg.game.Unit;
import com.geek.rpg.game.UnitFactory;
import com.geek.rpg.game.actions.BaseAction;

import java.util.ArrayList;
import java.util.List;

public class BattleScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont font36;
    private List<Unit> units;
    private int currentUnitIndex;
    private Unit currentUnit;
    private TextureRegion textureSelector;
    private Texture textureBackground;
    private InfoSystem infoSystem;
    private UnitFactory unitFactory;
    private Vector2[][] stayPoints;
    private float animationTimer;
    private Stage stage;
    private Skin skin;
    private MyInputProcessor mip;
    private SpecialFXEmitter specialFXEmitter;
    private int levelUp;
    private int enemyCount, alliesCount;
    private BitmapFont font96;

    public SpecialFXEmitter getSpecialFXEmitter() {
        return specialFXEmitter;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public Vector2[][] getStayPoints() {
        return stayPoints;
    }

    public boolean canIMakeTurn() {
        return animationTimer <= 0.0f;
    }

    public InfoSystem getInfoSystem() {
        return infoSystem;
    }

    public BattleScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        final int LEFT_STAYPOINT_X = 280;
        final int TOP_STAYPOINT_Y = 400;
        final int DISTANCE_BETWEEN_UNITS_X = 160;
        final int DISTANCE_BETWEEN_UNITS_Y = 120;
        final int DISTANCE_BETWEEN_TEAMS = 100;
        stayPoints = new Vector2[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int x = LEFT_STAYPOINT_X + i * DISTANCE_BETWEEN_UNITS_X + j * 20;
                if (i > 1) x += DISTANCE_BETWEEN_TEAMS;
                stayPoints[i][j] = new Vector2(x, TOP_STAYPOINT_Y - j * DISTANCE_BETWEEN_UNITS_Y);
            }
        }
        Hero player1 = GameSession.getInstance().getPlayer();
        Hero player2 = new Hero();
        unitFactory = new UnitFactory();

        player2.setArmy(
                unitFactory.createUnit(UnitFactory.UnitType.KNIGHT, true, true, 1),
                unitFactory.createUnit(UnitFactory.UnitType.ARCHER, true, true, 1),
                unitFactory.createUnit(UnitFactory.UnitType.SKELETON, true, true, 1),
                unitFactory.createUnit(UnitFactory.UnitType.MAGE, true, true, 1),
                null, null
        );

        units = new ArrayList<Unit>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (player1.getUnits()[i][j] != null) {
                    unitFactory.reloadUnit(player1.getUnits()[i][j]);
                    player1.getUnits()[i][j].setToMap(this, i, j);
                    units.add(player1.getUnits()[i][j]);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (player2.getUnits()[i][j] != null) {
                    units.add(player2.getUnits()[i][j]);
                    player2.getUnits()[i][j].setToMap(this, i + 2, j);
                }
            }
        }

        mip = new MyInputProcessor();
        Gdx.input.setInputProcessor(mip);
        textureBackground = Assets.getInstance().getAssetManager().get("background.png", Texture.class);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("zorque.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = -2;
        parameter.shadowOffsetY = 2;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        infoSystem = new InfoSystem();
        textureSelector = Assets.getInstance().getAtlas().findRegion("selector");

        currentUnitIndex = 0;
        currentUnit = units.get(currentUnitIndex);
        specialFXEmitter = new SpecialFXEmitter();
        createGUI();
        InputMultiplexer im = new InputMultiplexer(stage, mip);
        Gdx.input.setInputProcessor(im);
        animationTimer = 0.0f;
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        skin = new Skin(Assets.getInstance().getAtlas());
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("zorque.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        font36 = generator.generateFont(parameter);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("menuBtn");
        textButtonStyle.font = font36;
        skin.add("tbs", textButtonStyle);
        Button btnExit = new TextButton("MENU", skin, "tbs");
        btnExit.setPosition(1020, 650);
        btnExit.setSize(250, 70);
        stage.addActor(btnExit);

        Button btnShop = new TextButton("SHOP", skin, "tbs");
        btnShop.setPosition(1020, 550);
        btnShop.setSize(250, 70);
        stage.addActor(btnShop);

        Button btnInventory = new TextButton("INVENTORY", skin, "tbs");
        btnInventory.setPosition(1020, 450);
        btnInventory.setSize(250, 70);
        stage.addActor(btnInventory);

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSession.getInstance().saveSession();
                ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.MENU);
            }
        });

        btnShop.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSession.getInstance().saveSession();

            }
        });

        btnInventory.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSession.getInstance().saveSession();
                ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.INVENTORY);
            }
        });

        List<BaseAction> list = unitFactory.getActions();

        for (BaseAction o : list) {
            Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttonStyle.up = skin.newDrawable(o.getTextureName());
            skin.add(o.getName(), buttonStyle);
        }
        for (Unit o : units) {
            if (!o.isAI()) {
                Group actionPanel = new Group();
                Image image = new Image(Assets.getInstance().getAtlas().findRegion("actionPanel"));
                actionPanel.addActor(image);
                actionPanel.setPosition(1280 / 2 - 840 / 2, 5);
                actionPanel.setVisible(false);
                o.setActionPanel(actionPanel);
                stage.addActor(actionPanel);

                int counter = 0;
                final Unit innerUnit = o;
                for (BaseAction a : o.getActions()) {
                    final BaseAction ba = a;
                    Button btn = new Button(skin, a.getName());
                    btn.setPosition(30 + counter * 100, 30);
                    btn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (!innerUnit.isAI()) {
                                if (ba.action(innerUnit)) {
                                    nextTurn();
                                }
                            }
                        }
                    });
                    actionPanel.addActor(btn);
                    counter++;
                }
            }
        }
    }

    public boolean isHeroTurn() {
        return currentUnit.getAutopilot() == null;
    }

    public void nextTurn() {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getActionPanel() != null) {
                units.get(i).getActionPanel().setVisible(false);
            }
        }
        do {
            currentUnitIndex++;
            if (currentUnitIndex >= units.size()) {
                currentUnitIndex = 0;
            }
        } while (!units.get(currentUnitIndex).isAlive());
        currentUnit = units.get(currentUnitIndex);
        currentUnit.getTurn();
        animationTimer = 1.0f;
        if (currentUnit.getActionPanel() != null) {
            currentUnit.getActionPanel().setVisible(true);
        }
    }

    public void update(float dt) {
        if (isHeroTurn() && canIMakeTurn()) {
            stage.act(dt);
            if (currentUnit.getActionPanel() != null) {
                currentUnit.getActionPanel().setVisible(true);
            }
        }
        if (animationTimer > 0.0f) {
            animationTimer -= dt;
        }
        for (int i = 0; i < units.size(); i++) {
            units.get(i).update(dt);
            if (mip.isTouchedInArea(units.get(i).getRect()) && units.get(i).isAlive() && currentUnit.isAlive()) {
                currentUnit.setTarget(units.get(i));
            }
        }
        if (!isHeroTurn()) {
            if (currentUnit.getAutopilot().turn(currentUnit)) {
                nextTurn();
            }
        }
        infoSystem.update(dt);
        specialFXEmitter.update(dt);

        if (isGameOver(units)) {

            if (isHeroTurn()) {
                currentUnit.getActionPanel().setVisible(false);
            }
            Button btnChangeLevel = new TextButton("CHANGE LEVEL", skin, "tbs");
            btnChangeLevel.setPosition(400, 400);
            stage.addActor(btnChangeLevel);

            btnChangeLevel.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    levelUp++;
                    ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.BATTLE);
                    for (int i = 0; i < units.size(); i++) {
                        if (units.get(i).getAutopilot() != null) {
                            units.get(i).setLevelTo(units.get(i).getLevel() + levelUp);
                        }
                    }
                }
            });

            Button btnStayAtThisLevel = new TextButton("STAY AT THIS LEVEL", skin, "tbs");
            btnStayAtThisLevel.setPosition(400, 300);
            stage.addActor(btnStayAtThisLevel);

            btnStayAtThisLevel.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.BATTLE);
                    for (int i = 0; i < units.size(); i++) {
                        if (units.get(i).getAutopilot() != null) {
                            units.get(i).setLevelTo(units.get(i).getLevel() + levelUp);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(textureBackground, 0, 0);
        batch.setColor(1, 1, 0, 0.8f);
        batch.draw(textureSelector, currentUnit.getPosition().x, currentUnit.getPosition().y - 5);
        if (isHeroTurn() && currentUnit.getTarget() != null) {
            batch.setColor(1, 0, 0, 0.8f);
            batch.draw(textureSelector, currentUnit.getTarget().getPosition().x, currentUnit.getTarget().getPosition().y - 5);
            currentUnit.getTarget().showStats(batch, font);
        }
        batch.setColor(1, 1, 1, 1);
        for (int i = 0; i < units.size(); i++) {
            units.get(i).render(batch);
            if (units.get(i).isAlive()) {
                units.get(i).renderInfo(batch, font);
            }
        }
        if(isGameOver(units)) {
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
            if (enemyCount > alliesCount) {
                batch.draw(textureBackground, 0, 0);
                font96.draw(batch, "YOU LOSE", 0, 670 , 1280, 1, false);
            } else {
                batch.draw(textureBackground, 0, 0);
                font96.draw(batch, "YOU WIN", 0, 670, 1280, 1, false);
            }
        }
        infoSystem.render(batch, font);
        specialFXEmitter.render(batch);
        batch.end();
        stage.draw();
    }

    public boolean isGameOver(List<Unit> units) {
        enemyCount = 0;
        alliesCount = 0;
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).isAlive() && units.get(i).isAI()) {
                enemyCount++;
            }
        }
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).isAlive() && !units.get(i).isAI()) {
                alliesCount++;
            }
        }
        if (enemyCount == 0 || alliesCount == 0) return true;
        return false;
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
    }
}
