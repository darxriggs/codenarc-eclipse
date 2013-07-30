package org.codenarc.eclipse.commands

import org.codenarc.eclipse.jobs.ClearViolationsJob
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job

class ClearViolationsHandler extends EmptyHandler {
    
    @Override
    protected void handle(ExecutionEvent event) {
        def job = new ClearViolationsJob()
        job.priority = Job.INTERACTIVE
        job.schedule()
    }
}
