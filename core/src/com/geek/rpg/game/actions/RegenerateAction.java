package com.geek.rpg.game.actions;

import com.badlogic.gdx.graphics.Texture;
import com.geek.rpg.game.Assets;
import com.geek.rpg.game.Unit;
import com.geek.rpg.game.effects.RegenerationEffect;

/**
 * Created by slate on 21.11.2017.
 */

public class RegenerateAction extends BaseAction {
    public RegenerateAction() {
        super("REGEN", Assets.getInstance().getAssetManager().get("btnRegen.png", Texture.class));
    }

    @Override
    public boolean action(Unit me) {
        RegenerationEffect regenerationEffect = new RegenerationEffect();
        regenerationEffect.start(me.getBattleScreen().getInfoSystem(), me, 3);
        me.addEffect(regenerationEffect);
        return false;
    }
}
