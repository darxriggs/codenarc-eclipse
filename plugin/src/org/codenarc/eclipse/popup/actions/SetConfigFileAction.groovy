package org.codenarc.eclipse.popup.actions

import org.codenarc.eclipse.jobs.SetConfigFileJob
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.action.IAction
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.IObjectActionDelegate
import org.eclipse.ui.IWorkbenchPart

class SetConfigFileAction implements IObjectActionDelegate {

    private IStructuredSelection selection

    @Override
    void run(IAction action) {
        def job = new SetConfigFileJob(selection)
        job.priority = Job.INTERACTIVE
        job.schedule()
    }

    @Override
    void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection)  {
            this.selection = selection
        }
    }

    @Override
    void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }
}
