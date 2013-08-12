package org.codenarc.eclipse.commands

import org.eclipse.core.commands.AbstractHandler as CoreAbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException

abstract class AbstractHandler extends CoreAbstractHandler {

    @Override
    Object execute(ExecutionEvent event) throws ExecutionException {
        handle(event)
        null
    }

    protected abstract void handle(ExecutionEvent event)
}
