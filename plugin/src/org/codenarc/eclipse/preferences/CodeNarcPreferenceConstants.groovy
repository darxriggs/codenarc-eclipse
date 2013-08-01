package org.codenarc.eclipse.preferences

/**
 * @author <a href="mailto:csaba.sulyok@gmail.com">Csaba Sulyok</a>
 */
class CodeNarcPreferenceConstants {

    public static final String P_CONFIG_PATH = 'org.codenarc.eclipse.configFilePath'
    public static final String CONFIG_PATH_LABEL = '&CodeNarc XML ruleset config file:'
    public static final String CONFIG_PATH_DEFAULT = 'D:/codenarc.xml'

    public static final String P_USE_CUSTOM_CONFIG = 'org.codenarc.eclipse.useCustomConfig'
    public static final String USE_CUSTOM_CONFIG_LABEL = '&Use custom CodeNarc XML ruleset config file:'
    public static final boolean USE_CUSTOM_CONFIG_DEFAULT = false

    public static final String PREFERENCE_PAGE_DESCRIPTION = 'CodeNarc preference page'
}
