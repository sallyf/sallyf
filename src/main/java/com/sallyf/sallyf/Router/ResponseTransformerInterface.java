package com.sallyf.sallyf.Router;

public interface ResponseTransformerInterface<I, R>
{
    boolean supports(Object response);

    R transform(I response) ;
}
