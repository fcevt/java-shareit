package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.Answer;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithAnswers;

import java.util.List;

@UtilityClass
public class RequestMapper {
    public static RequestDto toItemRequestDto(Request itemRequest) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(itemRequest.getRequestId());
        requestDto.setDescription(itemRequest.getDescription());
        requestDto.setCreated(itemRequest.getCreatedDate());
        return requestDto;
    }

    public static RequestDtoWithAnswers toItemRequestDtoWithAnswers(Request request, List<Answer> answers) {
        RequestDtoWithAnswers requestDtoWithAnswers = new RequestDtoWithAnswers();
        requestDtoWithAnswers.setId(request.getRequestId());
        requestDtoWithAnswers.setDescription(request.getDescription());
        requestDtoWithAnswers.setCreated(request.getCreatedDate());
        requestDtoWithAnswers.setItems(answers);
        return requestDtoWithAnswers;
    }
}
