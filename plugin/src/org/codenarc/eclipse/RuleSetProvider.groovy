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
		'design',
		'dry',
		'exceptions',
		'generic',
		'grails',
		'imports',
		'jdbc',
		'junit',
		'logging',
		'naming',
		'size',
		'unnecessary',
		'unused']

	static RuleSet createDefaultRuleSet() {
		def paths = DEFAULT_RULESETS.collect{ ruleSet -> "rulesets/${ruleSet}.xml" }
		def overallRuleSet = new CompositeRuleSet()

		def begin = System.currentTimeMillis()
		for (path in paths) {
			def ruleSet = RuleSetUtil.loadRuleSetFile(path)
			overallRuleSet.addRuleSet(ruleSet)
		}
		def end = System.currentTimeMillis()

		log.log new Status(IStatus.INFO, Activator.PLUGIN_ID, "Loading rulesets took ${end - begin} ms")

		overallRuleSet
	}
}
