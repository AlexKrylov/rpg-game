package com.geek.rpg.game;

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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.geek.rpg.game.actions.BaseAction;
import com.geek.rpg.game.actions.DefenceStanceAction;
import com.geek.rpg.game.actions.MeleeAttackAction;
import com.geek.rpg.game.actions.RestAction;

import java.util.ArrayList;
import java.util.List;

public class BattleScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private Background background;
    private List<Unit> units;
    private int currentUnitIndex;
    private Unit currentUnit;
    private TextureRegion textureSelector;
    private InfoSystem infoSystem;
    private UnitFactory unitFactory;
    private Vector2[][] stayPoints;
    private float animationTimer;
    private Stage stage;
    private Skin skin;
    private MyInputProcessor mip;
    private SpecialFXEmitter specialFXEmitter;
    private Group actionPanel;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private int level;

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
        Hero player1 = new Hero();
        Hero player2 = new Hero();

        unitFactory = new UnitFactory(this);
        unitFactory.createUnitPatterns();
        units = new ArrayList<Unit>();

        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.KNIGHT, this, player1, true, 1, 0);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.KNIGHT, this, player1, true, 1, 1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.KNIGHT, this, player1, true, 1, 2);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, player1, true, 0, 0);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, player1, true, 0, 1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, player1, true, 0, 2);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, player2, false, 2, 0);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, player2, false, 2, 1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.SKELETON, this, player2, false, 2, 2);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.KNIGHT, this, player2, false, 3, 1);


        mip = new MyInputProcessor();
        Gdx.input.setInputProcessor(mip);
        background = new Background();

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

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("menuBtn");
        textButtonStyle.font = font;
        skin.add("tbs", textButtonStyle);
        Button btnExit = new TextButton("MENU", skin, "tbs");
        btnExit.setPosition(980, 600);
        stage.addActor(btnExit);

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.SECONDMENU);
            }
        });

        List<BaseAction> list = unitFactory.getActions();
        for (BaseAction o : list) {
            skin.add(o.getName(), o.getBtnTexture());
            Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttonStyle.up = skin.newDrawable(o.getName());
            skin.add(o.getName(), buttonStyle);
        }
        for (Unit o : units) {
            if (!o.isAI()) {
                actionPanel = new Group();
                Image image = new Image(Assets.getInstance().getAssetManager().get("actionPanel.png", Texture.class));
                actionPanel.addActor(image);
                actionPanel.setPosition(1280 / 2 - 840 / 2, 5);
                actionPanel.setVisible(false);
                o.setActionPanel(actionPanel);
                stage.addActor(actionPanel);

                int counter = 0;
                for (BaseAction a : o.getActions()) {
                    final BaseAction ba = a;
                    Button btn = new Button(skin, a.getName());
                    btn.setPosition(30 + counter * 100, 30);
                    btn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (!currentUnit.isAI()) {
                                if (ba.action(currentUnit)) {
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

    public boolean isGameOver(List<Unit> units) {
        int enemyCount = 0;
        int alliesCount = 0;
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
            if (mip.isTouchedInArea(units.get(i).getRect()) && units.get(i).isAlive()) {
                currentUnit.setTarget(units.get(i));
            }
        }
        if (!isHeroTurn()) {
            unitFactory.setLevel(unitFactory.getLevel() + 1);
            if (currentUnit.getAutopilot().turn(currentUnit)) {
                nextTurn();
            }
        }
        infoSystem.update(dt);
        specialFXEmitter.update(dt);

        if (isGameOver(units)) {
            currentUnit.getActionPanel().setVisible(false);
            Button btnChangeLevel = new TextButton("CHANGE LEVEL", skin, "tbs");
            btnChangeLevel.setPosition(400, 400);
            stage.addActor(btnChangeLevel);

            btnChangeLevel.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                }
            });

            Button btnStayAtThisLevel = new TextButton("STAY AT THIS LEVEL", skin, "tbs");
            btnStayAtThisLevel.setPosition(400, 300);
            stage.addActor(btnStayAtThisLevel);

            btnStayAtThisLevel.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

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
        background.render(batch);
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

        infoSystem.render(batch, font);
        specialFXEmitter.render(batch);
        batch.end();
        stage.draw();
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
