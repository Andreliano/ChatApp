package com.example.laborator6.utils.observer;


import com.example.laborator6.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}