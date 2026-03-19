package com.taller.patrones.infrastructure.combat.DamageStrategy;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public class NormalDamageStrategy implements DamageStrategy {
    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        int raw = attacker.getAttack() * attack.getBasePower() / 100;
        return Math.max(1, raw - defender.getDefense());
    }
}