package com.taller.patrones.infrastructure.combat.attackFactory;

import com.taller.patrones.domain.Attack;

public class MeteorAttackFactory implements AttackFactory {

    @Override
    public Attack createAttack() {
        return new Attack("Meteoro", 120, Attack.AttackType.SPECIAL);
    }
}
