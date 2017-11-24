package com.geek.rpg.game.actions;

import com.geek.rpg.game.Unit;

public class RestAction extends BaseAction {
    public RestAction() {
        super("REST", "btnHeal");
    }

    @Override
    public boolean action(Unit currentUnit) {
        currentUnit.changeHp((int)(currentUnit.getMaxHp() * 0.15f));
        return true;
    }
}
