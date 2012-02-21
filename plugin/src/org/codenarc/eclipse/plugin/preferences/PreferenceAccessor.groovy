package org.codenarc.eclipse.plugin.preferences

import org.codenarc.eclipse.Activator
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.QualifiedName
import org.eclipse.jface.preference.IPreferenceStore

import com.bdaum.overlayPages.FieldEditorOverlayPage

class PreferenceAccessor {

    static String getOverlayedPreferenceValue(IResource resource, String key) {

        if (hasProjectSpecificSetting(resource.project)) {
            return getProperty(resource, key)
        } else {
            def store = Activator.default.preferenceStore
            return store.getString(key)
        }
    }

    public static boolean hasProjectSpecificSetting(IResource resource) {
        def property = getProperty(resource, PreferenceConstants.USE_PROJECT_SETTINGS)
        property == 'true'
    }

    private static String getProperty(IResource resource, String key) {
        def qualifiedName = new QualifiedName(PreferencePage.PAGE_ID, key)
        resource.getPersistentProperty(qualifiedName)
    }
}
