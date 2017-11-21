package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private Texture texture;

    public Background() {
        this.texture = Assets.getInstance().getAssetManager().get("background.png", Texture.class);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, 0, 0);
    }
}
