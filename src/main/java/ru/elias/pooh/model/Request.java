package ru.elias.pooh.model;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Request {

    private static final AtomicInteger count = new AtomicInteger(0);

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;
    private final String id;

    public Request(String httpRequestType, String poohMode, String sourceName, String param, String id) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
        this.id = id;
    }

    public static Request of(String content) {
        System.out.println(content);
        var request = content.split(System.lineSeparator());
        var requestFirstLine = request[0].split("/");
        System.out.println(Arrays.toString(requestFirstLine));
        var requestType = requestFirstLine[0].trim();
        var poohMode = requestFirstLine[1].trim();
        var sourceName = requestFirstLine[2].trim().split(" ")[0];
        var param = "POST".equals(requestType) ? request[request.length - 1]
                : requestFirstLine.length > 4 ? requestFirstLine[3].trim().split(" ")[0] : "";

        String id = "POST".equals(requestType) && "topic".equals(poohMode) ? String.valueOf(count.incrementAndGet())
                                                                           : param;
        return new Request(requestType, poohMode, sourceName, param, id);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }

    public String getId() {
        return id;
    }

}
