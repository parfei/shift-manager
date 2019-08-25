package shiftman.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**This class is where general management of the shop occurs, i.e. the main driver of the system. This includes managing the schedule,
 * the staff list and getting rosters. It also investigates the shop roster for any errors.
 *
 */
public class Shop {

	private String _shopName;
	private WeekSchedule _weekSchedule;
	private ShopStaffList _staffList;

	public Shop(String shopName) {
		_shopName = shopName;
		_weekSchedule = new WeekSchedule();
		_staffList = new ShopStaffList();
	}

	/**
	 * Register staff to the shop.
	 * @param staff the staff member.
	 * @throws RosterException
	 */
	public void registerStaffToShop(Staff staff) throws RosterException {
		_staffList.registerStaff(staff); //register into the staff list.
	}

	/**
	 * Get a specified roster.
	 * @param rosterType the type of roster to get. This can only include string arguments of "roster for day", "roster for worker",
	 * "roster for manager", "week roster summary"
	 * @param rosterSpecific the specific day/worker/manager to get the roster for.
	 * @return arraylist of string of the specified roster type.
	 * @throws RosterException
	 */
	public List<String> getRoster(String rosterType, String rosterSpecific) throws RosterException {
		List<String> rosterOutput = new ArrayList<String>();

		switch (rosterType) {
		case "roster for day":
			rosterOutput.add(_shopName);
			try {
				rosterOutput.addAll(manageWeekSchedule(rosterSpecific).getDayRoster());
			} catch (RosterException exception) {
				if ((exception.getMessage()).equals("no day")) { //if getting a specific day is not possible, catch a specific exception.
					return Collections.emptyList(); //return empty list.
				} else {
					throw new RosterException(exception.getMessage());
				}
			}
			return rosterOutput;
		case "roster for worker":
			return _staffList.getShiftsAssignedBy(rosterSpecific); //get shifts assigned by work name.
		case "roster for manager":
			return _staffList.getShiftsManagedBy(rosterSpecific); //get shifts of a specified manager.
		case "week roster summary":
			rosterOutput.add("SHOP NAME: " + _shopName + " ");
			rosterOutput.add(_weekSchedule.weekSummary()); //add summary of the week schedule.
			return rosterOutput;
		default:
			throw new UnsupportedOperationException("shop.getRoster must take a type string of \"roster for day\", \"roster for worker\" or \"roster for manager\"");
		}
	}

	/**
	 * Get information about all staff (that is, in the staff list). 
	 * @param staffSpecific Takes string arguments of only "registered" or "unassigned". Specifies the specific information to retrieve.
	 * @return arraylist of string with all relevant staff.
	 */
	public List<String> getStaffInfo(String staffSpecific){
		switch (staffSpecific) {
		case "registered":
			return _staffList.displayAllStaff(); //Display all registered staff.
		case "unassigned":
			return _staffList.displayUnassignedStaff(); //Display unassigned.
		default:
			throw new UnsupportedOperationException("Shop.getStaffInfo must take string arguments of either \"registered\" or \"unassigned\"");
		}
	}

	/**
	 * Investigates a specific problem with any current roster. 
	 * @param problemType Can only take string arguments of "without managers", "overstaffed", "understaffed" otherwise
	 * later on an unchecked exception will be thrown.
	 * @return arraylist of string containing all shifts with the specific problem.
	 */
	public List<String> investigateWeekSchedule(String problemType) {
		return _weekSchedule.getProblems(problemType);
	}

	/**
	 * Manage a specific day of the week schedule.
	 * @param dayOfWeek
	 * @return WorkDay the specific day of the week to manage.
	 * @throws RosterException
	 */
	public WorkDay manageWeekSchedule(String dayOfWeek) throws RosterException {
		return _weekSchedule.getWorkDay(dayOfWeek);
	}

	/**
	 * Manage a specific staff member of the staff list.
	 * @param givenName
	 * @param familyName
	 * @return Staff the specific staff member.
	 * @throws RosterException
	 */
	public Staff manageStaffList(String givenName, String familyName) throws RosterException {
		return _staffList.getStaff(givenName + " " + familyName);
	}

	@Override
	public String toString() {
		return _shopName; //return the name of the shop.
	}


}
