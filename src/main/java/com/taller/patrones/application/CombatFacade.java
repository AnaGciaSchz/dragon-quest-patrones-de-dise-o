package com.taller.patrones.application;

import com.taller.patrones.application.BattleService.BattleStartResult;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;

import java.util.List;

public class CombatFacade {

    private final BattleService battleService;

    public CombatFacade() {
        this.battleService = new BattleService();
    }

    public static List<String> getPlayerAttacks() {
        return BattleService.PLAYER_ATTACKS;
    }

    public static List<String> getEnemyAttacks() {
        return BattleService.ENEMY_ATTACKS;
    }

    public BattleStartResult startBattle(String playerName, String enemyName) {
        return battleService.startBattle(playerName, enemyName);
    }

    public BattleStartResult startBattleFromExternal(Character player, Character enemy) {
        return battleService.startBattleFromExternal(player, enemy);
    }

    public Battle getState(String battleId) {
        return battleService.getBattle(battleId);
    }

    public Battle executeAttack(String battleId, String attackName) {
        Battle battle = battleService.getBattle(battleId);
        if (battle == null || battle.isFinished())
            return battle;

        if (battle.isPlayerTurn()) {
            battleService.executePlayerAttack(battleId, attackName);
        } else {
            battleService.executeEnemyAttack(battleId, attackName);
        }

        return battleService.getBattle(battleId);
    }

    public Battle executeEnemyTurn(String battleId) {
        Battle battle = battleService.getBattle(battleId);
        if (battle == null)
            return null;
        if (battle.isPlayerTurn() || battle.isFinished())
            return battle;

        String attack = BattleService.ENEMY_ATTACKS.get(
                (int) (Math.random() * BattleService.ENEMY_ATTACKS.size()));
        battleService.executeEnemyAttack(battleId, attack);
        return battleService.getBattle(battleId);
    }

    public Battle undoLastAttack(String battleId) {
        Battle battle = battleService.getBattle(battleId);
        battleService.undoLastDamage(battle);
        return battle;
    }
}
