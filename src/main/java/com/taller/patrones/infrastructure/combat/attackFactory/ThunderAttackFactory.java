package com.taller.patrones.infrastructure.combat.attackFactory;

import com.taller.patrones.domain.Attack;

public class ThunderAttackFactory implements AttackFactory {

    @Override
    public Attack createAttack() {
        return new Attack("Thunder", 90, Attack.AttackType.SPECIAL);
    }
}
