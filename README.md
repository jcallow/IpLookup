# IpLookup
GrayLog Plugin.  If a log contains the ip field, it will attempt to map this ip to a city and country and add these fields to the log.  Uses MaxMindDB local version for the lookup.

MaxMindDB binary file (GeoLite2-city.mmdb) should be placed in ./maxmind by default, or specify the path by placing

ip_lookup_maxmindDB = PATH/GeoLite2-City.mmdb

In the graylog configuration file.
