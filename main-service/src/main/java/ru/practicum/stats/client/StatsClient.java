package ru.practicum.stats.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;


import java.util.List;

@FeignClient(value = "stats-client", url = "http://stats-server:9090")
public interface StatsClient {

    @PostMapping("/hit")
    StatsDtoIn createStats(@RequestBody StatsDtoIn creationDto);

    @GetMapping("/stats")
    List<StatsDtoOut> getStats(@RequestParam String start,
                               @RequestParam String end,
                               @RequestParam(required = false) String[] uris,
                               @RequestParam(defaultValue = "false") boolean unique);
}