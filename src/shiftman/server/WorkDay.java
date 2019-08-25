package shiftman.server;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the information of a working day. It encloses the inner class Shift.
 * The working day holds information about the day including the shifts scheduled, the working hours and the specific day itself.
 * Add a shift, assign a staff to this working day. Get the working day's roster through this class and also identify any
 * problems with it.
 */
public class WorkDay {

	private final DayShiftList _shiftList = new DayShiftList();
	private Hours _workingHours;
	private String _day;

	public WorkDay(String day) {
		this._day = day;
	}

	/**
	 * Sets the working day hours. If the hours are set, the day is considered rostered on.
	 * @param startTime
	 * @param endTime
	 * @throws RosterException
	 */
	public void setHours(String startTime, String endTime) throws RosterException {
		Hours trial = new WorkingHours(startTime, endTime); //Create a new object for this working day's working hours.
		trial.rosterInTime("0000"); //roster in the time.
		_workingHours = trial; //if it clears rosterInTime checks, the working day will be finalised.
	}

	/**
	 * Roster in a shift to this day.
	 * @param shift
	 * @throws RosterException
	 */
	public void addShift(Shift shift) throws RosterException {
		_shiftList.rosterInShift(shift);
	}

	/**
	 * Assign a staff to a shift to this day.
	 * @param staffToBeAssigned the staff member to be assigned.
	 * @param startTime
	 * @param endTime
	 * @param isManager
	 * @throws RosterException
	 */
	public void assignStaff(Staff staffToBeAssigned, String startTime, String endTime, boolean isManager) throws RosterException {
		Shift shift = _shiftList.getShift(startTime, endTime); //retrieve the relevant shift.
		shift.addStaffToShift(staffToBeAssigned, isManager); //add the staff to this shift.
	}

	/**
	 * Get the working day's roster. This includes the current day, working hours, and the shifts. If there are no working hours
	 * set, i.e. the day is not rostered on, it will throw a specific exception that will be caught before ShiftManServer.
	 * @return arraylist of the working day's roster, with the current day, working hours and the shifts (workers, manager, shift time).
	 * @throws RosterException
	 */
	public List<String> getDayRoster() throws RosterException {
		if (_shiftList.getAllShiftDetails().isEmpty()) {
			//if working hours have not been set or there're no shifts, throw a specific exception that will later be caught before ShiftManServer.
			throw new RosterException("no day");
		}
		List<String> roster = new ArrayList<String>();
		roster.add(_day + " " + _workingHours.toString().substring(0,2) + ":" + _workingHours.toString().substring(2,4) + "-"
				+ _workingHours.toString().substring(4,6) + ":" + _workingHours.toString().substring(6,8)); //add the day and the working hours info.

		roster.addAll(_shiftList.getAllShiftDetails()); //add shift details.
		return roster;

	}

	/**
	 * Investigates the working day and its shift problems.
	 * @param problemType Can only take specific string arguments "without managers", "overstaffed", "understaffed"
	 * @return arraylist of string that includes all shifts that corresponds to the shift problem.
	 */
	public List<String> problems(String problemType){
		switch (problemType) {
		case "without managers":
			return _shiftList.getAllShiftsWithProblems(problemType);
		case "overstaffed":
			return _shiftList.getAllShiftsWithProblems(problemType);
		case "understaffed":
			return _shiftList.getAllShiftsWithProblems(problemType);
		default:
			//throw an unchecked exception if programmer decides to use another problemType input.
			throw new UnsupportedOperationException("WorkDay.problems can only accept arguments \"without managers\" or \"overstaffed\" or \"understaffed\"");
		}
	}

	@Override
	public String toString() {
		return _day;
	}

	/**
	 * This inner class encapsulates all shift details including the shift's staff and the hours of the shift. 
	 * Manage shift staff and get relevant shift details.
	 */
	public class Shift implements Comparable<Shift>{

		private ShiftStaff _shiftStaff;
		private Hours _shiftHours;

		public Shift(String startTime, String endTime, String minWorkers) throws RosterException {
			_shiftStaff = new ShiftStaff(Integer.parseInt(minWorkers));
			Hours trial = new ShiftHours (startTime, endTime); //propose new hours for the shift.
			trial.rosterInTime(_workingHours.toString()); //roster in the hours for the shift.
			_shiftHours = trial; //if it passes rosterInTime checks, it can then finalise hours for the shift.
		}

		/**
		 * Add staff for the shift.
		 * @param staff
		 * @param isManager
		 * @throws RosterException
		 */
		public void addStaffToShift(Staff staff, boolean isManager) throws RosterException {
			if (isManager == true) {
				_shiftStaff.addManager(this, staff);
			} else {
				_shiftStaff.addWorker(this, staff); //if the staff isn't a manager, add it as a worker instead.
			}
		}

		/**
		 * Get shift details, which is the working day and the shift hours.
		 * @return a string of the shift details.
		 */
		public String getShiftDetails() {
			return _day + "[" + _shiftHours.toString().substring(0,2) + ":" + _shiftHours.toString().substring(2,4) + "-"
					+ _shiftHours.toString().substring(4,6) + ":" + _shiftHours.toString().substring(6,8) + "]";
		}

		/**
		 * Get shift staff.
		 * @param staffType Can only pass in arguments "worker" or "manager" otherwise an exception will be thrown.
		 * @return a string of all the staff in the shift.
		 */
		public String getShiftStaff(String staffType) {
			return _shiftStaff.displayShiftStaff(staffType);

		}

		/**
		 * Check whether the working day has the specified problem.
		 * @param problemType Can only take in arguments "understaffed" or "overstaffed"
		 * @return true if staff is over or under the minimum amount of workers.
		 */
		public boolean hasProblems(String problemType) {
			return _shiftStaff.staffProblem(problemType);
		}

		@Override
		public String toString() {
			return _shiftHours.toString();
		}

		@Override
		public int compareTo(Shift compare) {
			//Get start times.
			int thisTime = Integer.parseInt(this.toString().substring(0,4)) + Integer.parseInt(this.toString().substring(4,8));
			int compareTime = Integer.parseInt(compare.toString().substring(0,4)) + Integer.parseInt(compare.toString().substring(4,8));

			if (thisTime == compareTime) {
				return 0;
			} else if (thisTime > compareTime) {
				return 1;
			} else {
				return -1;
			}
		}

	}

}
