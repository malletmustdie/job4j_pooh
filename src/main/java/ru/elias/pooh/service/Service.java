package ru.elias.pooh.service;

import ru.elias.pooh.model.Req;
import ru.elias.pooh.model.Resp;

public interface Service {
    Resp process(Req req);
}