package de.codecentric.android.timer.util;

import java.io.Serializable;

public class TimeParts implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final TimeParts ZERO = fromTimeParts(0, 0, 0, 0, 0);
	public static final TimeParts TEN_SECONDS = fromTimeParts(0, 10);
	public static final TimeParts FIVE_MINUTES = fromTimeParts(5, 0);
	public static final TimeParts TEN_MINUTES = fromTimeParts(10, 0);
	public static final TimeParts FIFTEEN_MINUTES = fromTimeParts(15, 0);

	static final int MILLIS_PER_SECOND = 1000;
	static final int SECONDS_PER_MINUTE = 60;
	static final int MILLIS_PER_MINUTE = MILLIS_PER_SECOND * SECONDS_PER_MINUTE;
	static final int MINUTES_PER_HOUR = 60;
	static final int MILLIS_PER_HOUR = MILLIS_PER_MINUTE * MINUTES_PER_HOUR;
	static final int HOURS_PER_DAY = 24;
	static final int MILLIS_PER_DAY = MILLIS_PER_HOUR * HOURS_PER_DAY;
	static final int DAYS_PER_NON_LEAP_YEAR = 365;
	static final long MILLIS_PER_YEAR = ((long) MILLIS_PER_DAY)
			* DAYS_PER_NON_LEAP_YEAR;

	private final long millisecondsTotal;

	private final int days;
	private final int hours;
	private final int minutes;
	private final int seconds;
	private final int milliseconds;

	private TimeParts(long millisecondsTotal, int days, int hours, int minutes,
			int seconds, int milliseconds) {
		this.millisecondsTotal = millisecondsTotal;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.milliseconds = milliseconds;
	}

	/**
	 * Creates a TimesParts object by calculating each time part (days, hours,
	 * minutes, seconds) from the given {@code millisecondsTotal}, rounding up
	 * to the next full second. The milliseconds part of the resulting TimeParts
	 * object will always be 0.
	 * 
	 * @param millisecondsTotal
	 *            the number of milliseconds
	 * @return a TimeParts object approximately equal to
	 *         {@code millisecondsTotal}, rounded up to the next full second.
	 */
	public static TimeParts fromMillisRoundingUp(long millisecondsTotal) {
		checkBounds(millisecondsTotal);
		int days = (int) (millisecondsTotal / MILLIS_PER_DAY);
		int remainder = (int) (millisecondsTotal - (days * MILLIS_PER_DAY));
		int hours = remainder / MILLIS_PER_HOUR;
		remainder = (int) (remainder - (hours * MILLIS_PER_HOUR));
		int minutes = remainder / MILLIS_PER_MINUTE;
		remainder = remainder - (minutes * MILLIS_PER_MINUTE);
		int seconds = remainder / MILLIS_PER_SECOND;
		remainder = remainder - (seconds * MILLIS_PER_SECOND);
		if (remainder > 0) {
			seconds++;
		}
		if (seconds == SECONDS_PER_MINUTE) {
			seconds = 0;
			minutes++;
		}
		if (minutes == MINUTES_PER_HOUR) {
			minutes = 0;
			hours++;
		}
		if (hours == HOURS_PER_DAY) {
			hours = 0;
			days++;
		}

		return new TimeParts(millisecondsTotal, days, hours, minutes, seconds,
				0);
	}

	/**
	 * Creates a TimesParts object by calculating each time part (days, hours,
	 * minutes, seconds, milliseconds) from the given {@code millisecondsTotal}.
	 * 
	 * @param millisecondsTotal
	 *            the number of milliseconds
	 * @return a TimeParts object equal to {@code millisecondsTotal}
	 */
	public static TimeParts fromMillisExactly(long millisecondsTotal) {
		checkBounds(millisecondsTotal);
		int days = (int) (millisecondsTotal / MILLIS_PER_DAY);
		int remainder = (int) (millisecondsTotal - (days * MILLIS_PER_DAY));
		int hours = remainder / MILLIS_PER_HOUR;
		remainder = remainder - (hours * MILLIS_PER_HOUR);
		int minutes = remainder / MILLIS_PER_MINUTE;
		remainder = remainder - (minutes * MILLIS_PER_MINUTE);
		int seconds = remainder / MILLIS_PER_SECOND;
		int milliseconds = remainder - (seconds * MILLIS_PER_SECOND);
		return new TimeParts(millisecondsTotal, days, hours, minutes, seconds,
				milliseconds);
	}

	private static void checkBounds(long millisecondsTotal) {
		if (millisecondsTotal < 0) {
			throw new IllegalArgumentException("millisecondsTotal < 0: "
					+ millisecondsTotal);
		}
		if (millisecondsTotal > MILLIS_PER_YEAR) {
			throw new IllegalArgumentException("Too large: "
					+ millisecondsTotal);
		}
	}

	public static TimeParts fromTimeParts(int minutes, int seconds) {
		return fromTimeParts(0, minutes, seconds);
	}

	public static TimeParts fromTimeParts(int hours, int minutes, int seconds) {
		return fromTimeParts(0, hours, minutes, seconds, 0);
	}

	public static TimeParts fromTimeParts(int days, int hours, int minutes,
			int seconds, int milliseconds) {
		long millisecondsTotal = days * MILLIS_PER_DAY + hours
				* MILLIS_PER_HOUR + minutes * MILLIS_PER_MINUTE + seconds
				* MILLIS_PER_SECOND + milliseconds;
		return new TimeParts(millisecondsTotal, days, hours, minutes, seconds,
				milliseconds);
	}

	public static TimeParts fromTimePartsArray(int[] timePartsArray) {
		return fromTimeParts(timePartsArray[0], timePartsArray[1],
				timePartsArray[2], timePartsArray[3], timePartsArray[4]);
	}

	public int getDays() {
		return this.days;
	}

	public int getHours() {
		return this.hours;
	}

	public int getMinutes() {
		return this.minutes;
	}

	public int getSeconds() {
		return this.seconds;
	}

	public int getMilliseconds() {
		return this.milliseconds;
	}

	public long getMillisecondsTotal() {
		return this.millisecondsTotal;
	}

	public int[] toIntArray() {
		return new int[] { days, hours, minutes, seconds, milliseconds };
	}

	/**
	 * @return a new TimeParts object like this with the days portion removed
	 */
	public TimeParts removeDays() {
		return fromTimeParts(0, this.hours, this.minutes, this.seconds,
				this.milliseconds);
	}

	/**
	 * @return a new TimeParts object like this with the hours portion removed
	 */
	public TimeParts removeHours() {
		return fromTimeParts(this.days, 0, this.minutes, this.seconds,
				this.milliseconds);
	}

	/**
	 * @return a new TimeParts object like this with the minutes portion removed
	 */
	public TimeParts removeMinutes() {
		return fromTimeParts(this.days, this.hours, 0, this.seconds,
				this.milliseconds);
	}

	/**
	 * @return a new TimeParts object like this with the seconds portion removed
	 */
	public TimeParts removeSeconds() {
		return fromTimeParts(this.days, this.hours, this.minutes, 0,
				this.milliseconds);
	}

	/**
	 * @return a new TimeParts object like this with the milliseconds portion
	 *         removed
	 */
	public TimeParts removeMilliseconds() {
		return fromTimeParts(this.days, this.hours, this.minutes, this.seconds,
				0);
	}

	@Override
	public String toString() {
		return "TimeParts [days=" + days + ", hours=" + hours + ", minutes="
				+ minutes + ", seconds=" + seconds + ", milliseconds="
				+ milliseconds + ", millisecondsTotal=" + millisecondsTotal
				+ "]";
	}
}