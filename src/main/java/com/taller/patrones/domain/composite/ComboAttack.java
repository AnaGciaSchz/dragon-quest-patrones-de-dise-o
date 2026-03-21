package com.taller.patrones.domain.composite;

import com.taller.patrones.domain.Attack;

import java.util.ArrayList;
import java.util.List;

public class ComboAttack implements AttackComponent {

    private final String name;
    private final List<AttackComponent> components = new ArrayList<>();

    public ComboAttack(String name) {
        this.name = name;
    }

    public ComboAttack add(AttackComponent component) {
        components.add(component);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Attack> getAttacks() {
        List<Attack> all = new ArrayList<>();

        for (AttackComponent component : components) {
            all.addAll(component.getAttacks());
        }

        return all;
    }
}
