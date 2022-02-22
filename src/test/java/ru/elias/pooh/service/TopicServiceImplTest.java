package ru.elias.pooh.service;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import ru.elias.pooh.model.Request;
import ru.elias.pooh.model.Response;
import ru.elias.pooh.service.impl.QueueServiceImpl;
import ru.elias.pooh.service.impl.TopicServiceImpl;

public class TopicServiceImplTest {

    @Test
    public void whenCreateTwoTopicThenGetTwoParameters() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();

        String queue1 = "client1";
        String queue2 = "client2";

        String paramForQueue1 = "temperature=18";
        String paramForQueue2 = "temperature=1488";

        topicServiceImpl.process(
                new Request("GET", "topic", "weather", queue1)
        );
        topicServiceImpl.process(
                new Request("POST", "topic", "weather", paramForQueue1)
        );

        topicServiceImpl.process(
                new Request("GET", "topic", "weather", queue2)
        );
        topicServiceImpl.process(
                new Request("POST", "topic", "weather", paramForQueue2)
        );

        Response result1 = topicServiceImpl.process(
                new Request("GET", "topic", "weather", queue1)
        );
        Response result2 = topicServiceImpl.process(
                new Request("GET", "topic", "weather", queue2)
        );
        Assert.assertThat(result1.text(), Matchers.is("temperature=18"));
        Assert.assertThat(result2.text(), Matchers.is("temperature=1488"));
    }

    @Test
    public void whenCreateTopicWithEmptyQueueThenGet404() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        String queue1 = "client1";
        topicServiceImpl.process(
                new Request("GET", "topic", "weather", queue1)
        );
        Response result1 = topicServiceImpl.process(
                new Request("GET", "topic", "weather", queue1)
        );
        Assert.assertThat(result1.text(), Matchers.is("Error, param not found"));
    }

    @Test
    public void whenCreateTopicWithAndGetWithIncorrectQueueNameThenGet404() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        String queue = "client1";
        String incorrectQueue = "client100500";
        topicServiceImpl.process(
                new Request("GET", "topic", "weather", queue)
        );
        Response result1 = topicServiceImpl.process(
                new Request("GET", "topic", "weather", incorrectQueue)
        );
        Assert.assertThat(result1.text(), Matchers.is("Error, param not found"));
    }

    @Test
    public void whenCreateTopicWithAndPostMsgWithIncorrectSourceNameThenGet404() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        String queue = "client1";
        String incorrectTopicName = "topic100500";
        topicServiceImpl.process(
                new Request("GET", "topic", "weather", queue)
        );
        Response result1 = topicServiceImpl.process(
                new Request("POST", "topic", incorrectTopicName, queue)
        );
        Assert.assertThat(result1.text(), Matchers.is("Internal server error"));
    }

    @Test
    public void whenSendIncorrectMsgThenGet500() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        String queue = "client1";
        topicServiceImpl.process(
                new Request("GET", "topic", "weather", queue)
        );
        Response result = topicServiceImpl.process(
                new Request("PUT", "topic", "weather", queue)
        );
        Assert.assertThat(result.text(), Matchers.is("Internal server error"));
    }

}