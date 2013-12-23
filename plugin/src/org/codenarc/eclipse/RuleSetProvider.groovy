package org.codenarc.eclipse

import org.codenarc.ruleset.CompositeRuleSet
import org.codenarc.ruleset.RuleSet
import org.codenarc.ruleset.RuleSetUtil
import org.eclipse.core.runtime.ILog
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status

/**
 * Provides all rulesets that are shipped with CodeNarc as a composite ruleset.
 */
class RuleSetProvider {

    private static final ILog log = Activator.getDefault().getLog()

    private static final DEFAULT_RULESETS = [
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
        'naming',
        'security',
        'serialization',
        'size',
        'unnecessary',
        'unused']

    static RuleSet createDefaultRuleSet() {
        def paths = DEFAULT_RULESETS.collect{ ruleSet -> "rulesets/${ruleSet}.xml" }

        createRuleSetFromFiles(paths)
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
