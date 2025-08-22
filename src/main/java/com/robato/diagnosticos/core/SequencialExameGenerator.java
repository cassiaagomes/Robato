package com.robato.diagnosticos.core;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SequencialExameGenerator {
    private final AtomicInteger contador = new AtomicInteger(1);
    public int proximo() { return contador.getAndIncrement(); }
}
