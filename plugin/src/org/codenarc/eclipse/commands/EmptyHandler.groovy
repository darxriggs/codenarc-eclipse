package org.codenarc.eclipse.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public abstract class EmptyHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        handle(event)
        return null
    }

    /**
     * Should be overrided 
     */
    protected abstract void handle(ExecutionEvent event)
}
