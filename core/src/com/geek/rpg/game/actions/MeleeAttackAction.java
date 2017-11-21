package com.geek.rpg.game.actions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.geek.rpg.game.*;

public class MeleeAttackAction extends BaseAction {
    public MeleeAttackAction() {
        super("MELEE_ATTACK", Assets.getInstance().getAssetManager().get("btnMeleeAttack.png", Texture.class));
    }

    @Override
    public boolean action(Unit me) {
        if (me.getTarget() == null) return false;
        if (me.isMyTeammate(me.getTarget())) return false;
        me.setAttackAction(1.0f);
        me.setCurrentAnimation(Unit.AnimationType.ATTACK);
        if (!Calculator.isTargetEvaded(me, me.getTarget())) {
            int dmg = Calculator.getMeleeDamage(me, me.getTarget());
            me.getTarget().changeHp(-dmg);
        } else {
            me.getTarget().evade();
        }
        me.getBattleScreen().getSpecialFXEmitter().setup(me, me.getTarget(), 1.0f, 2f, 2f, true);
        me.getBattleScreen().getSpecialFXEmitter().setup(me.getTarget(), me.getTarget(), 3.0f, 2f, 20f, true);
        return true;
    }
}
