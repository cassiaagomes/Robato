package com.robato.diagnosticos.domain.service;

import java.util.concurrent.atomic.AtomicLong;


public final class GeradorIDSequencial {

    private static final GeradorIDSequencial INSTANCE = new GeradorIDSequencial();
    private final AtomicLong contador = new AtomicLong(1);

    private GeradorIDSequencial() {}

    public static GeradorIDSequencial getInstance() {
        return INSTANCE;
    }

    public long proximoId() {
        return contador.getAndIncrement();
    }
}