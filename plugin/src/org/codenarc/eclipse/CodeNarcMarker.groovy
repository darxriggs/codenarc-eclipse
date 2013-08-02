package org.codenarc.eclipse

/**
 * Access for the CodeNarc marker types defined in plugin.xml.
 * 
 * @author René Scheibe
 */
class CodeNarcMarker {

    static final String SUPER_TYPE = 'org.codenarc.eclipse.marker.violation'

    static String getMarkerTypeForPriority(priority) {
        SUPER_TYPE + '.priority' + priority
    }
}
