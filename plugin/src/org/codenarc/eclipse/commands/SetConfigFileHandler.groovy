package org.codenarc.eclipse.commands

import org.codenarc.eclipse.SelectionUtils
import org.codenarc.eclipse.jobs.SetConfigFileJob
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.viewers.ISelection

/**
 * @author Sergey Tselovalnikov
 */
class SetConfigFileHandler extends EmptyHandler {

    @Override
    protected void handle(ExecutionEvent event) {
        ISelection selection = SelectionUtils.getCurrentSelection(event)
        def job = new SetConfigFileJob(selection)
        job.priority = Job.INTERACTIVE
        job.schedule()
    }
}
