package com.geek.rpg.game.effects;


import com.geek.rpg.game.FlyingText;
import com.geek.rpg.game.InfoSystem;
import com.geek.rpg.game.Unit;

public class RegenerationEffect extends Effect {
    @Override
    public void start(InfoSystem infoSystem, Unit unit, int rounds) {
        super.start(infoSystem, unit, rounds);
        infoSystem.addMessage("Regeneration 3T/+2HP", unit, FlyingText.Colors.GREEN);
    }

    @Override
    public void tick() {
        super.tick();
        infoSystem.addMessage("Regeneration", unit, FlyingText.Colors.GREEN);
        unit.changeHp((int)(unit.getMaxHp() * 0.05f));
    }

    @Override
    public void end() {
        infoSystem.addMessage("Regeneration end", unit, FlyingText.Colors.WHITE);
    }
}
