package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Staff lift for the shop. General management for the staff list.
 */
public class ShopStaffList {
	private List<Staff> _staffList = new ArrayList<Staff>();

	/**
	 * Register staff into the shop staff list.
	 * @param staff staff to be registered.
	 * @throws RosterException
	 */
	public void registerStaff(Staff staff) throws RosterException {
		for (Staff staffCompare : _staffList) {
			//checks for already registered staff member. Names are case insensitive.
			if ((staff.getName("firstlast")).equalsIgnoreCase(staffCompare.getName("firstlast"))) {
				throw new RosterException("ERROR: " + staff.getName("firstlast") + "already registered.");
			}
		}
		_staffList.add(staff);
		Collections.sort(_staffList); //sort staff list by family name.
	}

	/**
	 * Get staff to manage.
	 * @param staffName specific staff to get for.
	 * @return staff to manage.
	 * @throws RosterException
	 */
	public Staff getStaff(String staffName) throws RosterException {
		int current = 0;

		for (Staff staffCompare : _staffList) {
			if (staffName.equals(staffCompare.getName("firstlast"))) {
				return _staffList.get(current);
			}
			current++;
		}

		throw new RosterException("ERROR: cannot assign " + staffName + ", because they have not been registered into the roster yet.");
	}

	/**
	 * Get shifts assigned by the specified staff. It will then get the relevant staff to retrieve the information from.
	 * @param staffName
	 * @return arraylist of string of all the relevant shifts of the staff.
	 * @throws RosterException
	 */
	public List<String> getShiftsAssignedBy(String staffName) throws RosterException {

		Staff staff = getStaff(staffName); //get the staff to retrieve the information from.

		return staff.getShiftList();
	}

	/**
	 * Get shifts managed by the specified staff. IT will then get relevant staff to retrieve the information from.
	 * @param staffName
	 * @return arraylist of string of all the releveant shifts of the staff.
	 * @throws RosterException
	 */
	public List<String> getShiftsManagedBy(String staffName) throws RosterException {

		Staff staff = getStaff(staffName);

		return staff.getManagingShifts();
	}

	/**
	 * Get all the staff that are unassigned in the shop staff list.
	 * @return arraylist of string of all the staff that are unassigned.
	 */
	public List<String> displayUnassignedStaff() {
		List<String> output = new ArrayList<String>();

		for (Staff staff : _staffList) {
			//If the staff is neither assigned shifts nor assigned to shifts to manage, add to the list.
			if (staff.getShiftList().isEmpty() && staff.getManagingShifts().isEmpty()) {
				output.add(staff.getName("firstlast"));
			}
		}

		return output;
	}

	/**
	 * Display all registered staff in the shop staff list.
	 * @return arraylist of string of all the shop staff members.
	 */
	public List<String> displayAllStaff() {

		List<String> output = new ArrayList<String>();

		for (Staff staff : _staffList) {
			output.add(staff.getName("firstlast")); //Add to list the staff name.
		}

		return output;
	}

}
