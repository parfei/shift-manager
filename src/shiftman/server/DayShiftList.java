package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shift list of the working day.
 * Gets a shift to manage. Roster in the shift into the list. Get shift details.
 */
public class DayShiftList {

	private List<WorkDay.Shift> _shiftList;

	public DayShiftList() {
		_shiftList = new ArrayList<WorkDay.Shift>();
	}

	/**
	 * Roster in the shift and add it to the working day list.
	 * @param shift
	 * @throws RosterException
	 */
	public void rosterInShift(WorkDay.Shift shift) throws RosterException {
		//Get int values.
		int start = Integer.parseInt((shift.toString()).substring(0,4));
		int end = Integer.parseInt((shift.toString()).substring(4,8));

		for (WorkDay.Shift shiftCompare : _shiftList) {
			int compStart = Integer.parseInt((shiftCompare.toString()).substring(0,4));
			int compEnd = Integer.parseInt((shiftCompare.toString()).substring(4,8));

			//Checks if it overlaps with other shifts.
			if ((start <= compStart && end >= compStart) ||
					(start >= compStart && end <= compEnd) ||
					(start >= compStart && start <= compEnd) ||
					(start <= compStart && end >= compEnd)) {
				throw new RosterException("ERROR: cannot roster in shift, overlaps with " + (shiftCompare.toString()).substring(0,2)
						+ ":" + shiftCompare.toString().substring(2,4) + " shift.");
			}
		}
		_shiftList.add(shift);
		Collections.sort(_shiftList);
	}

	/**
	 * Get the specified shift to manage with.
	 * @param startTime
	 * @param endTime
	 * @return shift to manage.
	 * @throws RosterException
	 */
	public WorkDay.Shift getShift(String startTime, String endTime) throws RosterException {
		Hours comparison = new ShiftHours(startTime, endTime);
		for (WorkDay.Shift shift : _shiftList) {
			if ((comparison.toString()).equals(shift.toString())) {
				return shift;
			}
		}

		throw new RosterException("ERROR: " + startTime + " shift has not been specified.");

	}

	/**
	 * Get the details of all shifts of the working day.
	 * @return arraylist of string of all the shift details.
	 */
	public List<String> getAllShiftDetails() {
		List<String> allShifts = new ArrayList<String>();
		for (WorkDay.Shift shift : _shiftList) {
			allShifts.add(shift.getShiftDetails() + " " + shift.getShiftStaff("manager") + " " + shift.getShiftStaff("workers"));
		}
		return allShifts;
	}

	/**
	 * Get the details of all the problems of all the shifts of the working day.
	 * @param problemType can only take arguments of "without managers", "overstaffed" or "understaffed"
	 * @return arraylist of string of all the days.
	 */
	public List<String> getAllShiftsWithProblems(String problemType){

		List<String> problems = new ArrayList<String>();

		for (WorkDay.Shift shift : _shiftList) { //Iterates through all shifts of the day. If there are no shifts, empty list is returned.
			switch (problemType) {
			case "without managers":
				if (shift.getShiftStaff("manager").equals("[No manager assigned]")) {
					problems.add(shift.getShiftDetails());
				}
				break;
			case "overstaffed":
				if (shift.hasProblems("overstaffed")) {
					problems.add(shift.getShiftDetails());
				}
				break;
			case "understaffed":
				if (shift.hasProblems("understaffed")) {
					problems.add(shift.getShiftDetails());
				}
				break;
			default:
				throw new UnsupportedOperationException("ShiftList.getAllShiftsWithProblems can only accept arguments \"without managers\" or \"overstaffed\" or \"understaffed\"");
			}
		}

		return problems;
	}

}
