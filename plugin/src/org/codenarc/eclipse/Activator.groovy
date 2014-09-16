package org.codenarc.eclipse

import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext

/**
 * Controls the plug-in life cycle.
 */
class Activator extends AbstractUIPlugin {

    // The plug-in ID
    static final String PLUGIN_ID = 'CodeNarc'

    // The shared instance
    private static Activator plugin

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    void start(BundleContext context) throws Exception {
        super.start(context)
        plugin = this
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    void stop(BundleContext context) throws Exception {
        plugin = null
        super.stop(context)
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    static Activator getDefault() {
        plugin
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    static ImageDescriptor getImageDescriptor(String path) {
        imageDescriptorFromPlugin(PLUGIN_ID, path)
    }
}
