package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithAnswers;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(RequestDto requestDto, Long userId);

    List<RequestDtoWithAnswers> getUserRequests(Long userId);

    List<RequestDto> getAllRequests();

    RequestDtoWithAnswers getRequestsById(Long requestId);
}
