package com.taller.patrones.application.command;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;

public class ApplyDamageCommand implements AttackCommand {

    private final Battle battle;
    private final Character attacker;
    private final Character defender;
    private final int damage;
    private final Attack attack;

    private int defenderHpBeforeAttack;
    private String logEntryBeforeAttack;
    private int lastDamageBeforeAttack;
    private String lastDamageTargetBeforeAttack;

    public ApplyDamageCommand(Attack attack, int damage, Character defender, Character attacker, Battle battle) {
        this.attack = attack;
        this.damage = damage;
        this.defender = defender;
        this.attacker = attacker;
        this.battle = battle;
    }

    @Override
    public void execute() {
        defenderHpBeforeAttack = defender.getCurrentHp();
        logEntryBeforeAttack = battle.getBattleLog().isEmpty() ? null : battle.getBattleLog().get(battle.getBattleLog().size() - 1);
        lastDamageBeforeAttack = battle.getLastDamage();
        lastDamageTargetBeforeAttack = battle.getLastDamageTarget();

        defender.takeDamage(damage);
        String target = defender == battle.getPlayer() ? "player" : "enemy";
        battle.setLastDamage(damage, target);
        battle.switchTurn();
        if (!defender.isAlive()) {
            battle.finish(attacker.getName());
        }
    }

    @Override
    public void undo() {
        defender.restoreHp(defenderHpBeforeAttack);

        if (!battle.getBattleLog().isEmpty())
            battle.getBattleLog().remove(battle.getBattleLog().size() - 1);

        battle.setLastDamage(lastDamageBeforeAttack, lastDamageTargetBeforeAttack);

        battle.switchTurn();
    }
}
