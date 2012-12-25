package org.codenarc.eclipse.plugin.preferences

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer

import org.codenarc.eclipse.Activator

/**
 * Class used to initialize default preference values.
 */
class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    void initializeDefaultPreferences() {
        def store = Activator.default.preferenceStore
        store.setDefault(PreferenceConstants.USE_PROJECT_SETTINGS, false)
        store.setDefault(PreferenceConstants.USE_CUSTOM_RULESET, false)
        store.setDefault(PreferenceConstants.RULESET_FILES, '')
    }
}
