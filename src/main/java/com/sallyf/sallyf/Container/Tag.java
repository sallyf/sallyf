package com.sallyf.sallyf.Container;

import java.util.Objects;

public class Tag<T extends ContainerAwareInterface>
{
    private String name;

    public Tag(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Tag setName(String name)
    {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tag<?> tag = (Tag<?>) o;
        return Objects.equals(getName(), tag.getName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getName());
    }
}
