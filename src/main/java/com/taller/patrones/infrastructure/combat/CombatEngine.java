package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;
import com.taller.patrones.infrastructure.combat.DamageStrategy.DamageStrategy;
import com.taller.patrones.infrastructure.combat.attackFactory.AttackFactory;

/**
 * Motor de combate. Calcula daño y crea ataques.
 * <p>
 * Nota: Esta clase crece cada vez que añadimos un ataque nuevo o un tipo de daño distinto.
 */
public class CombatEngine {
    private AttackFactory attackFactory;
    private DamageStrategy damageStrategy;

    /**
     * Crea un ataque a partir de su nombre.
     * Cada ataque nuevo requiere modificar este método.
     */
    public Attack createAttack() {
        return attackFactory.createAttack();
    }

    /**
     * Calcula el daño según el tipo de ataque.
     * Cada fórmula nueva (ej. crítico, veneno con tiempo) requiere modificar este switch.
     */
    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        return damageStrategy.calculateDamage(attacker, defender, attack);
    }

    public void setAttackFactory(AttackFactory attackFactory) {
        this.attackFactory = attackFactory;
    }

    public void setDamageStrategy(DamageStrategy damageStrategy) {
        this.damageStrategy = damageStrategy;
    }
}
