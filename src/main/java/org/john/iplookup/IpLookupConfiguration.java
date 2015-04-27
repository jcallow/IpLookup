package org.john.iplookup;

import java.io.File;

import org.graylog2.plugin.PluginConfigBean;
import org.joda.time.Period;

import com.github.joschi.jadconfig.Parameter;

public class IpLookupConfiguration implements PluginConfigBean{
	
	@Parameter(value = "ip_lookup_timeout")
	private Period resolverTimeout = Period.seconds(2);
	
	public Period getResolverTimeout() {
		return resolverTimeout;
	}
	
	@Parameter(value = "ip_lookup_maxmindDB")
	private File maxmindDB = new File("maxmind/GeoLite2-City.mmdb");
	
	public File getDBFile() {
		return maxmindDB;
	}
}
