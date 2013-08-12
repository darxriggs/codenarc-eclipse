package org.codenarc.eclipse

import groovy.transform.CompileStatic

import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IProjectNature
import org.eclipse.core.resources.IResource
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jface.viewers.IStructuredSelection

@CompileStatic
class SelectionUtils {

    private static final GROOVY_FILE_EXTENSION = 'groovy'
    private static final GRAILS_LINKED_RESOURCES_NAME = '.link_to_grails_plugins'

    static IProject getProject(IStructuredSelection selection) {
        def resources = findResources(selection)

        (IProject) resources.findResult { IResource resource -> resource?.project?.project }
    }

    static List<IFile> getGroovyFiles(IStructuredSelection selection) {
        def resources = findResources(selection)
        def files = []
        addFileResources(resources, files)

        files
    }

    private static List<IResource> findResources(IStructuredSelection selection) {
        def resources = []

        selection.each {
            switch (it) {
                case IResource:        resources << it;                                         break
                case ICompilationUnit: resources << ((ICompilationUnit) it).underlyingResource; break
                case IProjectNature:   resources << ((IProjectNature) it).project;              break
            }
        }

        resources
    }

    private static void addFileResources(List<IResource> resources, List<IFile> files) {
        for (resource in resources) {
            if (!resource.isAccessible() || resource.name == GRAILS_LINKED_RESOURCES_NAME) {
                continue
            }

            if (resource instanceof IFile) {
                IFile file = (IFile) resource
                if (file.type == IResource.FILE && file.fileExtension == GROOVY_FILE_EXTENSION) {
                    files << file
                }
            } else if (resource instanceof IContainer) {
                IContainer container = (IContainer) resource
                addFileResources(container.members() as List, files)
            }
        }
    }
}
