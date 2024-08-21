package org.acme.kogitoexample.model;

public interface BaseObject<T, U> {
    T getId();
    void setId(T id);
}
