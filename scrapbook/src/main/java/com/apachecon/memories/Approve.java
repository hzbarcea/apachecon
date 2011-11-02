package com.apachecon.memories;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@AuthorizeInstantiation("admin")
public class Approve extends ScrapbookPage {
    private static final long serialVersionUID = 1L;

    public Approve(final PageParameters parameters) {
    }
}
