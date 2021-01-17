package com.example.thesis.views.utilities;

import com.vaadin.flow.shared.Registration;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
public class CommentBroadcaster {
    static Executor executor = Executors.newSingleThreadExecutor();

    static LinkedList<Runnable> listeners = new LinkedList<>();

    public static synchronized Registration register(
            Runnable listener) {
        log.info("New listener added!");
        listeners.add(listener);

        return () -> {
            synchronized (CommentBroadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    public static synchronized void broadcast() {
        for (Runnable listener : listeners) {
            executor.execute(listener);
        }
    }
}