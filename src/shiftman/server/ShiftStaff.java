package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The staff of the specific shift.
 * It handles the rostering of works and managers, problems and the display of the staff.
 */
public class ShiftStaff {
	private List<Staff> _shiftWorkers = new ArrayList<Staff>();
	private Staff _manager;
	private int _minWorkers;

	ShiftStaff(int minWorkers){
		_minWorkers = minWorkers;
	}

	/**
	 * Assigns the worker to the shift, adds into list.
	 * @param shift
	 * @param staff
	 * @throws RosterException
	 */
	public void addWorker(WorkDay.Shift shift, Staff staff) throws RosterException {

		checkAlreadyAssignedWith(staff); //check whether the staff has already been rostered on for this shift.

		_shiftWorkers.add(staff);
		Collections.sort(_shiftWorkers);
		staff.updateShifts(shift);

	}

	/**
	 * Assigns the manager to the shift, adds into list.
	 * @param shift
	 * @param staff
	 * @throws RosterException
	 */
	public void addManager(WorkDay.Shift shift, Staff staff) throws RosterException {

		checkAlreadyAssignedWith(staff);

		if (_manager == null) { //If manager is null, there is no manager assigned. Otherwise, there is already a manager.
			_manager = staff;
			staff.updateManaging(shift);
		} else {
			throw new RosterException("ERROR: this shift already has " + _manager.getName("firstlast") + " managing.");
		}
	}

	/**
	 * Private method to check whether the staff that is about to be assigned is already assigned in the shift.
	 * @param staff
	 * @throws RosterException
	 */
	private void checkAlreadyAssignedWith(Staff staff) throws RosterException {

		for (Staff staffCompare : _shiftWorkers) {
			if (staffCompare.getName("firstlast").equals(staff.getName("firstlast"))) {
				throw new RosterException("ERROR: this shift already has " + staff.getName("firstlast") + " assigned as a worker.");
			}
		}

		if (_manager != null) { //If manager is null, then there is no need to check as there is no manager for this shift at that time.
			if (_manager.getName("firstlast").equals(staff.getName("firstlast"))) {
				throw new RosterException("ERROR: this shift already has " + staff.getName("firstlast") + " assigned as a manager.");
			}
		}
	}

	/**
	 * Display the specified staff of the shift.
	 * @param staffType can only take arguments "manager" or "workers". 
	 * @return string of the specified staff of the shift.
	 */
	public String displayShiftStaff(String staffType) {
		switch (staffType) {
		case "manager":
			if (_manager == null) {
				return "[No manager assigned]";
			}
			//If there is a manager, return the manager name.
			return " Manager:" + _manager.getName("last") + ", " + _manager.getName("first");
		case "workers":
			if (_shiftWorkers.isEmpty()) {
				return "[No workers assigned]";
			}
			//If there are workers assigned, iterate through each worker and add it to the output.
			String workerList = "";
			int current = 0;
			for (Staff staff : _shiftWorkers) {
				if (current != _shiftWorkers.size() - 1) {
					workerList = workerList + staff.getName("firstlast") + ", ";
				} else {
					workerList = workerList + staff.getName("firstlast"); //Don't add a comma at the end if it is end of the workers list.
				}
				current++;
			}
			return "[" + workerList + "]";
		default:
			throw new UnsupportedOperationException("WorkDay.Shift.getShiftStaff can only accept arguments \"manager\" or \"workers\"");
		}
	}

	/**
	 * Retrieve all the problems of the staff in the current shift. Checks against the shift's specified minimum workers.
	 * @param problemType can only take arguments of "overstaffed" or "understaffed"
	 * @return
	 */
	public boolean staffProblem(String problemType) {
		switch (problemType) {
		case "overstaffed":
			if (_shiftWorkers.size() > _minWorkers) {
				return true;
			}
			break;
		case "understaffed":
			if (_shiftWorkers.size() < _minWorkers) {
				return true;
			}
			break;
		default:
			throw new UnsupportedOperationException("WorkDay.Shift.shiftHasProblems can only accept arguments \"overstaffed\" or \"understaffed\"");
		}
		return false;
	}

}
