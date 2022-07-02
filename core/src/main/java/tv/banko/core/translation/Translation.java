package tv.banko.core.translation;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translation {

    private final String resourceBundlePrefix;
    private final ClassLoader loader;

    public Translation(String resourceBundlePrefix) {
        this.resourceBundlePrefix = resourceBundlePrefix;
        this.loader = getClass().getClassLoader();
    }

    public Translation(String resourceBundlePrefix, ClassLoader loader) {
        this.resourceBundlePrefix = resourceBundlePrefix;
        this.loader = loader;
    }

    public String get(String key, Object... format) {
        return get(key, Locale.GERMAN, format);
    }

    public String get(String key, Locale locale, Object... format) {
        try {
            ResourceBundle bundle = getResourceBundle(locale);

            if (!bundle.containsKey(key)) {
                bundle = getDefaultResourceBundle();

                if (!bundle.containsKey(key)) {
                    return key;
                }
            }

            if (format.length == 0) {
                return bundle.getString(key);
            }

            return MessageFormat.format(bundle.getString(key), format);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    private ResourceBundle getResourceBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle(resourceBundlePrefix, locale, loader);
        } catch (MissingResourceException e) {
            return getDefaultResourceBundle();
        }
    }

    private ResourceBundle getDefaultResourceBundle() {
        return ResourceBundle.getBundle(resourceBundlePrefix, Locale.GERMAN, loader);
    }
}
