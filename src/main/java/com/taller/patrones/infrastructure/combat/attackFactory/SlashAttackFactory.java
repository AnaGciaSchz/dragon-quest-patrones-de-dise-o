package com.taller.patrones.infrastructure.combat.attackFactory;

import com.taller.patrones.domain.Attack;

public class SlashAttackFactory implements AttackFactory {

    @Override
    public Attack createAttack() {
        return new Attack("Slash", 55, Attack.AttackType.NORMAL);
    }
}
