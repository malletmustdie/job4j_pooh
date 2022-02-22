package ru.elias.pooh.model;

public class Request {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Request(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Request of(String content) {
        var request = content.split(System.lineSeparator());
        var requestFirstLine = request[0].split("/");
        var requestType = requestFirstLine[0].trim();
        var poohMode = requestFirstLine[1].trim();
        var sourceName = requestFirstLine[2].trim().split(" ")[0];
        var param = "POST".equals(requestType) ? request[request.length - 1]
                : requestFirstLine.length > 3 ? requestFirstLine[3].trim().split(" ")[0]
                : " ";
        return new Request(requestType, poohMode, sourceName, param);
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

}
