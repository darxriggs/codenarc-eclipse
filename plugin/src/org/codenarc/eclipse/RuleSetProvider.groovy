package org.codenarc.eclipse

import org.codenarc.eclipse.preferences.CodeNarcPreferenceConstants
import org.codenarc.ruleset.CompositeRuleSet
import org.codenarc.ruleset.RuleSet
import org.codenarc.ruleset.RuleSetUtil
import org.codenarc.ruleset.XmlReaderRuleSet
import org.eclipse.core.runtime.ILog
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.jface.preference.IPreferenceStore

/**
 * Provides all rulesets that are shipped with CodeNarc as a composite ruleset.
 * 
 * @author René Scheibe
 */
class RuleSetProvider {

    private static final ILog log = Activator.getDefault().getLog()
    
    private static final String DEFAULT_RULESET_PATH = 'classpath:org/codenarc/eclipse/rulesets/defaultRuleSet.xml'

    /**
     * Do not uses by default
     */
    private static final FULL_RULESETS = [
        'basic',
        'braces',
        'concurrency',
        'convention',
        'design',
        'dry',
        'exceptions',
        'formatting',
        'generic',
        'grails',
        'groovyism',
        'imports',
        'jdbc',
        'junit',
        'logging',
        //'naming' // TODO: reenable when this commit is released https://github.com/CodeNarc/CodeNarc/commit/43899f4eb7ab2f72713538603c8b23c3c7e7a9d1
        'security',
        'serialization',
        'size',
        'unnecessary',
        'unused']
    
    private static default_ruleset
    
    static RuleSet createFullRuleSet() {
        def paths = FULL_RULESETS.collect{ ruleSet -> "rulesets/${ruleSet}.xml" }
        createRuleSetFromFiles(paths)
    }

    static RuleSet createDefaultRuleSet() {
        createRuleSetFromFiles([] << DEFAULT_RULESET_PATH)
    }

    static File retrieveRuleSetFileFromPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore()
        if (!store.getBoolean(CodeNarcPreferenceConstants.P_USE_CUSTOM_CONFIG)) {
            return null
        }
        String path = store.getString(CodeNarcPreferenceConstants.P_CONFIG_PATH)
        if (!path || !new File(path).exists()) {
            return null
        }
        new File(path)
    }

    static RuleSet createRuleSet() {
        File configFile = retrieveRuleSetFileFromPreferences()
        if (!configFile) {
            if(!default_ruleset) {
                default_ruleset = createDefaultRuleSet()
            }
            return default_ruleset
        }

        CompositeRuleSet overallRuleSet = new CompositeRuleSet()
        new XmlReaderRuleSet(new FileReader(configFile)).rules.each { rule ->
            overallRuleSet.addRule(rule)
        }
        overallRuleSet
    }

    static RuleSet createRuleSetFromFiles(List paths) {
        def invalidFiles = []
        def overallRuleSet = new CompositeRuleSet()

        def begin = System.currentTimeMillis()
        for (path in paths) {
            try {
                def ruleSet = RuleSetUtil.loadRuleSetFile(path)
                overallRuleSet.addRuleSet(ruleSet)
            } catch (e) {
                log.log new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error loading ruleset ${path}", e)
                invalidFiles << path
            }
        }
        def end = System.currentTimeMillis()

        if (invalidFiles) {
            throw new IllegalArgumentException("Invalid ruleset files ${invalidFiles}")
        }

        log.log new Status(IStatus.INFO, Activator.PLUGIN_ID, "Loading ${paths?.size()} ruleset(s) took ${end - begin} ms")

        overallRuleSet
    }
}
