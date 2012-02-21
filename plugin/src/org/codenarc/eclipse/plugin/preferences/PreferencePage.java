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

    private BooleanFieldEditor disableButton;
    private FileListEditor rulesetList;

    public PreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    public void createFieldEditors() {
        disableButton = new BooleanFieldEditor(PreferenceConstants.USE_PROJECT_SETTINGS, "Use custom ruleset", getFieldEditorParent());
        rulesetList = new FileListEditor(PreferenceConstants.RULESET_FILES, "", "Choose a ruleset file", getFieldEditorParent());

        addField(disableButton);
        addField(rulesetList);
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

            if (event.getSource().equals(disableButton)) {
                propertyChangeDisableButton(event);
            }
        }
    }

    @Override
    protected String getPageId() {
        return PAGE_ID;
    }

    @Override
    protected void initialize() {
        super.initialize();
        rulesetList.setEnabled(disableButton.getBooleanValue(), getFieldEditorParent());
    }

    private void propertyChangeDisableButton(PropertyChangeEvent event) {
        Object isButtonEnabled = event.getNewValue();
        if (isButtonEnabled instanceof Boolean) {
            rulesetList.setEnabled((Boolean) isButtonEnabled, getFieldEditorParent());
        }
    }
}
