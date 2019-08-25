package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The staff and all its information to do with the staff inside it.
 * Describes the shifts of the staff and any shifts they are managing.
 */
public class Staff implements Comparable<Staff> {

	private String _lastName, _firstName;
	private final List<WorkDay.Shift> _shiftsAssigned = new ArrayList<WorkDay.Shift>(); //shifts assigned to the staff member.
	private final List<WorkDay.Shift> _shiftsManaging = new ArrayList<WorkDay.Shift>(); //shifts managed by the staff member.

	public Staff(String firstName, String lastName) {
		_lastName = lastName;
		_firstName = firstName;
	}

	/**
	 * Add in a shift that the staff is working for.
	 * @param shift
	 */
	public void updateShifts(WorkDay.Shift shift) {
		_shiftsAssigned.add(shift);
		Collections.sort(_shiftsAssigned);
	}

	/**
	 * Add in a shift that the staff is managing for.
	 * @param shift
	 */
	public void updateManaging(WorkDay.Shift shift) {
		_shiftsManaging.add(shift);
		Collections.sort(_shiftsManaging);
	}

	/**
	 * Get a list of all shifts the staff is managing for.
	 * @return
	 */
	public List<String> getManagingShifts(){
		if (_shiftsManaging.isEmpty()) {
			return Collections.emptyList(); //no relevant shifts will return an empty list.
		}

		List<String> output = new ArrayList<String>();
		output.add(this.getName("last") + ", " + this.getName("first"));
		for (WorkDay.Shift shift : _shiftsManaging) { //iterate through shifts managing.
			output.add(shift.getShiftDetails());
		}
		return output;
	}

	/**
	 * Get a list of all shifts the staff is working for.
	 * @return arraylist of all shifts the staff is assigned to.
	 */
	public List<String> getShiftList(){
		if (_shiftsAssigned.isEmpty()) {
			return Collections.emptyList();
		}

		List<String> output = new ArrayList<String>();
		output.add(this.getName("last") + ", " + this.getName("first"));
		for (WorkDay.Shift shift: _shiftsAssigned) { //iterate through shifts assigned.
			output.add(shift.getShiftDetails());
		}
		return output;
	}

	/**
	 * Get the name of the staff member.
	 * @param nameType the specific name of the staff member.
	 * @return the name of the staff member.
	 */
	public String getName(String nameType) {
		switch (nameType) {
		case "firstlast":
			return _firstName + " " + _lastName;
		case "lastfirst":
			return _lastName + " " + _firstName;
		case "first":
			return _firstName;
		case "last":
			return _lastName;
		default:
			throw new UnsupportedOperationException("Staff.getName supports only strings of \"firstlast\", \"lastfirst\", \"first\" and \"last\"");
		}
	}

	@Override
	public int compareTo(Staff compare) {

		int comparison = (this.getName("lastfirst")).compareTo(compare.getName("lastfirst")); //name of the staff.
		if (comparison == 0) {
			return 0;
		} else if (comparison > 0) {
			return 1;
		} else {
			return -1;
		}
	}

}
