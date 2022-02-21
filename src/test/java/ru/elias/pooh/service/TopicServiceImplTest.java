package ru.elias.pooh.service;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import ru.elias.pooh.model.Req;
import ru.elias.pooh.model.Resp;
import ru.elias.pooh.service.impl.TopicServiceImpl;

public class TopicServiceImplTest {

    @Test
    public void whenTopic() {
        TopicServiceImpl topicServiceImpl = new TopicServiceImpl();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicServiceImpl.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicServiceImpl.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicServiceImpl.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        Resp result2 = topicServiceImpl.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        Assert.assertThat(result1.text(), Matchers.is("temperature=18"));
        Assert.assertThat(result2.text(), Matchers.is(""));
    }

}