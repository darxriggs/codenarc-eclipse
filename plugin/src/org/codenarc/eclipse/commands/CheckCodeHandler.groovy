package org.codenarc.eclipse.commands

import groovy.transform.CompileStatic

import org.codenarc.eclipse.jobs.CheckCodeJob
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.handlers.HandlerUtil

@CompileStatic
class CheckCodeHandler extends AbstractHandler {

    @Override
    Object execute(ExecutionEvent event) {
        def selection = HandlerUtil.getActiveWorkbenchWindow(event)?.getActivePage()?.getSelection()
        if (selection instanceof IStructuredSelection) {
            def job = new CheckCodeJob(selection)
            job.priority = Job.INTERACTIVE
            job.schedule()
        }
        null
    }
}
