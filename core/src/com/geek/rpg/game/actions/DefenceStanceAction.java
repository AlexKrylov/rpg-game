package com.geek.rpg.game.actions;
import com.geek.rpg.game.Unit;
import com.geek.rpg.game.effects.DefenceStanceEffect;

public class DefenceStanceAction extends BaseAction {
    public DefenceStanceAction() {
        super("DEFENCE", "btnDefence");
    }

    @Override
    public boolean action(Unit me) {
        DefenceStanceEffect dse = new DefenceStanceEffect();
        dse.start(me, 1);
        me.addEffect(dse);
        return true;
    }
}
