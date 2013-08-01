package org.codenarc.eclipse.commands

import org.codenarc.eclipse.SelectionUtils
import org.codenarc.eclipse.jobs.ClearViolationsJob
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.viewers.ISelection

/**
 * @author stselovalnikov
 * @since 01 Aug. 2013 г.
 */
class ClearViolationsHandler extends EmptyHandler {
    
    @Override
    protected void handle(ExecutionEvent event) {
        ISelection selection = SelectionUtils.getCurrentSelection(event)
        def job = new ClearViolationsJob(selection)
        job.priority = Job.INTERACTIVE
        job.schedule()
    }
}
