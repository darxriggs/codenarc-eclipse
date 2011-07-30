package org.codenarc.eclipse

import java.util.Iterator
import java.util.List

import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.jface.viewers.IStructuredSelection

class SelectionUtils {

    static List<IFile> getGroovyFiles(IStructuredSelection selection) {
        def files = []
        addFileResources(selection.toList(), files)
        return files
    }

    static private void addFileResources(List resources, List files) {
        Iterator iterator = resources.iterator()
        while (iterator.hasNext()) {
            IResource resource = iterator.next()

            if (!resource.isAccessible()) {
                continue
            }

            if (resource instanceof IFile) {
                IFile file = resource
                if (file.type == IResource.FILE && file.fileExtension == 'groovy') {
                    files.add(file)
                }
            } else if (resource instanceof IContainer) {
                addFileResources(resource.members() as List, files)
            }
        }
    }
}
