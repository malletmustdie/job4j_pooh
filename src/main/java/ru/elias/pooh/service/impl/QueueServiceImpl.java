package ru.elias.pooh.service.impl;

import ru.elias.pooh.model.Req;
import ru.elias.pooh.model.Resp;
import ru.elias.pooh.service.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueServiceImpl implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp response = null;
        if (req == null) {
            response = new Resp("Error, request is null", "500");
        } else if ("GET".equals(req.httpRequestType())) {
            var text = queue.get(req.getSourceName()).poll();
            response = new Resp(text, "200");
        } else if ("POST".equals(req.httpRequestType())) {
            var q = new ConcurrentLinkedQueue<String>();
            q.add(req.getParam());
            queue.putIfAbsent(req.getSourceName(), q);
            response = new Resp("Success added to queue", "200");
        }
        return response;
    }

}
