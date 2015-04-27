package org.john.iplookup;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class IpLookupMetaData implements PluginMetaData {
    @Override
    public String getUniqueId() {
        return "org.john.iplookup.IpLookupPlugin";
    }

    @Override
    public String getName() {
        return "IpLookup";
    }

    @Override
    public String getAuthor() {
        // TODO Insert author name
        return "John Callow";
    }

    @Override
    public URI getURL() {
        // TODO Insert correct plugin website
        return URI.create("https://github.com/jcallow/IpLookup");
    }

    @Override
    public Version getVersion() {
        return new Version(1, 0, 0);
    }

    @Override
    public String getDescription() {
        // TODO Insert correct plugin description
        return "If ip field exists, looks up it's city and country and adds them as fields.";
    }

    @Override
    public Version getRequiredVersion() {
        return new Version(1, 0, 0);
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
