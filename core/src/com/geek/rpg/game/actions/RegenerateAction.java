package com.geek.rpg.game.actions;

import com.geek.rpg.game.Unit;
import com.geek.rpg.game.effects.RegenerationEffect;

public class RegenerateAction extends BaseAction {
    public RegenerateAction() {
        super("REGEN", "btnRegen");
    }

    @Override
    public boolean action(Unit me) {
        RegenerationEffect regenerationEffect = new RegenerationEffect();
        regenerationEffect.start(me, 3);
        me.addEffect(regenerationEffect);
        return false;
    }
}
