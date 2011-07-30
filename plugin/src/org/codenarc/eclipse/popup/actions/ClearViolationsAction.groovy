package org.codenarc.eclipse.popup.actions

import org.codenarc.eclipse.Activator
import org.codenarc.eclipse.CodeNarcMarker
import org.codenarc.eclipse.SelectionUtils
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.jface.action.IAction
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.IObjectActionDelegate
import org.eclipse.ui.IWorkbenchPart

class ClearViolationsAction implements IObjectActionDelegate {

    private IStructuredSelection selection

    @Override
    void run(IAction action) {
        def files = SelectionUtils.getGroovyFiles(selection)

        for (file in files) {
            try {
                file.deleteMarkers(CodeNarcMarker.SUPER_TYPE, true, IResource.DEPTH_INFINITE)
            } catch (CoreException e) {
                def status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, 'Could not delete marker', e)
                Activator.getDefault().getLog().log(status)
            }
        }
    }

    @Override
    void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection)  {
            this.selection = selection
        }
    }

    @Override
    void setActivePart(IAction action, IWorkbenchPart targetPart) {}
}
