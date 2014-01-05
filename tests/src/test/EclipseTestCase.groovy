package test

import org.junit.*

abstract class EclipseTestCase {

    protected TestProject testProject

    @Before
    void setUpProject() {
        testProject = new TestProject()
    }

    @After
    void tearDownProject() {
        testProject.dispose()
    }
}