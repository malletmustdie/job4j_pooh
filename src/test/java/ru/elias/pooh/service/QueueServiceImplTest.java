package ru.elias.pooh.service;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import ru.elias.pooh.model.Request;
import ru.elias.pooh.model.Response;
import ru.elias.pooh.service.impl.QueueServiceImpl;
import ru.elias.pooh.util.ApiConstants;

public class QueueServiceImplTest {

    @Test
    public void whenPostOneMsgInQueueThenGetOneParameter() {
        QueueServiceImpl queueServiceImpl = new QueueServiceImpl();
        String paramForPostMethod = "param=value";
        queueServiceImpl.process(
                new Request("POST", "queue", "weather", paramForPostMethod)
        );
        Response result = queueServiceImpl.process(
                new Request("GET", "queue", "weather", null)
        );
        Assert.assertThat(result.text(), Matchers.is("param=value"));
    }

    @Test
    public void whenPostTwoMsgInQueueThenGetTwoParameter() {
        QueueServiceImpl queueServiceImpl = new QueueServiceImpl();
        String paramForPostMethod = "param=value";
        String paramForPostMethod1 = "param=value-1";
        queueServiceImpl.process(
                new Request("POST", "queue", "weather", paramForPostMethod)
        );
        queueServiceImpl.process(
                new Request("POST", "queue", "weather", paramForPostMethod1)
        );
        Response result = queueServiceImpl.process(
                new Request("GET", "queue", "weather", null)
        );
        Assert.assertThat(result.text(), Matchers.is("param=value"));

        Response result1 = queueServiceImpl.process(
                new Request("GET", "queue", "weather", null)
        );
        Assert.assertThat(result1.text(), Matchers.is("param=value-1"));
    }


    @Test
    public void whenSendIncorrectMsgThenGetError() {
        QueueServiceImpl queueServiceImpl = new QueueServiceImpl();
        String paramForPostMethod = "param=value";
        String incorrectSourceName = "some-name";
        queueServiceImpl.process(
                new Request("POST", "queue", "weather", paramForPostMethod)
        );
        Response result = queueServiceImpl.process(
                new Request("GET", "queue", incorrectSourceName, null)
        );
        Assert.assertThat(result.text(), Matchers.is(ApiConstants.ERROR));
    }

}