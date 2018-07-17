package org.dufy.log.logback;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ch.qos.logback.core.rolling.helper.RollingCalendar;
import ch.qos.logback.core.spi.ContextAwareBase;

/**
 * An enhanced version of {@link RollingCalendar} with support of custom rolling interval
 * 
 * @author Charlie
 * @see ch.qos.logback.core.rolling.helper.RollingCalendar
 */
public class DataLogRollingCalendar extends RollingCalendar {

	private static final long serialVersionUID = -8495650434935598764L;
	int rollingInterval = 1;

	public DataLogRollingCalendar() {
		super();
	}

	public DataLogRollingCalendar(TimeZone tz, Locale locale) {
		super(tz, locale);
	}

	public void init(String datePattern, int rollingInterval) {
		super.init(datePattern);
		if(rollingInterval > 1) {
			this.rollingInterval = rollingInterval;
		}
	}

	@Override
	public void printPeriodicity(ContextAwareBase cab) {
		switch (getPeriodicityType()) {
		case TOP_OF_MILLISECOND:
			cab.addInfo(String.format("Roll-over every %s millisecond(s).", rollingInterval));
			break;

		case TOP_OF_SECOND:
			cab.addInfo(String.format("Roll-over every %s second(s).", rollingInterval));
			break;

		case TOP_OF_MINUTE:
			cab.addInfo(String.format("Roll-over every %s minute(s).", rollingInterval));
			break;

		case TOP_OF_HOUR:
			cab.addInfo(String.format("Roll-over at the top of every %s hour(s).", rollingInterval));
			break;

		case TOP_OF_DAY:
			cab.addInfo(String.format("Roll-over at midnight of every %s day(s).", rollingInterval));
			break;

		case TOP_OF_WEEK:
			cab.addInfo(String.format("Rollover at the start of every %s week(s).", rollingInterval));
			break;

		case TOP_OF_MONTH:
			cab.addInfo(String.format("Rollover at start of every %s month(s).", rollingInterval));
			break;

		default:
			cab.addInfo("Unknown periodicity.");
		}
	}

	@Override
	public Date getNextTriggeringDate(Date now) {
		return getRelativeDate(now, rollingInterval);
	}
}
