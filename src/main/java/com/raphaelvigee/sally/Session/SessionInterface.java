package com.raphaelvigee.sally.Session;

import java.util.Map;

public interface SessionInterface<O>
{
    String getId();

    O get(String name);

    void set(String name, O value);

    Map<String, Object> getAll();
}
