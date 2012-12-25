package org.codenarc.eclipse.plugin.preferences

import org.eclipse.jface.preference.ListEditor
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.FileDialog

/**
 * A field editor to edit files.
 */
class FileListEditor extends ListEditor {

    /**
     * The last file, or <code>null</code> if none.
     */
    private String lastFile

    /**
     * The special label text for the file chooser dialog,
     * or <code>null</code> if none.
     */
    private String fileChooserLabelText

    /**
     * Creates a new editor.
     */
    protected FileListEditor() { }

    /**
     * Creates a new editor.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param fileChooserLabelText the label text displayed for the directory chooser
     * @param parent the parent of the field editor's control
     */
    FileListEditor(String name, String labelText, String fileChooserLabelText, Composite parent) {
        init(name, labelText)
        this.fileChooserLabelText = fileChooserLabelText
        createControl(parent)
    }

    @Override
    protected String createList(String[] items) {
        items.join(File.pathSeparator)
    }

    @Override
    protected String[] parseString(String stringList) {
        stringList.split(File.pathSeparator)
    }

    @Override
    protected String getNewInputObject() {
        FileDialog dialog = new FileDialog(shell, SWT.SHEET)

        if (fileChooserLabelText) {
            dialog.setText(fileChooserLabelText)
        }

        if (lastFile != null && new File(lastFile).exists()) {
            String lastDir = new File(lastFile).absoluteFile.parent
            dialog.setFilterPath(lastDir)
        }

        String file = dialog.open()
        if (file != null) {
            file = file.trim()
            if (file.length() == 0) {
                return null
            }
            lastFile = file
        }

        file
    }
}
