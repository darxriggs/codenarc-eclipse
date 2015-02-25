package org.codenarc.eclipse.plugin.preferences

import groovy.transform.CompileStatic

import com.bdaum.overlayPages.FieldEditorOverlayPage

/**
 * Constant definitions for plug-in preferences
 */
@CompileStatic
class PreferenceConstants {
    public static final String USE_PROJECT_SETTINGS = FieldEditorOverlayPage.USE_PROJECT_SETTINGS
    public static final String USE_CUSTOM_RULESET = 'codenarc.use.custom.ruleset'
    public static final String RULESET_FILE = 'codenarc.ruleset.file'
}
