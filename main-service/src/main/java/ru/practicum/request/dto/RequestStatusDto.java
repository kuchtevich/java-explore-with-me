package ru.practicum.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.request.model.RequestStatus;

import java.util.List;

@Data
public class RequestStatusDto {
    @NotNull
    private List<Long> requestIds;

    @NotNull
    private RequestStatus status;
}
