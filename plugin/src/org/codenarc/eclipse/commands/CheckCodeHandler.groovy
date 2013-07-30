package org.codenarc.eclipse.commands;

import org.codenarc.eclipse.jobs.CheckCodeJob
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job

public class CheckCodeHandler extends EmptyHandler {

    @Override
    protected void handle(ExecutionEvent event) {
        def job = new CheckCodeJob()
        job.priority = Job.INTERACTIVE
        job.schedule()
    }
}
