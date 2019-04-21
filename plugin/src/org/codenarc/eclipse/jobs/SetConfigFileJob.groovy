package org.codenarc.eclipse.jobs

import org.codenarc.eclipse.Activator
import org.codenarc.eclipse.SelectionUtils
import org.codenarc.eclipse.preferences.CodeNarcPreferenceConstants
import org.eclipse.core.resources.IFile
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.preference.IPreferenceStore
import org.eclipse.jface.viewers.IStructuredSelection

/**
 * Job to set a preference in current plugin's store.
 * If this value is set, the value's file will be used to retrieve a custom ruleset which
 * this plugin will use.
 *
 * @author <a href="mailto:csaba.sulyok@gmail.com">Csaba Sulyok</a>
 */
class SetConfigFileJob extends Job {

    private IProgressMonitor monitor
    private IStructuredSelection selection

    SetConfigFileJob(IStructuredSelection selection) {
        super('Use this file as CodeNarc config')
        this.selection = selection
    }

    IStatus run(IProgressMonitor monitor) {
        this.monitor = monitor

        monitor.beginTask('Setting current CodeNarc config file', 1)
        changeConfigFile(SelectionUtils.getSingleGroovyFile(selection))
        monitor.worked(1)
        monitor.done()

        monitor.isCanceled() ? Status.CANCEL_STATUS : Status.OK_STATUS
    }

    private void changeConfigFile(IFile file) {
        String path = file.getRawLocation().makeAbsolute().toFile().absolutePath
        IPreferenceStore store = Activator.getDefault().getPreferenceStore()
        store.setValue(CodeNarcPreferenceConstants.P_USE_CUSTOM_CONFIG, true)
        store.setValue(CodeNarcPreferenceConstants.P_CONFIG_PATH, path)
    }
}
