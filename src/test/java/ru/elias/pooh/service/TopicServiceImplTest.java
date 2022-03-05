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
        String param = "param=value";
        topicServiceImpl.process(
                new Request("POST", "topic", "weather", param)
        );
        Response response = topicServiceImpl.process(
                new Request("GET", "topic", "weather", "1")
        );
        Assert.assertThat(response.text(), Matchers.is("param=value"));
    }

    @Test
    public void whenCreateTopicWithAndGetMultipleTimesThenGetError() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        String param = "param=value";
        topicServiceImpl.process(
                new Request("POST", "topic", "weather", param)
        );
        topicServiceImpl.process(
                new Request("GET", "topic", "weather", "1")
        );
        Response response = topicServiceImpl.process(
                new Request("GET", "topic", "weather", "1")
        );

        Assert.assertThat(response.text(), Matchers.is(ApiConstants.ERROR));
    }

    @Test
    public void whenCreateTopicWithAndGetFromIncorrectTopicNameThenGetError() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        String param = "param=value";
        topicServiceImpl.process(
                new Request("POST", "topic", "weather", param)
        );
        Response response = topicServiceImpl.process(
                new Request("GET", "topic", "weatherrr", "1")
        );

        Assert.assertThat(response.text(), Matchers.is(ApiConstants.ERROR));
    }


    @Test
    public void whenSendIncorrectMsgThenGet500() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        String param = "param=value";
        Response result = topicServiceImpl.process(
                new Request("DELETE", "topic", "weather", param)
        );
        Assert.assertThat(result.text(), Matchers.is(ApiConstants.ERROR));
    }

}