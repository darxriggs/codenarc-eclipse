package org.codenarc.eclipse.commands

import org.codenarc.eclipse.jobs.CheckCodeJob
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.ui.handlers.HandlerUtil

class CheckCodeHandler extends AbstractHandler {

    @Override
    protected void handle(ExecutionEvent event) {
        def selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection()
        def job = new CheckCodeJob(selection)
        job.priority = Job.INTERACTIVE
        job.schedule()
    }
}
