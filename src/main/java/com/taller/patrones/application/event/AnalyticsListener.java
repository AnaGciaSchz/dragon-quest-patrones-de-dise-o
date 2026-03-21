package com.taller.patrones.application.event;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;

public class AnalyticsListener implements BattleEventListener {

    @Override
    public void update(Battle battle, Character attacker, Character defender, int damage, Attack attack) {
        System.out.println("Attack = " + attack.getName()
                + " type = " + attack.getType()
                + " damage = " + damage
                + " attacker_hp = " + attacker.getCurrentHp()
                + " defender_hp = " + defender.getCurrentHp());
    }
}
