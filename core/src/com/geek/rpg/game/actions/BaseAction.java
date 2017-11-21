package com.geek.rpg.game.actions;

import com.badlogic.gdx.graphics.Texture;
import com.geek.rpg.game.Unit;

public abstract class BaseAction {
    String name;
    Texture btnTexture;

    public BaseAction(String name, Texture btnTexture) {
        this.name = name;
        this.btnTexture = btnTexture;
    }

    public String getName() {
        return name;
    }

    public Texture getBtnTexture() {
        return btnTexture;
    }

    public abstract boolean action(Unit me);
}
