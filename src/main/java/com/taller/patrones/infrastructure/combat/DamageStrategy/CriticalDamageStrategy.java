package com.taller.patrones.infrastructure.combat.DamageStrategy;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public class CriticalDamageStrategy implements DamageStrategy {

    @Override
    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        int raw = attacker.getAttack() * attack.getBasePower() / 100;
        if (Math.random() < 0.2)
            raw = (int) (raw * 1.5);
        return Math.max(1, raw - defender.getDefense());
    }
}
