package com.taller.patrones.domain.composite;

import com.taller.patrones.domain.Attack;

import java.util.List;

public class SingleAttack implements AttackComponent {

    private final Attack attack;

    public SingleAttack(Attack attack) {
        this.attack = attack;
    }

    @Override
    public String getName() {
        return attack.getName();
    }

    @Override
    public List<Attack> getAttacks() {
        return List.of(attack);
    }
}
