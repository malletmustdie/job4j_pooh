package ru.elias.pooh;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import ru.elias.pooh.model.Req;

public class ReqTest {

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
        Req req = Req.of(content);
        Assert.assertThat(req.httpRequestType(), Matchers.is("POST"));
        Assert.assertThat(req.getPoohMode(), Matchers.is("queue"));
        Assert.assertThat(req.getSourceName(), Matchers.is("weather"));
        Assert.assertThat(req.getParam(), Matchers.is("temperature=18"));
    }

    @Test
    public void whenQueueModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /queue/weather HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        Req req = Req.of(content);
        Assert.assertThat(req.httpRequestType(), Matchers.is("GET"));
        Assert.assertThat(req.getPoohMode(), Matchers.is("queue"));
        Assert.assertThat(req.getSourceName(), Matchers.is("weather"));
        Assert.assertThat(req.getParam(), Matchers.is(""));
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
        Req req = Req.of(content);
        Assert.assertThat(req.httpRequestType(), Matchers.is("POST"));
        Assert.assertThat(req.getPoohMode(), Matchers.is("topic"));
        Assert.assertThat(req.getSourceName(), Matchers.is("weather"));
        Assert.assertThat(req.getParam(), Matchers.is("temperature=18"));
    }

    @Test
    public void whenTopicModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /topic/weather/client407 HTTP/1.1" + ls
                + "Host: localhost:9000" + ls
                + "User-Agent: curl/7.72.0" + ls
                + "Accept: */*" + ls + ls + ls;
        Req req = Req.of(content);
        Assert.assertThat(req.httpRequestType(), Matchers.is("GET"));
        Assert.assertThat(req.getPoohMode(), Matchers.is("topic"));
        Assert.assertThat(req.getSourceName(), Matchers.is("weather"));
        Assert.assertThat(req.getParam(), Matchers.is("client407"));
    }

}