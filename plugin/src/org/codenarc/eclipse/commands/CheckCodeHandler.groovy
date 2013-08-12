package org.codenarc.eclipse.commands

import org.codenarc.eclipse.SelectionUtils
import org.codenarc.eclipse.jobs.CheckCodeJob
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job

class CheckCodeHandler extends AbstractHandler {

    @Override
    protected void handle(ExecutionEvent event) {
        def selection = SelectionUtils.getCurrentSelection(event)
        def job = new CheckCodeJob(selection)
        job.priority = Job.INTERACTIVE
        job.schedule()
    }
}
