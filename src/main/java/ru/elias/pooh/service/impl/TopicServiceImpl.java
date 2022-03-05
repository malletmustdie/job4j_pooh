package ru.elias.pooh.service.impl;

import ru.elias.pooh.model.Request;
import ru.elias.pooh.model.Response;
import ru.elias.pooh.service.Service;
import ru.elias.pooh.util.ApiConstants;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicServiceImpl implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> tempQueue = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topic
            = new ConcurrentHashMap<>();

    /**
     * Метод для обработки входящих сообщений.
     * <p>
     * POST - создание консьюмера на топике (подписчика) и публикация сообщения в очереди топика.
     * GET - получение сообщения из очереди.
     *
     * @param request
     *         Сообщение от клиента.
     *
     * @return Ответ сервера.
     */
    @Override
    public Response process(Request request) {
        Response response = new Response(ApiConstants.ERROR, ApiConstants.RESPONSE_STATUS_500);
        tempQueue.putIfAbsent(request.getSourceName(), new ConcurrentLinkedQueue<>());
        topic.putIfAbsent(request.getSourceName(), new ConcurrentHashMap<>());
        switch (request.httpRequestType()) {
            case ApiConstants.POST_METHOD -> response = requestMappingPost(request);
            case ApiConstants.GET_METHOD -> response = requestMappingGet(request, response);
        }
        return response;
    }

    private Response requestMappingPost(Request request) {
        tempQueue.get(request.getSourceName()).add(request.getParam());
        topic.get(request.getSourceName())
             .putIfAbsent(
                     request.getParam(),
                     new ConcurrentLinkedQueue<>(tempQueue.get(request.getSourceName()))
             );
        return new Response(request.getParam(), ApiConstants.RESPONSE_STATUS_200);
    }

    private Response requestMappingGet(Request request, Response response) {
        topic.get(request.getSourceName())
             .putIfAbsent(
                     request.getParam(),
                     new ConcurrentLinkedQueue<>(tempQueue.get(request.getSourceName()))
             );
        String text =
                Optional.ofNullable(
                        topic.get(request.getSourceName()).get(request.getParam()).poll()
                ).orElse("");
        return text.isEmpty() ? response
                              : new Response(text, ApiConstants.RESPONSE_STATUS_200);

    }

}
