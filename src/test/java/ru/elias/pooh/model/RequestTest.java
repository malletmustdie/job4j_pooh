package ru.elias.pooh.model;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class RequestTest {

    @Test
    public void whenQueueModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /queue/weather HTTP/1.1" + ls +
                "Host: localhost:9000" + ls +
                "User-Agent: curl/7.72.0" + ls +
                "Accept: */*" + ls +
                "Content-Length: 14" + ls +
                "Content-Type: application/x-www-form-urlencoded" + ls +
                "" + ls +
                "temperature=18" + ls;
        Request request = Request.of(content);
        Assert.assertThat(request.httpRequestType(), Matchers.is("POST"));
        Assert.assertThat(request.getPoohMode(), Matchers.is("queue"));
        Assert.assertThat(request.getSourceName(), Matchers.is("weather"));
        Assert.assertThat(request.getParam(), Matchers.is("temperature=18"));
    }

    @Test
    public void whenQueueModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /queue/weather HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        Request request = Request.of(content);
        Assert.assertThat(request.httpRequestType(), Matchers.is("GET"));
        Assert.assertThat(request.getPoohMode(), Matchers.is("queue"));
        Assert.assertThat(request.getSourceName(), Matchers.is("weather"));
        Assert.assertThat(request.getParam(), Matchers.is(""));
    }

    @Test
    public void whenTopicModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /topic/weather HTTP/1.1" + ls +
                "Host: localhost:9000" + ls +
                "User-Agent: curl/7.72.0" + ls +
                "Accept: */*" + ls +
                "Content-Length: 14" + ls +
                "Content-Type: application/x-www-form-urlencoded" + ls +
                "" + ls +
                "temperature=18" + ls;
        Request request = Request.of(content);
        Assert.assertThat(request.httpRequestType(), Matchers.is("POST"));
        Assert.assertThat(request.getPoohMode(), Matchers.is("topic"));
        Assert.assertThat(request.getSourceName(), Matchers.is("weather"));
        Assert.assertThat(request.getParam(), Matchers.is("temperature=18"));
    }

    @Test
    public void whenTopicModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /topic/weather/client407 HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        Request request = Request.of(content);
        Assert.assertThat(request.httpRequestType(), Matchers.is("GET"));
        Assert.assertThat(request.getPoohMode(), Matchers.is("topic"));
        Assert.assertThat(request.getSourceName(), Matchers.is("weather"));
        Assert.assertThat(request.getParam(), Matchers.is("client407"));
    }

}