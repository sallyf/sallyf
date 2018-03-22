package com.sallyf.sallyf.FlashManager;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.RedirectResponse;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.RuntimeBag;
import com.sallyf.sallyf.Server.RuntimeBagContext;
import com.sallyf.sallyf.Server.RuntimeStorage;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class FlashManager implements ServiceInterface
{
    private static String SESSION_FLASHES_KEY = "_flashes";

    private static String CURRENT_FLASHES_KEY = "_current_flashes";

    private static String NEXT_FLASHES_KEY = "_next_flashes";

    private static String FORWARD_FLAG = "_forward_flashes";

    private EventDispatcher eventDispatcher;

    private Router router;

    public FlashManager(EventDispatcher eventDispatcher, Router router)
    {
        this.eventDispatcher = eventDispatcher;
        this.router = router;
    }

    @Override
    public void initialize(Container container)
    {
        router.addActionParameterResolver(new FlashManagerHelperActionParameterResolver(container));

        eventDispatcher.register(KernelEvents.POST_MATCH_ROUTE, (eventType, routeMatchEvent) -> {
            hydrateCurrent(); // Move Session flashes to RuntimeStorage
        });

        eventDispatcher.register(KernelEvents.PRE_SEND_RESPONSE, (eventType, responseEvent) -> {
            Response response = responseEvent.getResponse();

            persistNext(); // Persist next flashes to Session

            if (isForwarding() || response instanceof RedirectResponse) {
                forwardCurrent(); // Forward current flashes to Session (next runtime)
            }
        });
    }

    public boolean isForwarding()
    {
        Boolean v = (Boolean) getSession().getAttribute(FORWARD_FLAG);

        return null == v ? false : v;
    }

    public void setForward(boolean value)
    {
        getSession().setAttribute(FORWARD_FLAG, value);
    }

    public void addFlash(FlashEntry<?> entry)
    {
        getNextFlashes().add(entry);
    }

    public Set<FlashEntry<?>> getSessionFlashes()
    {
        HttpSession session = getSession();

        Set<FlashEntry<?>> flashes = (Set<FlashEntry<?>>) session.getAttribute(SESSION_FLASHES_KEY);

        if (null == flashes) {
            flashes = new HashSet<>();
            session.setAttribute(SESSION_FLASHES_KEY, flashes);
        }

        return flashes;
    }

    public Set<FlashEntry<?>> getCurrentFlashes()
    {
        return getRuntimeStorageFlashes(CURRENT_FLASHES_KEY);
    }

    public Set<FlashEntry<?>> getNextFlashes()
    {
        return getRuntimeStorageFlashes(NEXT_FLASHES_KEY);
    }

    private Set<FlashEntry<?>> getRuntimeStorageFlashes(Object key)
    {
        RuntimeBag runtimeBag = RuntimeBagContext.get();

        RuntimeStorage storage = runtimeBag.getStorage();

        Set<FlashEntry<?>> flashes = storage.get(key);

        if (null == flashes) {
            flashes = new LinkedHashSet<>();
            storage.put(key, flashes);
        }

        return flashes;
    }

    private void persistNext()
    {
        Set<FlashEntry<?>> sessionFlashes = getSessionFlashes();
        Set<FlashEntry<?>> nextFlashes = getNextFlashes();

        sessionFlashes.addAll(nextFlashes);

        nextFlashes.clear();
    }

    private void forwardCurrent()
    {
        Set<FlashEntry<?>> sessionFlashes = getSessionFlashes();
        Set<FlashEntry<?>> currentFlashes = getCurrentFlashes();

        sessionFlashes.addAll(currentFlashes);

        currentFlashes.clear();
    }

    private void hydrateCurrent()
    {
        Set<FlashEntry<?>> sessionFlashes = getSessionFlashes();
        Set<FlashEntry<?>> currentFlashes = getCurrentFlashes();

        currentFlashes.addAll(sessionFlashes);

        sessionFlashes.clear();
    }

    private HttpSession getSession()
    {
        RuntimeBag runtimeBag = RuntimeBagContext.get();

        return runtimeBag.getRequest().getSession(true);
    }
}
