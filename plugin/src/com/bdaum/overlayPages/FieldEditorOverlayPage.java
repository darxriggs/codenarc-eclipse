/*******************************************************************************
 * Copyright (c) 2003 Berthold Daum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *     Berthold Daum
 *******************************************************************************/
package com.bdaum.overlayPages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbenchPropertyPage;

/**
 * @author Berthold Daum
 */
public abstract class FieldEditorOverlayPage
    extends FieldEditorPreferencePage
    implements IWorkbenchPropertyPage {

    /*** Name of resource property for the selection of workbench or project settings ***/
    public static final String USE_PROJECT_SETTINGS = "useProjectSettings";

    public static final String FALSE = "false";
    public static final String TRUE = "true";

    // Stores all created field editors
    private List<FieldEditor> editors = new ArrayList<FieldEditor>();

    // Stores owning element of properties
    private IAdaptable element;

    // Additional elements for property pages
    private Button projectSettingsButton;
    private Link workspaceSettingsLink;

    // Overlay preference store for property pages
    private IPreferenceStore overlayStore;

    // The image descriptor of this pages title image
    private ImageDescriptor image;

    // Cache for page id
    private String pageId;

    /**
     * Constructor
     * @param style - layout style
     */
    public FieldEditorOverlayPage(int style) {
        super(style);
    }

    /**
     * Constructor
     * @param title - title string
     * @param style - layout style
     */
    public FieldEditorOverlayPage(String title, int style) {
        super(title, style);
    }

    /**
     * Constructor
     * @param title - title string
     * @param image - title image
     * @param style - layout style
     */
    public FieldEditorOverlayPage(
        String title,
        ImageDescriptor image,
        int style) {
        super(title, image, style);
        this.image = image;
    }

    /**
     * Returns the id of the current preference page as defined in plugin.xml
     * Subclasses must implement.
     *
     * @return - the qualifier
     */
    protected abstract String getPageId();

    /**
     * Receives the object that owns the properties shown in this property page.
     * @see org.eclipse.ui.IWorkbenchPropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
     */
    public void setElement(IAdaptable element) {
        this.element = element;
    }

    /**
     * Delivers the object that owns the properties shown in this property page.
     * @see org.eclipse.ui.IWorkbenchPropertyPage#getElement()
     */
    public IAdaptable getElement() {
        return element;
    }

    private IResource getElementAsResource() {
        return (IResource) getElement().getAdapter(IResource.class);
    }

    /**
     * Returns true if this instance represents a property page
     * @return - true for property pages, false for preference pages
     */
    public boolean isPropertyPage() {
        return getElement() != null;
    }

    /**
     * We override the addField method. This allows us to store each field editor added by subclasses
     * in a list for later processing.
     *
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#addField(org.eclipse.jface.preference.FieldEditor)
     */
    @Override
    protected void addField(FieldEditor editor) {
        editors.add(editor);
        super.addField(editor);
    }

    /**
     * We override the createControl method.
     * In case of property pages we create a new PropertyStore as local preference store.
     * After all control have been create, we enable/disable these controls.
     *
     * @see org.eclipse.jface.preference.PreferencePage#createControl()
     */
    @Override
    public void createControl(Composite parent) {
        // Special treatment for property pages
        if (isPropertyPage()) {
            // Cache the page id
            pageId = getPageId();
            // Create an overlay preference store and fill it with properties
            overlayStore =
                new PropertyStore(
                    getElementAsResource(),
                    super.getPreferenceStore(),
                    pageId);
            // Set overlay store as current preference store
        }
        super.createControl(parent);
        // Update state of all subclass controls
        if (isPropertyPage())
            updateFieldEditors();
    }

    /**
     * We override the createContents method.
     * In case of property pages we insert a button to enable project-specific settings.
     *
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        if (isPropertyPage())
            createProjectVsWorkspaceSettingsSelection(parent);
        return super.createContents(parent);
    }

    private void createProjectVsWorkspaceSettingsSelection(Composite parent) {
        Composite composite = createProjectVsWorkspaceSettingsSelectionComposite(parent);

        projectSettingsButton = createProjectSettingsButton(composite);
        workspaceSettingsLink = createWorkspaceSettingsLink(composite);
        loadSelectionGroupSettings();

        createSeparator(parent);
    }

    private Composite createProjectVsWorkspaceSettingsSelectionComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return composite;
    }

    private Button createProjectSettingsButton(Composite parent) {
        String label = "Enable pr&oject specific settings";
        final Button button = new Button(parent, SWT.CHECK);
        button.setText(label);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                workspaceSettingsLink.setEnabled(!button.getSelection());
                updateFieldEditors();
            }
        });
        return button;
    }

    private Link createWorkspaceSettingsLink(Composite composite) {
        String text = "&Configure Workspace Settings...";
        Link link = new Link(composite, SWT.NONE);
        link.setFont(composite.getFont());
        link.setText("<A>" + text + "</A>");
        link.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                configureWorkspaceSettings();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        return link;
    }

    private void createSeparator(Composite parent) {
        Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
        separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void loadSelectionGroupSettings() {
        try {
            QualifiedName key = new QualifiedName(pageId, USE_PROJECT_SETTINGS);
            String value = getElementAsResource().getPersistentProperty(key);
            boolean isProjectSpecific = TRUE.equals(value);

            projectSettingsButton.setSelection(isProjectSpecific);
            workspaceSettingsLink.setEnabled(!isProjectSpecific);
        } catch (CoreException e) {
            projectSettingsButton.setSelection(false);
            workspaceSettingsLink.setEnabled(true);
        }
    }

    /**
     * Returns in case of property pages the overlay store,
     * in case of preference pages the standard preference store
     * @see org.eclipse.jface.preference.PreferencePage#getPreferenceStore()
     */
    @Override
    public IPreferenceStore getPreferenceStore() {
        if (isPropertyPage())
            return overlayStore;
        return super.getPreferenceStore();
    }

    /*
     * Enables or disables the field editors and buttons of this page
     */
    private void updateFieldEditors() {
        boolean enabled = projectSettingsButton.getSelection();
        updateFieldEditors(enabled);
    }

    /**
     * Enables or disables the field editors and buttons of this page
     * Subclasses may override.
     * @param enabled - true if enabled
     */
    protected void updateFieldEditors(boolean enabled) {
        Composite parent = getFieldEditorParent();
        for (FieldEditor editor : editors) {
            editor.setEnabled(enabled, parent);
        }
    }

    /**
     * We override the performOk method. In case of property pages we copy the values in the
     * overlay store into the property values of the selected project.
     * We also save the state of the button.
     *
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        boolean result = super.performOk();
        if (result && isPropertyPage()) {
            IResource resource = getElementAsResource();
            try {
                QualifiedName key = new QualifiedName(pageId, USE_PROJECT_SETTINGS);
                String value = projectSettingsButton.getSelection() ? TRUE : FALSE;
                resource.setPersistentProperty(key, value);
            } catch (CoreException e) {
            }
        }
        return result;
    }

    /**
     * We override the performDefaults method. In case of property pages we
     * switch back to the workspace settings and disable the field editors.
     *
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        if (isPropertyPage()) {
            projectSettingsButton.setSelection(false);
            workspaceSettingsLink.setEnabled(true);
            updateFieldEditors();
        }
        super.performDefaults();
    }

    /**
     * Creates a new preferences page and opens it
     */
    protected void configureWorkspaceSettings() {
        try {
            IPreferencePage page = (IPreferencePage) this.getClass().newInstance();
            page.setTitle(getTitle());
            page.setImageDescriptor(image);
            showPreferencePage(pageId, page);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a single preference pages
     * @param id - the preference page identification
     * @param page - the preference page
     */
    protected void showPreferencePage(String id, IPreferencePage page) {
        final IPreferenceNode targetNode = new PreferenceNode(id, page);
        PreferenceManager manager = new PreferenceManager();
        manager.addToRoot(targetNode);
        final PreferenceDialog dialog =
            new PreferenceDialog(getControl().getShell(), manager);
        BusyIndicator.showWhile(getControl().getDisplay(), new Runnable() {
            public void run() {
                dialog.create();
                dialog.setMessage(targetNode.getLabelText());
                dialog.open();
            }
        });
    }

}
