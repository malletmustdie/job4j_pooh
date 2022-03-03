package ru.elias.pooh.service.impl;

import ru.elias.pooh.model.Request;
import ru.elias.pooh.model.Response;
import ru.elias.pooh.service.Service;
import ru.elias.pooh.util.ApiConstants;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TopicServiceImpl implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics
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
        return switch (request.httpRequestType()) {
            case ApiConstants.GET_METHOD -> requestMappingGet(request);
            case ApiConstants.POST_METHOD -> requestMappingPost(request);
            default -> new Response(
                    ApiConstants.RESPONSE_MSG_REQUEST_INTERNAL_SERVER_ERROR,
                    ApiConstants.RESPONSE_STATUS_500
            );
        };
    }

    private Response requestMappingPost(Request request) {
        Response response;
        topics.putIfAbsent(request.getSourceName(), new ConcurrentHashMap<>());
        topics.get(request.getSourceName())
              .putIfAbsent(String.valueOf(request.getId()), new ConcurrentLinkedQueue<>());
        var topic = topics.get(request.getSourceName());
        if (topic == null) {
            response = new Response(
                    ApiConstants.RESPONSE_MSG_REQUEST_TOPIC_NOT_FOUND,
                    ApiConstants.RESPONSE_STATUS_404
            );
        } else {
            topic.values().forEach(queue -> queue.offer(request.getParam()));
            response = new Response(
                    ApiConstants.RESPONSE_MSG_REQUEST_SUCCESS,
                    ApiConstants.RESPONSE_STATUS_200
            );
        }
        return response;
    }

    private Response requestMappingGet(Request request) {
        topics.putIfAbsent(request.getSourceName(), new ConcurrentHashMap<>());
        var topic = topics.get(request.getSourceName());
        if (topic.get(request.getParam()) == null) {
            return new Response(ApiConstants.RESPONSE_MSG_BAD_REQUEST, ApiConstants.RESPONSE_STATUS_400);
        }
        var text = Optional.ofNullable(topic.get(request.getParam()).poll()).orElse("");
        return text.isEmpty()
               ?
               new Response(ApiConstants.RESPONSE_MSG_REQUEST_PARAM_NOT_FOUND, ApiConstants.RESPONSE_STATUS_404)
               : new Response(text, ApiConstants.RESPONSE_STATUS_200);
    }

}
