package com.taller.patrones.infrastructure.combat.attackFactory;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.composite.AttackComponent;
import com.taller.patrones.domain.composite.ComboAttack;
import com.taller.patrones.domain.composite.SingleAttack;

public class ComboTripleAttackFactory implements AttackFactory {

    @Override
    public Attack createAttack() {
        return new Attack("Combo Triple", 180, Attack.AttackType.SPECIAL);
    }

    public AttackComponent createCombo() {
        return new ComboAttack("Combo Triple")
                .add(new SingleAttack(new Attack("Tackle", 40, Attack.AttackType.NORMAL)))
                .add(new SingleAttack(new Attack("Slash", 60, Attack.AttackType.NORMAL)))
                .add(new SingleAttack(new Attack("Fireball", 80, Attack.AttackType.SPECIAL)));
    }
}
