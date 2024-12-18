package ru.practicum.mapper;

import ru.practicum.StatsDtoIn;
import ru.practicum.model.Stat;

public class StatMapper {

    public static Stat toStats(StatsDtoIn statsDtoIn) {

        final Stat stat = new Stat();

        stat.setIp(statsDtoIn.getIp());
        stat.setUri(statsDtoIn.getUri());
        stat.setTimestamp(statsDtoIn.getTimestamp());
        stat.setApp(statsDtoIn.getApp());

        return stat;
    }

    public static StatsDtoIn toStatsDto(Stat stat) {

        final StatsDtoIn statsDtoIn = new StatsDtoIn();

        statsDtoIn.setIp(stat.getIp());
        statsDtoIn.setUri(stat.getUri());
        statsDtoIn.setTimestamp(stat.getTimestamp());
        statsDtoIn.setApp(stat.getApp());

        return statsDtoIn;
    }
}
