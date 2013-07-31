package org.codenarc.eclipse.commands;

import org.codenarc.eclipse.jobs.CheckCodeJob
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.handlers.HandlerUtil

public class CheckCodeHandler extends EmptyHandler {

    @Override
    protected void handle(ExecutionEvent event) {
        ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
        def job = new CheckCodeJob(selection)
        job.priority = Job.INTERACTIVE
        job.schedule()
    }
}
