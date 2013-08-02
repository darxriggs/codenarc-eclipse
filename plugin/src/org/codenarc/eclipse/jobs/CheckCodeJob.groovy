package org.codenarc.eclipse.jobs

import org.codenarc.analyzer.StringSourceAnalyzer
import org.codenarc.eclipse.Activator
import org.codenarc.eclipse.CodeNarcMarker
import org.codenarc.eclipse.RuleSetProvider
import org.codenarc.eclipse.SelectionUtils
import org.codenarc.results.Results
import org.codenarc.results.VirtualResults
import org.codenarc.ruleset.RuleSet
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IMarker
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.ILog
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.viewers.IStructuredSelection

/**
 * @author René Scheibe
 */
class CheckCodeJob extends Job {

    private static final ILog log = Activator.default.log

    private IProgressMonitor monitor
    private IStructuredSelection selection

    CheckCodeJob(IStructuredSelection selection) {
        super('Check code with CodeNarc')
        this.selection = selection
    }

    IStatus run(IProgressMonitor monitor) {
        this.monitor = monitor

        def files = selectFiles()
        def ruleSet = createRuleSet()
        checkFiles(files, ruleSet)

        monitor.isCanceled() ? Status.CANCEL_STATUS : Status.OK_STATUS
    }

    private List<IFile> selectFiles() {
        monitor.beginTask('Selecting files', 1)
        def files = SelectionUtils.getGroovyFiles(selection)
        monitor.worked(1)

        files
    }

    private RuleSet createRuleSet() {
        monitor.beginTask('Loading rulesets', 10)
        def ruleSet = RuleSetProvider.createRuleSet()
        monitor.worked(10)

        ruleSet
    }

    private void checkFiles(List<IFile> files, RuleSet ruleSet) {
        monitor.beginTask('Checking files', files.size())
        for (file in files) {
            if (monitor.isCanceled()) return

            monitor.subTask(file.name)
            checkFile(file, ruleSet)
            monitor.worked(1)
        }
        monitor.done()
    }

    private void checkFile(IFile file, RuleSet ruleSet) {
        try {
            file.deleteMarkers(CodeNarcMarker.SUPER_TYPE, true, IResource.DEPTH_INFINITE)

            def results = analyzeFile(file, ruleSet)

            createViolationMarkers(results, file)
        } catch (CoreException e) {
            log.log new Status(IStatus.ERROR, Activator.PLUGIN_ID, 'Could not create violation marker', e)
        }
    }

    private Results analyzeFile(IFile file, RuleSet ruleSet) {
        def begin = System.currentTimeMillis()
        def results = analyzeSource(file.contents.text, ruleSet)
        def end = System.currentTimeMillis()

        log.log new Status(IStatus.INFO, Activator.PLUGIN_ID, "Analyzing $file.name took ${end - begin} ms")

        results
    }

    private Results analyzeSource(String source, RuleSet ruleSet) {
        def results

        if (source) {
            def analyzer = new StringSourceAnalyzer(source)
            results = analyzer.analyze(ruleSet)
        } else {
            results = new VirtualResults([])
        }

        results
    }

    private void createViolationMarkers(Results results, IFile file) {
        for (violation in results.violations) {
            def rule = violation.rule

            def markerType = CodeNarcMarker.getMarkerTypeForPriority(rule.priority)
            def markerMessage = rule.name
            if (violation.message) markerMessage += ": $violation.message"

            IMarker marker = file.createMarker(markerType)
            marker.setAttribute(IMarker.LINE_NUMBER, violation.lineNumber)
            marker.setAttribute(IMarker.MESSAGE, markerMessage)
        }
    }
}
