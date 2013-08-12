package org.codenarc.eclipse

import org.codehaus.jdt.groovy.model.GroovyCompilationUnit
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProjectNature
import org.eclipse.core.resources.IResource
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.handlers.HandlerUtil

class SelectionUtils {

    private static final GROOVY_FILE_EXTENSION = 'groovy'
    private static final GRAILS_LINKED_RESOURCES_NAME = '.link_to_grails_plugins'

    static List<IFile> getGroovyFiles(IStructuredSelection selection) {
        def resources = []

        selection.each {
            switch (it) {
                case IResource:             resources << it;                    break
                case GroovyCompilationUnit: resources << it.underlyingResource; break
                case IProjectNature:        resources << it.project;            break
            }
        }

        addFileResources(resources, [])
    }

    static ISelection getCurrentSelection(ExecutionEvent event) {
        HandlerUtil.getActiveWorkbenchWindow(event)?.getActivePage()?.getSelection()
    }

    private static List addFileResources(resources, files) {
        for (IResource resource in resources) {
            if (!resource.isAccessible() || resource.name == GRAILS_LINKED_RESOURCES_NAME) {
                continue
            }

            if (resource instanceof IFile) {
                IFile file = resource
                if (file.type == IResource.FILE && file.fileExtension == GROOVY_FILE_EXTENSION) {
                    files << file
                }
            } else if (resource instanceof IContainer) {
                IContainer container = resource
                addFileResources(container.members(), files)
            }
        }

        files
    }
}
