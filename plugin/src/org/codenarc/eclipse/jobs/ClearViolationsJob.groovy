package org.codenarc.eclipse.jobs

import org.codenarc.eclipse.CodeNarcMarker
import org.codenarc.eclipse.Logger
import org.codenarc.eclipse.SelectionUtils
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.viewers.IStructuredSelection

class ClearViolationsJob extends Job {

    private static final Logger log = Logger.instance

    private IProgressMonitor monitor
    private IStructuredSelection selection

    ClearViolationsJob(IStructuredSelection selection) {
        super('Clear CodeNarc violations')
        this.selection = selection
    }

    IStatus run(IProgressMonitor monitor) {
        this.monitor = monitor

        def files = selectFiles()
        deleteMarkersFromFiles(files)

        monitor.isCanceled() ? Status.CANCEL_STATUS : Status.OK_STATUS
    }

    @Override
    boolean belongsTo(final Object family) {
        this.class == family
    }

    private List<IFile> selectFiles() {
        monitor.beginTask('Selecting files', 1)
        def files = SelectionUtils.getGroovyFiles(selection)
        monitor.worked(1)

        files
    }

    private void deleteMarkersFromFiles(List<IFile> files) {
        monitor.beginTask('Deleting markers from files', files.size())
        for (file in files) {
            if (monitor.isCanceled()) { return }

            monitor.subTask(file.name)
            deleteMarkersFromFile(file)
            monitor.worked(1)
        }
        monitor.done()
    }

    private void deleteMarkersFromFile(IFile file) {
        try {
            file.deleteMarkers(CodeNarcMarker.SUPER_TYPE, true, IResource.DEPTH_INFINITE)
        } catch (CoreException e) {
            log.error('Could not delete marker', e)
        }
    }
}
