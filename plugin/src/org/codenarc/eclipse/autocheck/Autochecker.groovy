package org.codenarc.eclipse.autocheck

import org.codenarc.eclipse.commands.EmptyHandler
import org.codenarc.eclipse.jobs.CheckCodeJob
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.runtime.jobs.Job

/**
 * In the future version may be called, when file saved
 * 
 * @author Sergey Tselovalnikov
 * @since 30 Jul 2013 г.
 */
class Autochecker extends EmptyHandler {

    @Override
    protected void handle(ExecutionEvent event) {
        def job = new CheckCodeJob()
        job.priority = Job.INTERACTIVE
        job.schedule()
    }
}
