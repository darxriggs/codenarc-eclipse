package org.codenarc.eclipse

import org.eclipse.core.runtime.ILog
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status

@Singleton
class Logger {

    private static final ILog log = Activator.default.log

    static void info(message) {
        log.log new Status(IStatus.INFO, Activator.PLUGIN_ID, message)
    }

    static void warning(message) {
        log.log new Status(IStatus.WARNING, Activator.PLUGIN_ID, message)
    }

    static void error(message) {
        log.log new Status(IStatus.ERROR, Activator.PLUGIN_ID, message)
    }

    static void error(message, exception) {
        log.log new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, exception)
    }
}
