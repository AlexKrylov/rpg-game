package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.geek.rpg.game.actions.BaseAction;
import com.geek.rpg.game.actions.DefenceStanceAction;
import com.geek.rpg.game.actions.MeleeAttackAction;
import com.geek.rpg.game.actions.RegenerateAction;
import com.geek.rpg.game.actions.RestAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FlameXander on 13.11.2017.
 */

public class UnitFactory {
    private int level;
    public enum UnitType {
        KNIGHT, SKELETON;
    }

    private BattleScreen battleScreen;
    private Map<UnitType, Unit> data;
    private List<Autopilot> aiBank;
    private List<BaseAction> actions;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<BaseAction> getActions() {
        return actions;
    }

    public UnitFactory(BattleScreen battleScreen) {
        this.battleScreen = battleScreen;
        this.createActions();
        this.aiBank = new ArrayList<Autopilot>();
        this.aiBank.add(new Autopilot() {
            @Override
            public boolean turn(Unit me) {
                if (!me.getBattleScreen().canIMakeTurn()) {
                    return false;
                }
                Unit target = null;
                do {
                    target = me.getBattleScreen().getUnits().get((int) (Math.random() * me.getBattleScreen().getUnits().size()));
                } while (target.isAI());
                me.setTarget(target);
                me.getActions().get(0).action(me);
                return true;
            }
        });
        this.createUnitPatterns();
    }

    public void createActions() {
        this.actions = new ArrayList<BaseAction>();
        this.actions.add(new MeleeAttackAction());
        this.actions.add(new DefenceStanceAction());
        this.actions.add(new RestAction());
        this.actions.add(new RegenerateAction());
    }

    public void createUnitPatterns() {
        data = new HashMap<UnitType, Unit>();
        Stats knightStats = new Stats(1, 1000, 10, 30, 2, 5, 1.0f, 0.2f, 1.0f, 0.2f, 1.0f);
        Unit knight = new Unit(Assets.getInstance().getAtlas().findRegion("knightAnim"), knightStats);
        knight.getActions().add(actions.get(0));
        knight.getActions().add(actions.get(1));
        data.put(UnitType.KNIGHT, knight);

        Stats skeletonStats = new Stats(1, 1000, 20, 15, 1, 0, 0.5f, 1.0f, 1.0f, 0.2f, 0.1f);
        Unit skeleton = new Unit(Assets.getInstance().getAtlas().findRegion("skeleton"), skeletonStats);
        skeleton.getActions().add(actions.get(0));
        skeleton.getActions().add(actions.get(2));
        skeleton.getActions().add(actions.get(3));
        data.put(UnitType.SKELETON, skeleton);
    }

    public Unit createUnit(UnitType unitType, boolean isHuman) {
        Unit unitPattern = data.get(unitType);
        Unit unit = new Unit(unitPattern.getTexture(), (Stats) unitPattern.getStats().clone());
        unit.setActions(unitPattern.getActions());
        if (!isHuman) {
            unit.setAutopilot(aiBank.get(0));
        }
        return unit;
    }

    public void createUnitAndAddToBattle(UnitType unitType, BattleScreen battleScreen, Hero hero, boolean isHuman, int x, int y) {
        Unit unit = createUnit(unitType, isHuman);
        unit.setToMap(battleScreen, x, y);
        unit.setHero(hero);
        if (!isHuman) {
            unit.setFlip(true);
        }
        battleScreen.getUnits().add(unit);
    }
}
