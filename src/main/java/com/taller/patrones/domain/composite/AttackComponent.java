package com.taller.patrones.domain.composite;

import com.taller.patrones.domain.Attack;

import java.util.List;

public interface AttackComponent {

    String getName();

    List<Attack> getAttacks();
}
