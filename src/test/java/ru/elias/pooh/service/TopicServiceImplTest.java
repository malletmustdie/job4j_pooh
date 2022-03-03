package ru.elias.pooh.service;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import ru.elias.pooh.model.Request;
import ru.elias.pooh.model.Response;
import ru.elias.pooh.service.impl.TopicServiceImpl;
import ru.elias.pooh.util.ApiConstants;

public class TopicServiceImplTest {

    @Test
    public void whenCreateTopicThenGetParameter() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        topicServiceImpl.process(
                new Request("POST", "topic", "weather", "value", "1")
        );
        Response response = topicServiceImpl.process(
                new Request("GET", "topic", "weather", "1", "1")
        );
        Assert.assertThat(response.text(), Matchers.is("value"));
    }

    @Test
    public void whenCreateTopicWithAndGetWithIncorrectQueueNameThenGet404() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        topicServiceImpl.process(
                new Request("POST", "topic", "weather", "value", "1")
        );
        topicServiceImpl.process(
                new Request("GET", "topic", "weather", "1", "1")
        );
        Response response = topicServiceImpl.process(
                new Request("GET", "topic", "weather", "1", "1")
        );

        Assert.assertThat(response.text(), Matchers.is(ApiConstants.RESPONSE_MSG_REQUEST_PARAM_NOT_FOUND));
    }

    @Test
    public void whenSendIncorrectMsgThenGet500() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        Response result = topicServiceImpl.process(
                new Request("DELETE", "topic", "weather", "value", "1")
        );
        Assert.assertThat(result.text(), Matchers.is("Internal server error"));
    }

}