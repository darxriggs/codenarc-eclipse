package org.codenarc.eclipse.popup.actions

import groovy.transform.CompileStatic

import org.codenarc.eclipse.jobs.CheckCodeJob
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.action.IAction
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.IObjectActionDelegate
import org.eclipse.ui.IWorkbenchPart

@CompileStatic
class CheckCodeAction implements IObjectActionDelegate {

    private IStructuredSelection selection

    @Override
    void run(IAction action) {
        def job = new CheckCodeJob(selection)
        job.priority = Job.INTERACTIVE
        job.schedule()
    }

    @Override
    void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection)  {
            this.selection = (IStructuredSelection) selection
        }
    }

    @Override
    void setActivePart(IAction action, IWorkbenchPart targetPart) { }
}
