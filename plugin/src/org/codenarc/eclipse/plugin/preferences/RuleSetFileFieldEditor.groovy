package org.codenarc.eclipse.plugin.preferences

import groovy.transform.CompileStatic

import org.codenarc.eclipse.RuleSetProvider
import org.eclipse.jface.preference.BooleanFieldEditor
import org.eclipse.jface.preference.FileFieldEditor
import org.eclipse.swt.widgets.Composite

@CompileStatic
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

    @Override
    protected boolean doCheckState() {
        if (!activationButton.booleanValue) { return true }

        try {
            def path = 'file:' + textControl.text
            RuleSetProvider.createRuleSetFromFiles([path])
            true
        } catch (ex) {
            setErrorMessage('Value must be a valid CodeNarc ruleset file')
            false
        }
    }
}
