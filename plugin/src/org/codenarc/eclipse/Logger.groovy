package org.codenarc.eclipse

import groovy.transform.CompileStatic

import org.eclipse.core.runtime.ILog
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status

@CompileStatic
class Logger {

    private static final ILog log = Activator.default.log

    static void info(String message) {
        log.log new Status(IStatus.INFO, Activator.PLUGIN_ID, message)
    }

    static void warning(String message) {
        log.log new Status(IStatus.WARNING, Activator.PLUGIN_ID, message)
    }

    static void error(String message) {
        log.log new Status(IStatus.ERROR, Activator.PLUGIN_ID, message)
    }

    static void error(String message, Throwable exception) {
        log.log new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, exception)
    }
}
