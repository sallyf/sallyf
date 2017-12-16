package com.sallyf.sallyf.Server;

import fi.iki.elonen.NanoHTTPD;

public class Cookie extends NanoHTTPD.Cookie
{
    private final String path;

    public Cookie(String name, String value, int numDays, String path)
    {
        super(name, value, numDays);
        this.path = path;
    }

    @Override
    public String getHTTPHeader()
    {
        String header = super.getHTTPHeader();

        if (path != null) {
            header += "; path=" + path;
        }

        return header;
    }
}
