package com.example.thesis.views.utilities;

import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CommentBroadcaster {
    static Executor executor = Executors.newSingleThreadExecutor();

    static LinkedList<Consumer<Long>> listeners = new LinkedList<>();

    public static synchronized Registration register(
            Consumer<Long> listener) {
        listeners.add(listener);

        return () -> {
            synchronized (CommentBroadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    public static synchronized void broadcast(long noticeId) {
        for (Consumer<Long> listener : listeners) {
            executor.execute(() -> listener.accept(noticeId));
        }
    }
}