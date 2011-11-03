package com.apachecon.memories.session;

import com.apachecon.memories.Index;
import com.apachecon.memories.ScrapbookPage;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.cycle.RequestCycle;

@AuthorizeInstantiation("admin")
public class Logout extends ScrapbookPage {

    private static final long serialVersionUID = 8788191853758438793L;

    public Logout() {
        AuthenticatedWebSession.get().invalidateNow();

        RequestCycle.get().setResponsePage(Index.class);
    }
}
