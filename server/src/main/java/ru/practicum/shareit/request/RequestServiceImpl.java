package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithAnswers;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public RequestDto createRequest(RequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Request newRequest = new Request();
        newRequest.setDescription(requestDto.getDescription());
        newRequest.setRequester(user);
        newRequest.setCreatedDate(LocalDateTime.now());
        return RequestMapper.toItemRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public List<RequestDto> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(RequestMapper::toItemRequestDto)
                .sorted((r1, r2) -> r2.getCreated().compareTo(r1.getCreated()))
                .collect(Collectors.toList());
    }

    @Override
    public RequestDtoWithAnswers getRequestsById(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
        List<Answer> answers = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::itemToAnswer)
                .collect(Collectors.toList());
        return RequestMapper.toItemRequestDtoWithAnswers(request, answers);
    }

    @Override
    public List<RequestDtoWithAnswers> getUserRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<RequestDtoWithAnswers> finalList = new ArrayList<>();
        List<Request> requests = requestRepository.findAllByRequester_Id(userId); //список запросов пользователя
        List<Item> items = itemRepository.findAllByRequestIdIn(requests.stream() //список всех вещей по всем запросам пользователя
                .map(request -> request.getRequestId()).collect(Collectors.toList()));
        for (Request request : requests) { //раскладывание всех вещей по запросам
            List<Answer> answers = items.stream()
                    .filter(item -> item.getRequestId().equals(request.getRequestId()))
                    .map(ItemMapper::itemToAnswer)
                    .collect(Collectors.toList());
            finalList.add(RequestMapper.toItemRequestDtoWithAnswers(request, answers));
        }
        return finalList.stream()
                .sorted((r1, r2) -> r2.getCreated().compareTo(r1.getCreated()))
                .collect(Collectors.toList());
    }
}
