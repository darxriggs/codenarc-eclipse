package org.codenarc.eclipse

import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.IAdaptable
import org.eclipse.jface.viewers.IStructuredSelection

class SelectionUtils {

	// TODO: support selecting a project, different folders and files
	// see FindBugsAction.java from Eclipse-FindBugs
	static List<IFile> getGroovyFiles(IStructuredSelection selection) {
		def files = []

		if (selection) {
			for (object in selection.toList()) {
				if (object instanceof IAdaptable) {
					IFile file = ((IAdaptable) object).getAdapter(IFile)

					if (file && file.type == IResource.FILE && file.fileExtension == 'groovy') {
						files.add(file)
					}
				}
			}
		}

		files
	}
}
