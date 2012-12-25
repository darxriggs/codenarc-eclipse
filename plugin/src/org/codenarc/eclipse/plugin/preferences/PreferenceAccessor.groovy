package org.codenarc.eclipse.plugin.preferences

import org.codenarc.eclipse.Activator
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.QualifiedName

class PreferenceAccessor {

    static String getOverlayedPreferenceValue(IResource resource, String key) {
        if (hasProjectSpecificSetting(resource.project)) {
            return getProperty(resource, key)
        } else {
            def store = Activator.default.preferenceStore
            return store.getString(key)
        }
    }

    static boolean hasProjectSpecificSetting(IResource resource) {
        def property = getProperty(resource, PreferenceConstants.USE_PROJECT_SETTINGS)
        property == 'true'
    }

    static boolean hasCustomRuleset(IResource resource) {
        def property = getProperty(resource, PreferenceConstants.USE_CUSTOM_RULESET)
        property == 'true'
    }

    private static String getProperty(IResource resource, String key) {
        def qualifiedName = new QualifiedName(PreferencePage.PAGE_ID, key)
        resource.getPersistentProperty(qualifiedName)
    }
}
