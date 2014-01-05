package test

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext

class Activator implements BundleActivator {

    private static BundleContext context

    static BundleContext getContext() {
        context
    }

    void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext
    }

    void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null
    }
}
