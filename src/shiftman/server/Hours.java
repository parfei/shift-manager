package shiftman.server;

/**
 * Abstract class for any type of hours.
 */
public abstract class Hours {

	protected String _startTime, _endTime, _startHour, _startMin, _endHour, _endMin;

	public Hours(String start, String end) throws RosterException {
		// If the start and end times given are null, empty or do not have a length of 5, it is invalid.
		if (((start == null || start.isEmpty()) && (end == null || end.isEmpty())) ||
				(start.length() != 5 && end.length() != 5)) {
			throw new RosterException("ERROR: start and end times given are not valid.");
		}

		// Convert start and end times into separate int variables.
		_startHour = start.substring(0,2);
		_startMin = start.substring(3,5);
		_startTime = _startHour + _startMin;

		_endHour = end.substring(0,2);
		_endMin = end.substring(3,5);
		_endTime = _endHour + _endMin;

		//Checks whether start is the same as or after the end time, or if hours >23 or minutes >59
		if (Integer.parseInt(_startTime) >= Integer.parseInt(_endTime) || 
				Integer.parseInt(_startHour) > 23 || Integer.parseInt(_endHour) > 23 || 
				Integer.parseInt(_startMin) > 59 || Integer.parseInt(_endMin) > 59) {
			throw new RosterException("ERROR: hours cannot be set. The start time is "
					+ "the same as or after the end time.");
		}
	}

	/**
	 * A method to check hours and their validity when rostering hours in.
	 * @param comparison
	 * @throws RosterException
	 */
	public abstract void rosterInTime(String comparison) throws RosterException;

	public String toString() {
		return _startTime + _endTime;
	}
}
