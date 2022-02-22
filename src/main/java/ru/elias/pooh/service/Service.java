package ru.elias.pooh.service;

import ru.elias.pooh.model.Request;
import ru.elias.pooh.model.Response;

public interface Service {
    Response process(Request request);
}