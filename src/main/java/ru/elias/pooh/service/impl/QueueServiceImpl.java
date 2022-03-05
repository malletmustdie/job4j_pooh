package ru.elias.pooh.service.impl;

import ru.elias.pooh.model.Request;
import ru.elias.pooh.model.Response;
import ru.elias.pooh.service.Service;
import ru.elias.pooh.util.ApiConstants;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueServiceImpl implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Response process(Request request) {
        Response response = new Response(ApiConstants.ERROR, ApiConstants.RESPONSE_STATUS_500);
        switch (request.httpRequestType()) {
            case ApiConstants.POST_METHOD -> response = requestMappingPost(request);
            case ApiConstants.GET_METHOD -> response = requestMappingGet(request, response);
        }
        return response;
    }

    private Response requestMappingPost(Request request) {
        queue.putIfAbsent(request.getSourceName(), new ConcurrentLinkedQueue<>());
        var text = request.getParam();
        queue.get(request.getSourceName()).add(text);
        return new Response(text, ApiConstants.RESPONSE_STATUS_200);
    }

    private Response requestMappingGet(Request request, Response response) {
        if (queue.get(request.getSourceName()) == null) {
            return response;
        }
        var text = Optional.ofNullable(queue.get(request.getSourceName()).poll()).orElse("");
        return text.isEmpty() ? response
                              : new Response(text, ApiConstants.RESPONSE_STATUS_200);
    }

}
