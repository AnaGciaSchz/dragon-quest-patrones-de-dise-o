package com.taller.patrones.infrastructure.combat.attackFactory;

import com.taller.patrones.domain.Attack;

public class GolpeAttackFactory implements AttackFactory {

    @Override
    public Attack createAttack() {
        return new Attack("Golpe", 30, Attack.AttackType.NORMAL);
    }
}
