package org.codenarc.eclipse.autocheck

import org.codenarc.eclipse.jobs.CheckCodeJob
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.core.runtime.jobs.Job

/**
 * @author Sergey Tselovalnikov
 * @since 30 Jul 2013 г.
 */
class Autochecker extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        
        def job = new CheckCodeJob()
        job.priority = Job.INTERACTIVE
        job.schedule()
        
        return null;
    }
}
