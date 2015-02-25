package org.codenarc.eclipse

import groovy.transform.CompileStatic

import org.codenarc.ruleregistry.RuleRegistryInitializer
import org.codenarc.ruleset.CompositeRuleSet
import org.codenarc.ruleset.RuleSet
import org.codenarc.ruleset.RuleSetUtil

/**
 * Provides all rulesets that are shipped with CodeNarc as a composite ruleset.
 */
@CompileStatic
class RuleSetProvider {

    private static final Logger log = new Logger()

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
        def paths = (Collection<String>) DEFAULT_RULESETS.collect{ ruleSet -> "rulesets/${ruleSet}.xml" }

        createRuleSetFromFiles(paths)
    }

    static RuleSet createRuleSetFromFiles(Collection<String> paths) {
        new RuleRegistryInitializer().initializeRuleRegistry()

        def invalidFiles = []
        def overallRuleSet = new CompositeRuleSet()

        def begin = System.currentTimeMillis()
        for (path in paths) {
            try {
                def ruleSet = RuleSetUtil.loadRuleSetFile(path)
                overallRuleSet.addRuleSet(ruleSet)
            } catch (e) {
                invalidFiles << (path - 'file:')
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
