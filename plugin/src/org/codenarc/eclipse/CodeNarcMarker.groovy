package org.codenarc.eclipse

import groovy.transform.CompileStatic

/**
 * Access for the CodeNarc marker types defined in plugin.xml.
 */
@CompileStatic
class CodeNarcMarker {

    static final String SUPER_TYPE = 'org.codenarc.eclipse.marker.violation'

    static String getMarkerTypeForPriority(priority) {
        SUPER_TYPE + '.priority' + priority
    }
}
