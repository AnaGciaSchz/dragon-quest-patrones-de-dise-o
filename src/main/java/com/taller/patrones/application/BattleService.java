package com.taller.patrones.application;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;
import com.taller.patrones.infrastructure.combat.CombatEngine;
import com.taller.patrones.infrastructure.combat.DamageStrategy.CriticalDamageStrategy;
import com.taller.patrones.infrastructure.combat.DamageStrategy.DamageStrategy;
import com.taller.patrones.infrastructure.combat.DamageStrategy.NormalDamageStrategy;
import com.taller.patrones.infrastructure.combat.DamageStrategy.SpecialDamageStrategy;
import com.taller.patrones.infrastructure.combat.DamageStrategy.StatusDamageStrategy;
import com.taller.patrones.infrastructure.combat.attackFactory.AttackFactory;
import com.taller.patrones.infrastructure.combat.attackFactory.FireballAttackFactory;
import com.taller.patrones.infrastructure.combat.attackFactory.GolpeAttackFactory;
import com.taller.patrones.infrastructure.combat.attackFactory.IceBeamAttackFactory;
import com.taller.patrones.infrastructure.combat.attackFactory.MeteorAttackFactory;
import com.taller.patrones.infrastructure.combat.attackFactory.PoisonStingAttackFactory;
import com.taller.patrones.infrastructure.combat.attackFactory.SlashAttackFactory;
import com.taller.patrones.infrastructure.combat.attackFactory.TackleAttackFactory;
import com.taller.patrones.infrastructure.combat.attackFactory.ThunderAttackFactory;
import com.taller.patrones.infrastructure.persistence.BattleRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Caso de uso: gestionar batallas.
 * <p>
 * Nota: Crea sus propias dependencias con new. Cada vez que necesitamos
 * un CombatEngine o BattleRepository, hacemos new aquí.
 */
public class BattleService {

    private final CombatEngine combatEngine = new CombatEngine();
    private final BattleRepository battleRepository = new BattleRepository();
    private final Map<String, AttackFactory> attackFactoryMap = Map.of(
            "TACKLE", new TackleAttackFactory(),
            "SLASH", new SlashAttackFactory(),
            "FIREBALL", new FireballAttackFactory(),
            "ICE_BEAM", new IceBeamAttackFactory(),
            "POISON_STING", new PoisonStingAttackFactory(),
            "THUNDER", new ThunderAttackFactory(),
            "METEOR", new MeteorAttackFactory(),
            "GOLPE", new GolpeAttackFactory()
    );
    private final Map<Attack.AttackType, DamageStrategy> damageStrategyMap = Map.of(
            Attack.AttackType.NORMAL, new NormalDamageStrategy(),
            Attack.AttackType.SPECIAL, new SpecialDamageStrategy(),
            Attack.AttackType.STATUS, new StatusDamageStrategy(),
            Attack.AttackType.CRITICAL, new CriticalDamageStrategy()
    );

    public static final List<String> PLAYER_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL", "ICE_BEAM", "POISON_STING", "THUNDER");
    public static final List<String> ENEMY_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL");

    public BattleStartResult startBattle(String playerName, String enemyName) {
        Character player = new Character(
                playerName != null ? playerName : "Héroe",
                150, 25, 15, 20
        );

        Character enemy = new Character(
                enemyName != null ? enemyName : "Dragón",
                120, 30, 10, 15
        );

        Battle battle = new Battle(player, enemy);
        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);

        return new BattleStartResult(battleId, battle);
    }

    public Battle getBattle(String battleId) {
        return battleRepository.findById(battleId);
    }

    public void executePlayerAttack(String battleId, String attackName) {
        Battle battle = getBattle(battleId);
        if (battle == null || battle.isFinished() || !battle.isPlayerTurn()) return;

        combatEngine.setAttackFactory(attackFactoryMap.get(attackName));
        Attack attack = combatEngine.createAttack();
        combatEngine.setDamageStrategy(damageStrategyMap.get(attack.getType()));
        int damage = combatEngine.calculateDamage(battle.getPlayer(), battle.getEnemy(), attack);
        applyDamage(battle, battle.getPlayer(), battle.getEnemy(), damage, attack);
    }

    public void executeEnemyAttack(String battleId, String attackName) {
        Battle battle = getBattle(battleId);
        if (battle == null || battle.isFinished() || battle.isPlayerTurn()) return;

        combatEngine.setAttackFactory(attackFactoryMap.getOrDefault(attackName, new TackleAttackFactory()));
        Attack attack = combatEngine.createAttack();
        combatEngine.setDamageStrategy(damageStrategyMap.get(attack.getType()));
        int damage = combatEngine.calculateDamage(battle.getEnemy(), battle.getPlayer(), attack);
        applyDamage(battle, battle.getEnemy(), battle.getPlayer(), damage, attack);
    }

    private void applyDamage(Battle battle, Character attacker, Character defender, int damage, Attack attack) {
        defender.takeDamage(damage);
        String target = defender == battle.getPlayer() ? "player" : "enemy";
        battle.setLastDamage(damage, target);
        battle.log(attacker.getName() + " usa " + attack.getName() + " y hace " + damage + " de daño a " + defender.getName());
        battle.switchTurn();
        if (!defender.isAlive()) {
            battle.finish(attacker.getName());
        }
    }

    public BattleStartResult startBattleFromExternal(String fighter1Name, int fighter1Hp, int fighter1Atk,
                                                     String fighter2Name, int fighter2Hp, int fighter2Atk) {
        Character player = new Character(fighter1Name, fighter1Hp, fighter1Atk, 10, 10);
        Character enemy = new Character(fighter2Name, fighter2Hp, fighter2Atk, 10, 10);
        Battle battle = new Battle(player, enemy);
        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);
        return new BattleStartResult(battleId, battle);
    }

    public record BattleStartResult(String battleId, Battle battle) {
    }
}
