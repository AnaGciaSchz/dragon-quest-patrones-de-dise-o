package com.taller.patrones.infrastructure;

import com.taller.patrones.application.BattleService;
import com.taller.patrones.application.CombatFacade;
import com.taller.patrones.domain.Character;
import com.taller.patrones.interfaces.rest.dto.ExternalFighterDto;

public class ExternalBattleAdapter {

    public BattleService.BattleStartResult adapt(ExternalFighterDto dto, CombatFacade combatFacade) {
        Character player = Character.builder()
                .name(dto.fighter1Name() != null ? dto.fighter1Name() : "Héroe")
                .maxHp(dto.fighter1Hp() != 0 ? dto.fighter1Hp() : 150)
                .attack(dto.fighter1Atk() != 0 ? dto.fighter1Atk() : 25)
                .defense(10)
                .speed(10)
                .build();

        Character enemy = Character.builder()
                .name(dto.fighter2Name() != null ? dto.fighter2Name() : "Dragón")
                .maxHp(dto.fighter2Hp() != 0 ? dto.fighter2Hp() : 120)
                .attack(dto.fighter2Atk() != 0 ? dto.fighter2Atk() : 30)
                .defense(10)
                .speed(10)
                .build();

        return combatFacade.startBattleFromExternal(player, enemy);
    }
}
