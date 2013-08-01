package org.codenarc.eclipse.commands

import org.codenarc.eclipse.jobs.SetConfigFileJob
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.viewers.ISelection
import org.eclipse.ui.handlers.HandlerUtil

/**
 * @author Csaba Sulyok
 * @author Sergey Tselovalnikov
 */
class SetConfigFileHandler extends EmptyHandler {

    @Override
    protected void handle(ExecutionEvent event) {
        ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection()
        def job = new SetConfigFileJob(selection)
        job.priority = Job.INTERACTIVE
        job.schedule()
    }
}
