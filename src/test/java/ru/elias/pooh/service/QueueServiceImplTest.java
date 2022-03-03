package ru.elias.pooh.service;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import ru.elias.pooh.model.Request;
import ru.elias.pooh.model.Response;
import ru.elias.pooh.service.impl.QueueServiceImpl;

public class QueueServiceImplTest {

    @Test
    public void whenPostOneMsgInQueueThenGetOneParameter() {
        QueueServiceImpl queueServiceImpl = new QueueServiceImpl();
        String paramForPostMethod = "temperature=18";
        queueServiceImpl.process(
                new Request("POST", "queue", "weather", paramForPostMethod, "1")
        );
        Response result = queueServiceImpl.process(
                new Request("GET", "queue", "weather", null, "1")
        );
        Assert.assertThat(result.text(), Matchers.is("temperature=18"));
    }

    @Test
    public void whenPostTwoMsgInQueueThenGetTwoParameter() {
        QueueServiceImpl queueServiceImpl = new QueueServiceImpl();
        String paramForPostMethod = "temperature=18";
        String paramForPostMethod1 = "temperature=1488";
        queueServiceImpl.process(
                new Request("POST", "queue", "weather", paramForPostMethod, "1")
        );
        queueServiceImpl.process(
                new Request("POST", "queue", "weather", paramForPostMethod1, "1")
        );
        Response result = queueServiceImpl.process(
                new Request("GET", "queue", "weather", null, "1")
        );
        Assert.assertThat(result.text(), Matchers.is("temperature=18"));

        Response result1 = queueServiceImpl.process(
                new Request("GET", "queue", "weather", null, "1")
        );
        Assert.assertThat(result1.text(), Matchers.is("temperature=1488"));
    }

    @Test
    public void whenSendIncorrectMsgThenGet404() {
        QueueServiceImpl queueServiceImpl = new QueueServiceImpl();
        String paramForPostMethod = "temperature=18";
        queueServiceImpl.process(
                new Request("POST", "queue", "weather", paramForPostMethod, "1")
        );
        queueServiceImpl.process(new Request("GET", "queue", "weather", null, "1"));
        Response result = queueServiceImpl.process(
                new Request("GET", "queue", "weather", null, "1")
        );
        Assert.assertThat(result.text(), Matchers.is("Error, param not found"));
    }

    @Test
    public void whenSendIncorrectMsgThenGet500() {
        QueueServiceImpl queueServiceImpl = new QueueServiceImpl();
        String paramForPostMethod = "temperature=18";
        Response result = queueServiceImpl.process(
                new Request("PUT", "queue", "weather", paramForPostMethod, "1")
        );
        Assert.assertThat(result.text(), Matchers.is("Internal server error"));
    }

}