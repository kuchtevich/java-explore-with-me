package ru.practicum.mapper;

import ru.practicum.StatsDtoIn;
import ru.practicum.model.Stat;

public class StatMapper {

    public static Stat toStats(StatsDtoIn statsDtoIn) {

        final Stat stats = new Stat();

        stats.setIp(statsDtoIn.getIp());
        stats.setUri(statsDtoIn.getUri());
        stats.setTimestamp(statsDtoIn.getTimestamp());
        stats.setApp(statsDtoIn.getApp());

        return stats;
    }

    public static StatsDtoIn toStatsDto(Stat stats) {

        final StatsDtoIn statsDtoIn = new StatsDtoIn();

        statsDtoIn.setIp(stats.getIp());
        statsDtoIn.setUri(stats.getUri());
        statsDtoIn.setTimestamp(stats.getTimestamp());
        statsDtoIn.setApp(stats.getApp());

        return statsDtoIn;
    }
}
