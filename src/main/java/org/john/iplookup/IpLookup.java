package org.john.iplookup;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.graylog2.plugin.Message;
import org.graylog2.plugin.filters.MessageFilter;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.maxmind.geoip2.model.CityResponse;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * This is the plugin. Your class should implement one of the existing plugin
 * interfaces. (i.e. AlarmCallback, MessageInput, MessageOutput)
 */
public class IpLookup implements MessageFilter {
	private static final Logger log = LoggerFactory.getLogger(IpLookup.class);
	
	private final TimeLimiter timeLimiter;
	private final Timer resolveTime;
	private final Meter resolveTimeouts;
	private final long timeout;
	
	
	@Inject
	public IpLookup(@Named("ip_lookup_timeout") Period resolverTimeout,
					@Named("ip_lookup_maxmindDB") File maxmindDB,
					MetricRegistry metricRegistry) {
		timeout = resolverTimeout.toStandardDuration().getMillis();
		timeLimiter = new SimpleTimeLimiter(
				Executors.newSingleThreadExecutor(
						new ThreadFactoryBuilder()
								.setDaemon(true)
								.setNameFormat("ip-lookup-thread-%d")
								.build()
				)
		);
		this.resolveTime = metricRegistry.timer(name(IpLookup.class, "resolveTime"));
		this.resolveTimeouts = metricRegistry.meter(name(IpLookup.class, "resolveTimeouts"));
		
		MaxDBDAO.getInstance().init(maxmindDB);
	}
	
	@Override
	public boolean filter(Message msg) {
		if (!msg.hasField("ip")) {
			// return false means keep the message, true means drop it
			return false;
		}

		final String ip = (String)msg.getField("ip");
		
		try {
			try (Timer.Context ignored = resolveTime.time()) {
				final CityResponse location = timeLimiter.callWithTimeout(getLookupCallable(ip), timeout, TimeUnit.MILLISECONDS, true);
				if (location.getCity().getName() != null) {
					msg.addField("city", location.getCity().getName());
				} 
				if (location.getCountry() != null) {
					msg.addField("country", location.getCountry().getIsoCode());
				} 
			} catch (IllegalArgumentException e) {
				log.debug("Ip {} of message {} is not an IP literal. Cannot look up location.", ip, msg.getId());
			}
		} catch (Exception e) {
			resolveTimeouts.mark();
			log.debug("Timed out, skipping looking up {} for message {}", ip, msg.getId());
		} 
			
		return false;
	}
	
	protected Callable<CityResponse> getLookupCallable(final String ip) {
		return new Callable<CityResponse>() {
			@Override
			public CityResponse call() throws Exception {
				return MaxDBDAO.getInstance().getLocation(ip);
			}	
		};
	}

	@Override
	public String getName() {
		return "Ip Lookup";
	}

	@Override
	public int getPriority() {
		// Want to run this after extractors gets ip.  Extractors have priority 10.
		return 11;
	}
	
	
}
