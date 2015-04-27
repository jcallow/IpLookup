package org.john.iplookup;

import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Arrays;
import java.util.Collection;

/**
 * Implement the Plugin interface here.
 */
public class IpLookupPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new IpLookupMetaData();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Arrays.<PluginModule>asList(new IpLookupModule());
    }
}
