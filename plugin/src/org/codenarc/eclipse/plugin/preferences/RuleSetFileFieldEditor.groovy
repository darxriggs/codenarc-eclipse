package org.codenarc.eclipse.plugin.preferences

import org.codenarc.eclipse.Logger
import org.codenarc.eclipse.RuleSetProvider
import org.eclipse.jface.preference.BooleanFieldEditor
import org.eclipse.jface.preference.FileFieldEditor
import org.eclipse.swt.widgets.Composite

class RuleSetFileFieldEditor extends FileFieldEditor {

    BooleanFieldEditor activationButton

    RuleSetFileFieldEditor(BooleanFieldEditor activationButton, String name, String labelText, Composite parent) {
        super(name, labelText, false, parent)
        this.activationButton = activationButton
    }

    @Override
    void setEnabled(boolean enabled, Composite parent) {
        super.setEnabled(activationButton.booleanValue && enabled, parent)
    }
}
