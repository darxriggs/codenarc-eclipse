package org.codenarc.eclipse

import org.codehaus.jdt.groovy.model.GroovyCompilationUnit;
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.ui.IWorkbench
import org.eclipse.ui.PlatformUI


class SelectionUtils {

    private static final GROOVY_FILE_EXTENSION = 'groovy'
    private static final XML_FILE_EXTENSION = 'xml'
    private static final GRAILS_LINKED_RESOURCES_NAME = '.link_to_grails_plugins'
    
    static IFile getCurrentFile() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getWorkbenchWindows()[0].getActivePage().getActiveEditor().getEditorInput().getAdapter(IFile.class)
    }

    static List<IFile> getGroovyFiles(IStructuredSelection strSelection) {
        def selection = []
        strSelection.each {
            if(it instanceof IResource)
                selection << it
            else if(it instanceof GroovyCompilationUnit)
                selection << getFile(((GroovyCompilationUnit)it).getPath())
        }
        if(selection == null || selection.isEmpty()) {
            return [] << getCurrentFile()
        }
        def files = []
        
        addFileResources(selection.toList(), files)

        files
    }
    
    static IFile getSingleXmlFile(IStructuredSelection selection) {
        def files = []
        addFileResources(selection.toList(), files, XML_FILE_EXTENSION)
        assert files.size() == 1
        files[0]
    }

    private static void addFileResources(resources, files, expectedExtension = GROOVY_FILE_EXTENSION) {
        for (IResource resource in resources) {
            if (!resource.isAccessible() || resource.name == GRAILS_LINKED_RESOURCES_NAME) {
                continue
            }

            if (resource instanceof IFile) {
                IFile file = resource
                if (file.type == IResource.FILE && file.fileExtension == expectedExtension) {
                    files << file
                }
            } else if (resource instanceof IContainer) {
                IContainer container = resource
                addFileResources(container.members(), files)
            }
        }
    }
    
    private static IFile getFile(Path path) {
        return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
    }
}
