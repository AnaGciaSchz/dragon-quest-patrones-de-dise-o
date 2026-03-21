package com.taller.patrones.interfaces.rest.dto;

public record ExternalFighterDto(
        String fighter1Name,
        int fighter1Hp,
        int fighter1Atk,
        String fighter2Name,
        int fighter2Hp,
        int fighter2Atk
) {}