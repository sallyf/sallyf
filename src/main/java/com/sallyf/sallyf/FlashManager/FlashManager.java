package com.sallyf.sallyf.FlashManager;

import com.sallyf.sallyf.Container.Container;
import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.EventDispatcher.EventDispatcher;
import com.sallyf.sallyf.KernelEvents;
import com.sallyf.sallyf.Router.RedirectResponse;
import com.sallyf.sallyf.Router.Response;
import com.sallyf.sallyf.Router.Router;
import com.sallyf.sallyf.Server.RuntimeBag;
import com.sallyf.sallyf.Server.RuntimeStorage;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
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
            RuntimeBag runtimeBag = routeMatchEvent.getRuntimeBag();

            hydrateCurrent(runtimeBag); // Move Session flashes to RuntimeStorage
        });

        eventDispatcher.register(KernelEvents.PRE_SEND_RESPONSE, (eventType, responseEvent) -> {
            RuntimeBag runtimeBag = responseEvent.getRuntimeBag();
            Response response = responseEvent.getResponse();

            persistNext(runtimeBag); // Persist next flashes to Session

            if (isForwarding(runtimeBag) || response instanceof RedirectResponse) {
                forwardCurrent(runtimeBag); // Forward current flashes to Session (next runtime)
            }
        });
    }

    public boolean isForwarding(RuntimeBag runtimeBag)
    {
        Boolean v = (Boolean) getSession(runtimeBag).getAttribute(FORWARD_FLAG);

        return null == v ? false : v;
    }

    public void setForward(RuntimeBag runtimeBag, boolean value)
    {
        getSession(runtimeBag).setAttribute(FORWARD_FLAG, value);
    }

    public void addFlash(RuntimeBag runtimeBag, FlashEntry<?> entry)
    {
        getNextFlashes(runtimeBag).add(entry);
    }

    public Set<FlashEntry<?>> getSessionFlashes(RuntimeBag runtimeBag)
    {
        HttpSession session = getSession(runtimeBag);

        Set<FlashEntry<?>> flashes = (Set<FlashEntry<?>>) session.getAttribute(SESSION_FLASHES_KEY);

        if (null == flashes) {
            flashes = new HashSet<>();
            session.setAttribute(SESSION_FLASHES_KEY, flashes);
        }

        return flashes;
    }

    public Set<FlashEntry<?>> getCurrentFlashes(RuntimeBag runtimeBag)
    {
        return getRuntimeStorageFlashes(runtimeBag, CURRENT_FLASHES_KEY);
    }

    public Set<FlashEntry<?>> getNextFlashes(RuntimeBag runtimeBag)
    {
        return getRuntimeStorageFlashes(runtimeBag, NEXT_FLASHES_KEY);
    }

    private Set<FlashEntry<?>> getRuntimeStorageFlashes(RuntimeBag runtimeBag, Object key)
    {
        RuntimeStorage storage = runtimeBag.getStorage();

        Set<FlashEntry<?>> flashes = storage.get(key);

        if (null == flashes) {
            flashes = new HashSet<>();
            storage.put(key, flashes);
        }

        return flashes;
    }

    public void persistNext(RuntimeBag runtimeBag)
    {
        Set<FlashEntry<?>> sessionFlashes = getSessionFlashes(runtimeBag);
        Set<FlashEntry<?>> nextFlashes = getNextFlashes(runtimeBag);

        sessionFlashes.addAll(nextFlashes);

        nextFlashes.clear();
    }

    public void forwardCurrent(RuntimeBag runtimeBag)
    {
        Set<FlashEntry<?>> sessionFlashes = getSessionFlashes(runtimeBag);
        Set<FlashEntry<?>> currentFlashes = getCurrentFlashes(runtimeBag);

        sessionFlashes.addAll(currentFlashes);

        currentFlashes.clear();
    }

    public void hydrateCurrent(RuntimeBag runtimeBag)
    {
        Set<FlashEntry<?>> sessionFlashes = getSessionFlashes(runtimeBag);
        Set<FlashEntry<?>> currentFlashes = getCurrentFlashes(runtimeBag);

        currentFlashes.addAll(sessionFlashes);

        sessionFlashes.clear();
    }

    private HttpSession getSession(RuntimeBag runtimeBag)
    {
        return runtimeBag.getRequest().getSession(true);
    }
}
