package com.taller.patrones.application;

import com.taller.patrones.application.command.ApplyDamageCommand;
import com.taller.patrones.application.command.AttackCommand;
import com.taller.patrones.application.event.AnalyticsListener;
import com.taller.patrones.application.event.AnalyticsLogListener;
import com.taller.patrones.application.event.BattleEventListener;
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

import java.util.Deque;
import java.util.HashMap;
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
    private final BattleRepository battleRepository = BattleRepository.getInstance();
    private final Map<String, AttackFactory> attackFactoryMap = Map.of(
            "TACKLE", new TackleAttackFactory(),
            "SLASH", new SlashAttackFactory(),
            "FIREBALL", new FireballAttackFactory(),
            "ICE_BEAM", new IceBeamAttackFactory(),
            "POISON_STING", new PoisonStingAttackFactory(),
            "THUNDER", new ThunderAttackFactory(),
            "METEOR", new MeteorAttackFactory(),
            "GOLPE", new GolpeAttackFactory());
    private final Map<Attack.AttackType, DamageStrategy> damageStrategyMap = Map.of(
            Attack.AttackType.NORMAL, new NormalDamageStrategy(),
            Attack.AttackType.SPECIAL, new SpecialDamageStrategy(),
            Attack.AttackType.STATUS, new StatusDamageStrategy(),
            Attack.AttackType.CRITICAL, new CriticalDamageStrategy());
    private final List<BattleEventListener> listeners = List.of(
            new AnalyticsListener(),
            new AnalyticsLogListener());
    private final Map<Battle, Deque<AttackCommand>> commandhistory = new HashMap<>();

    public static final List<String> PLAYER_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL", "ICE_BEAM", "POISON_STING",
            "THUNDER");
    public static final List<String> ENEMY_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL");

    public BattleStartResult startBattle(String playerName, String enemyName) {
        Character player = Character.builder()
                .name(playerName != null ? playerName : "Héroe")
                .maxHp(150)
                .attack(25)
                .defense(10)
                .speed(20)
                .build();

        Character enemy = Character.builder()
                .name(enemyName != null ? playerName : "Dragón")
                .maxHp(120)
                .attack(30)
                .defense(10)
                .speed(15)
                .build();

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
        if (battle == null || battle.isFinished() || !battle.isPlayerTurn())
            return;

        combatEngine.setAttackFactory(attackFactoryMap.get(attackName));
        Attack attack = combatEngine.createAttack();
        combatEngine.setDamageStrategy(damageStrategyMap.get(attack.getType()));
        int damage = combatEngine.calculateDamage(battle.getPlayer(), battle.getEnemy(), attack);
        applyDamage(battle, battle.getPlayer(), battle.getEnemy(), damage, attack);
    }

    public void executeEnemyAttack(String battleId, String attackName) {
        Battle battle = getBattle(battleId);
        if (battle == null || battle.isFinished() || battle.isPlayerTurn())
            return;

        combatEngine.setAttackFactory(attackFactoryMap.getOrDefault(attackName, new TackleAttackFactory()));
        Attack attack = combatEngine.createAttack();
        combatEngine.setDamageStrategy(damageStrategyMap.get(attack.getType()));
        int damage = combatEngine.calculateDamage(battle.getEnemy(), battle.getPlayer(), attack);
        applyDamage(battle, battle.getEnemy(), battle.getPlayer(), damage, attack);
    }

    private void applyDamage(Battle battle, Character attacker, Character defender, int damage, Attack attack) {
        AttackCommand attackCommand = new ApplyDamageCommand(attack, damage, defender, attacker, battle);
        attackCommand.execute();

        commandhistory
                .computeIfAbsent(battle, k -> new java.util.ArrayDeque<>())
                .push(attackCommand);

        for (BattleEventListener listener : listeners) {
            listener.update(battle, attacker, defender, damage, attack);
        }
    }

    public void undoLastDamage(Battle battle) {
        if (battle == null)
            return;

        Deque<AttackCommand> history = commandhistory.get(battle);
        if (history != null && !history.isEmpty())
            history.pop().undo();
    }

    public BattleStartResult startBattleFromExternal(Character player, Character enemy) {
        Battle battle = new Battle(player, enemy);
        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);
        return new BattleStartResult(battleId, battle);
    }

    public record BattleStartResult(String battleId, Battle battle) {
    }
}
