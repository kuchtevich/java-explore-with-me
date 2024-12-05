package ru.practicum.mapper;

import ru.practicum.StatsDtoIn;
import ru.practicum.model.Stats;

public class StatsMapper {

    public static Stats toStats(StatsDtoIn statsDtoIn) {

        final Stats stats = new Stats();

        stats.setIp(statsDtoIn.getIp());
        stats.setUri(statsDtoIn.getUri());
        stats.setTimestamp(statsDtoIn.getTimestamp());
        stats.setApp(statsDtoIn.getApp());

        return stats;
    }

    public static StatsDtoIn toStatsDto(Stats stats) {

        final StatsDtoIn statsDtoIn = new StatsDtoIn();

        statsDtoIn.setIp(stats.getIp());
        statsDtoIn.setUri(stats.getUri());
        statsDtoIn.setTimestamp(stats.getTimestamp());
        statsDtoIn.setApp(stats.getApp());

        return statsDtoIn;
    }
}
