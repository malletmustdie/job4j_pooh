package ru.elias.pooh.service.impl;

import ru.elias.pooh.model.Request;
import ru.elias.pooh.model.Response;
import ru.elias.pooh.service.Service;
import ru.elias.pooh.util.ApiConstants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicServiceImpl implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics
            = new ConcurrentHashMap<>();

    @Override
    public Response process(Request request) {
        Response response;
        switch (request.httpRequestType()) {
            case ApiConstants.GET_METHOD:
                response = requestMappingGet(request);
                break;
            case ApiConstants.POST_METHOD:
                response = requestMappingPost(request);
                break;
            default:
                response = new Response(
                        ApiConstants.RESPONSE_MSG_REQUEST_INTERNAL_SERVER_ERROR,
                        ApiConstants.RESPONSE_STATUS_500
                );
                break;
        }
        return response;
    }

    private Response requestMappingPost(Request request) {
        Response response;
        if (topics.get(request.getSourceName()) == null) {
            response = new Response(
                    ApiConstants.RESPONSE_MSG_REQUEST_INTERNAL_SERVER_ERROR,
                    ApiConstants.RESPONSE_STATUS_500
            );
        } else {
            topics.get(request.getSourceName()).values().forEach(q -> q.add(request.getParam()));
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
        topic.putIfAbsent(request.getParam(), new ConcurrentLinkedQueue<>());
        var text = topic.get(request.getParam()).poll();
        return text == null ?
                new Response(ApiConstants.RESPONSE_MSG_REQUEST_NOT_FOUND, ApiConstants.RESPONSE_STATUS_404) :
                new Response(text, ApiConstants.RESPONSE_STATUS_200);
    }

}
