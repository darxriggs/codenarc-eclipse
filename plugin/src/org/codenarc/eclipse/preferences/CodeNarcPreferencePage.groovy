package org.codenarc.eclipse.preferences
class CodeNarcPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    private FileFieldEditor configFileField
    CodeNarcPreferencePage() {
        super(1)
    void createFieldEditors() {
    void init(final IWorkbench workbench) {
        preferenceStore = Activator.default.preferenceStore
        description = PREFERENCE_PAGE_DESCRIPTION