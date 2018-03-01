package com.sallyf.sallyf.Utils;

import java.util.Objects;

public class DataUtils
{
    public static <D> D fallback(D v, D fallback)
    {
        if (Objects.isNull(v)) {
            return fallback;
        }

        return v;
    }
}
