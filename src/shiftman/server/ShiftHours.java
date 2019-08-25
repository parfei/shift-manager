package shiftman.server;

/**
 * Class for the shift hours of a shift.
 */
public class ShiftHours extends Hours {

	public ShiftHours(String start, String end) throws RosterException {
		super(start, end);
	}

	public void rosterInTime(String workingHours) throws RosterException {
		//convert into integer values.
		int _startWH = Integer.parseInt(workingHours.substring(0,4));
		int _endWH = Integer.parseInt(workingHours.substring(4,8));

		//If the start and the end times of the shift is not within the working hours of the day, throw exception.
		if (Integer.parseInt(_startTime) < _startWH || Integer.parseInt(_startTime) > _endWH
				|| Integer.parseInt(_endTime) > _endWH || Integer.parseInt(_endTime) < _startWH) {
			throw new RosterException("ERROR: shift scheduled is not in working hours range.");
		}

	}

}