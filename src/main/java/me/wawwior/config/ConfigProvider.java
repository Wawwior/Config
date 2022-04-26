package me.wawwior.config;

/**
 * Used to categorize configs / config trees.
 */
public class ConfigProvider {

    public final String pathName;

    public final boolean listenToChildren;

    /**
     * @param path The root path where all configs using this provider go
     * @param listenToChildren Whether children of configurables using this provider should be allowed to load/save the whole tree or not
     */
    public ConfigProvider(String path, boolean listenToChildren) {
        this.listenToChildren = listenToChildren;
        if (!path.endsWith("/")) path += "/";
        pathName = path;
    }

}
