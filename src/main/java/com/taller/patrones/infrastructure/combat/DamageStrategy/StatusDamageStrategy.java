package com.taller.patrones.infrastructure.combat.DamageStrategy;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public class StatusDamageStrategy implements DamageStrategy {
    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        return 0;
    }
}
