package shiftman.server;

/**
 * A class for the working hours of the working day.
 */
public class WorkingHours extends Hours {

	public WorkingHours(String start, String end) throws RosterException {
		super(start, end);
	}

	/**
	 * To roster in the time for the working hours of the working day. Excludes the specified excluded time,
	 * can be changed in case the time to exclude is not midnight.
	 * @param exludeTime the time to exclude when checking the working hours. E.g. if excludeTime is midnight,
	 * working hours must not exceed or include this specific time.
	 */
	public void rosterInTime(String excludeTime) throws RosterException{

		int _excludeTime = Integer.parseInt(excludeTime);

		//Proposed working hours must not have excludeTime in its range or have either the start or end time include excludeTime.
		if ((Integer.parseInt(_startTime) < _excludeTime && Integer.parseInt(_endTime) > _excludeTime)
				|| Integer.parseInt(_endTime) == _excludeTime || Integer.parseInt(_startTime) == _excludeTime) {
			throw new RosterException("ERROR: opening hours cannot include nor exceed midnight.");
		}
	}
}
