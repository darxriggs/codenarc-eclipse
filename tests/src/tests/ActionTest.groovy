package tests

import org.codenarc.eclipse.CodeNarcMarker
import org.codenarc.eclipse.jobs.CheckCodeJob
import org.codenarc.eclipse.jobs.ClearViolationsJob
import org.codenarc.eclipse.popup.actions.CheckCodeAction
import org.codenarc.eclipse.popup.actions.ClearViolationsAction
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jface.viewers.StructuredSelection
import org.junit.Test

import test.EclipseTestCase

class ActionTest extends EclipseTestCase {

    @Test void 'check violation on single file'() {
        def file = createClassWithViolation('Class1')

        // Check for violations
        runActionOnFiles(CheckCodeAction, file)
        assert findCodeNarcMarkers() == [expectedMarkerForClass('Class1')]

        // Clear violation markers
        runActionOnFiles(ClearViolationsAction, file)
        assert findCodeNarcMarkers() == []
    }

    @Test void 'check violation on multiple files via multi-file selection'() {
        def file1 = createClassWithViolation('Class1')
        def file2 = createClassWithViolation('Class2')

        // Check for violations on both files
        runActionOnFiles(CheckCodeAction, file1, file2)
        assert findCodeNarcMarkers() == [expectedMarkerForClass('Class1'), expectedMarkerForClass('Class2')]

        // Clear violation markers for one of the both files
        runActionOnFiles(ClearViolationsAction, file1)
        assert findCodeNarcMarkers() == [expectedMarkerForClass('Class2')]

        // Clear violation markers for the other file
        runActionOnFiles(ClearViolationsAction, file2)
        assert findCodeNarcMarkers() == []
    }

    @Test void 'check violation on multiple files via project selection'() {
        def file1 = createClassWithViolation('Class1')
        def file2 = createClassWithViolation('Class2')

        // Check for violations on both files
        runActionOnFiles(CheckCodeAction, testProject.project)
        assert findCodeNarcMarkers() == [expectedMarkerForClass('Class1'), expectedMarkerForClass('Class2')]

        // Clear violation markers for one of the both files
        runActionOnFiles(ClearViolationsAction, file1)
        assert findCodeNarcMarkers() == [expectedMarkerForClass('Class2')]

        // Clear violation markers for the other file
        runActionOnFiles(ClearViolationsAction, file2)
        assert findCodeNarcMarkers() == []
    }

    private createClassWithViolation(className) {
        testProject.createGroovyTypeAndPackage(
            'test',
            "${className}.groovy",
            "class ${className} { def TEST }\n"
        )
    }

    private expectedMarkerForClass(className) {
        [
            type: 'org.codenarc.eclipse.marker.violation.priority2',
            attributes: [
                message: "PropertyName: The property name TEST in class test.${className} does not match the pattern [a-z][a-zA-Z0-9]*" as String,
                lineNumber: 3,
            ]
        ]
    }

    private runActionOnFiles(actionClass, ...files) {
        files.each{ assert it.exists() }
        def selection = new StructuredSelection(files as List)
        def action = actionClass.newInstance()
        action.selectionChanged(null, selection)
        action.run(null)
        waitForJobAndRefresh(files)
    }

    private waitForJobAndRefresh(...files) {
        Job.jobManager.join(CheckCodeJob, null)
        Job.jobManager.join(ClearViolationsJob, null)
        files.each{ it.parent.refreshLocal(IResource.DEPTH_INFINITE, null) }
    }

    private findCodeNarcMarkers() {
        def markers = testProject.project.findMarkers(CodeNarcMarker.SUPER_TYPE, true, IResource.DEPTH_INFINITE)
        markers.sort{ it.resource }.collect{ [type: it.type,  attributes: it.attributes] }
    }
}
