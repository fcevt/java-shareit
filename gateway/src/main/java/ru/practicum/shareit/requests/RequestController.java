package ru.practicum.shareit.requests;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient requestClient;

    @Autowired
    public RequestController(RequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
            @RequestBody @Valid ItemRequestDto requestDto) {
        log.info("создание запроса, requesrDto = {}, ownerId={}", requestDto.toString(), ownerId);
        return requestClient.create(requestDto, ownerId);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        return requestClient.getAllByOwner(ownerId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllByAnotherUsers(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        return requestClient.getAllByAnotherUsers(ownerId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                           @PathVariable Long requestId) {
        return requestClient.findById(requestId);
    }
}
