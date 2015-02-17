package com.pracbiz.b2bportal.core.ntlm2.filter;

import java.security.Principal;

public class NtlmV2Principal implements Principal
{

    /** Stores the NTLM username. */
    private final String userName;


    /**
     * Creates an NTLM principal holder.
     * 
     * @param userName The Windows username.
     */
    public NtlmV2Principal(String userName)
    {
        this.userName = userName;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.security.Principal#getName()
     */
    @Override
    public String getName()
    {
        return this.userName;
    }
}
