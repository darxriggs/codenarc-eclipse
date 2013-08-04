package org.codenarc.eclipse

import org.codenarc.ruleregistry.RuleRegistryInitializer
import org.codenarc.ruleset.CompositeRuleSet
import org.codenarc.ruleset.RuleSet
import org.codenarc.ruleset.RuleSetUtil

/**
 * Provides all rulesets that are shipped with CodeNarc as a composite ruleset.
 */
class RuleSetProvider {

    private static final Logger log = Logger.instance

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
        //'naming' // TODO: reenable when this commit is released https://github.com/CodeNarc/CodeNarc/commit/43899f4eb7ab2f72713538603c8b23c3c7e7a9d1
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
        new RuleRegistryInitializer().initializeRuleRegistry()

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

        log.info("Loading ${paths?.size()} ruleset(s) took ${end - begin} ms")

        overallRuleSet
    }
}
