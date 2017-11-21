package com.geek.rpg.game.effects;

import com.geek.rpg.game.InfoSystem;
import com.geek.rpg.game.Unit;

public abstract class Effect {
    InfoSystem infoSystem;
    Unit unit;
    int rounds;

    public void start(InfoSystem infoSystem, Unit unit, int rounds) {
        this.infoSystem = infoSystem;
        this.unit = unit;
        this.rounds = rounds;
    }

    public void tick() {
        this.rounds--;
    }

    public boolean isEnded() {
        return rounds == 0;
    }

    public abstract void end();
}
