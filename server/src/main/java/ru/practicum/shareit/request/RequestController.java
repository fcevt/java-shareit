package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithAnswers;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto createItemRequest(@RequestBody RequestDto requestDto,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("Create item request - user id: {}, request: {}", userId, requestDto);
        return requestService.createRequest(requestDto, userId);
    }

    @GetMapping
    public List<RequestDtoWithAnswers> getUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public RequestDtoWithAnswers getRequestById(@PathVariable Long requestId) {
        return requestService.getRequestsById(requestId);
    }
}
