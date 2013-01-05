package org.codenarc.eclipse

import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.jface.viewers.IStructuredSelection

class SelectionUtils {

    private static final GROOVY_FILE_EXTENSION = 'groovy'
    private static final GRAILS_LINKED_RESOURCES_NAME = '.link_to_grails_plugins'

    static List<IFile> getGroovyFiles(IStructuredSelection selection) {
        def files = []
        addFileResources(selection.toList(), files)

        files
    }

    private static void addFileResources(resources, files) {
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
    }
}
