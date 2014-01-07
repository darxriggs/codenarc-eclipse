package org.codenarc.eclipse.plugin.preferences;

import org.codenarc.eclipse.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.bdaum.overlayPages.FieldEditorOverlayPage;

public class PreferencePage extends FieldEditorOverlayPage implements IWorkbenchPreferencePage {

    public final static String PAGE_ID = "org.codenarc.eclipse.plugin.preferences.PreferencePage";

    private BooleanFieldEditor useCustomRulesetButton;
    private RuleSetFileFieldEditor rulesetFileField;

    public PreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    public void createFieldEditors() {
        useCustomRulesetButton = new BooleanFieldEditor(PreferenceConstants.USE_CUSTOM_RULESET, "Use custom ruleset", getFieldEditorParent());
        rulesetFileField = new RuleSetFileFieldEditor(useCustomRulesetButton, PreferenceConstants.RULESET_FILE, "Ruleset", getFieldEditorParent());

        addField(useCustomRulesetButton);
        addField(rulesetFileField);
    }

    public void init(IWorkbench workbench) {}

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);

        // It's required to dispatch to the logic of each field editor here.
        // Registering a change listener for each field editor does not work.
        // (Only FieldEditorPreferencePage#propertyChange is called then.)
        if (event.getProperty().equals(FieldEditor.VALUE)) {
            checkState();

            if (event.getSource().equals(useCustomRulesetButton)) {
                propertyChangeDisableButton(event);
            }
        }
    }

    @Override
    protected String getPageId() {
        return PAGE_ID;
    }

    private void propertyChangeDisableButton(PropertyChangeEvent event) {
        Object isButtonEnabled = event.getNewValue();
        if (isButtonEnabled instanceof Boolean) {
            rulesetFileField.setEnabled((Boolean) isButtonEnabled, getFieldEditorParent());
        }
    }
}
