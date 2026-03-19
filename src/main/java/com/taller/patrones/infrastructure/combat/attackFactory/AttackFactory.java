package com.taller.patrones.infrastructure.combat.attackFactory;

import com.taller.patrones.domain.Attack;

public interface AttackFactory {

    Attack createAttack();
}
