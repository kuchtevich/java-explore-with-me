package ru.practicum.mapper;

import ru.practicum.StatsDtoIn;
import ru.practicum.model.Stats;

public class StatsMapper {

    public static Stats toStats(StatsDtoIn statsDtoIn) {

        final Stats stats = new Stats();

        stats.setIP(statsDtoIn.getIP());
        stats.setURI(statsDtoIn.getURI());
        stats.setTimestamp(statsDtoIn.getTimestamp());
        stats.setApp(statsDtoIn.getApp());

        return stats;
    }

    public static StatsDtoIn toStatsDto(Stats stats) {

        final StatsDtoIn statsDtoIn = new StatsDtoIn();

        statsDtoIn.setIP(stats.getIP());
        statsDtoIn.setURI(stats.getURI());
        statsDtoIn.setTimestamp(stats.getTimestamp());
        statsDtoIn.setApp(stats.getApp());

        return statsDtoIn;
    }
}
