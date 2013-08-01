package org.codenarc.eclipse.preferences

import static org.codenarc.eclipse.preferences.CodeNarcPreferenceConstants.*

import org.codenarc.eclipse.Activator
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer
import org.eclipse.jface.preference.IPreferenceStore

/**
 * @author <a href="mailto:csaba.sulyok@gmail.com">Csaba Sulyok</a>
 */
class CodeNarcPreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.default.preferenceStore
        store.setDefault(P_CONFIG_PATH, CONFIG_PATH_DEFAULT)
        store.setDefault(P_USE_CUSTOM_CONFIG, USE_CUSTOM_CONFIG_DEFAULT)
    }
}
