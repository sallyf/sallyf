package com.sallyf.sallyf.Authentication.DeniedHandler;

import com.sallyf.sallyf.Authentication.SecurityDeniedHandler;
import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Exception.HttpException;
import com.sallyf.sallyf.Server.Status;

public class ForbiddenHandler implements SecurityDeniedHandler
{
    @Override
    public Object apply(Container container)
    {
        throw new HttpException(Status.FORBIDDEN);
    }
}
