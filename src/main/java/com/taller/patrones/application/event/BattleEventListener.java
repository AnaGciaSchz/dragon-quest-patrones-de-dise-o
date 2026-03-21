package com.taller.patrones.application.event;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;

public interface BattleEventListener {
    void update(Battle battle, Character attacker, Character defender, int damage, Attack attack);
}
