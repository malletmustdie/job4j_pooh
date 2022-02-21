package ru.elias.pooh.service;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import ru.elias.pooh.model.Req;
import ru.elias.pooh.model.Resp;
import ru.elias.pooh.service.impl.QueueServiceImpl;


public class QueueServiceImplTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueServiceImpl queueServiceImpl = new QueueServiceImpl();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        queueServiceImpl.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueServiceImpl.process(
                new Req("GET", "queue", "weather", null)
        );
        Assert.assertThat(result.text(), Matchers.is("temperature=18"));
    }

}