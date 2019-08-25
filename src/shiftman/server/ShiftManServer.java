package shiftman.server;

import java.util.ArrayList;
import java.util.List;

/**This class implements the ShiftMan interface.
 */
public class ShiftManServer implements ShiftMan { //check if emtpy string list returned

	private Shop _shop;

	@Override
	public String newRoster(String shopName) {
		try {
			checkValidInput(new String[]{shopName}, new String[]{"shop name"}); //Check if inputs are empty or null.
			_shop = new Shop(shopName);
		} catch (RosterException exception) {
			return exception.getMessage() + " New shop roster was not constructed.";
		}
		return "";
	}

	@Override
	public String setWorkingHours(String dayOfWeek, String startTime, String endTime) {
		try {
			checkValidInput(new String[] {dayOfWeek, startTime, endTime}, new String[] {"day", "working hours start time", "working hours end time"});
			checkRosterIsNull(); //Check if the private variable _shop is null; if null, no roster has been created yet.

			_shop.manageWeekSchedule(dayOfWeek).setHours(startTime, endTime);
		} catch (RosterException exception){
			return exception.getMessage();
		}

		return "";
	}

	@Override
	public String addShift(String dayOfWeek, String startTime, String endTime, String minimumWorkers) {
		try {
			checkValidInput(new String[] {dayOfWeek, startTime, endTime, minimumWorkers}, new String[] {"day", "shift start time", "shift end time", "minimum worker value"});
			checkRosterIsNull();

			WorkDay.Shift shift = _shop.manageWeekSchedule(dayOfWeek).new Shift(startTime, endTime, minimumWorkers); //create new shift in the specified day.
			_shop.manageWeekSchedule(dayOfWeek).addShift(shift); //schedule in shift.
		} catch (RosterException exception) {
			return exception.getMessage();
		}
		return "";
	}

	@Override
	public String registerStaff(String givenname, String familyName) {
		try {
			checkValidInput(new String[] {givenname, familyName}, new String[] {"first name", "last name"});
			checkRosterIsNull();

			Staff staff = new Staff(givenname, familyName); //create staff.
			_shop.registerStaffToShop(staff); //register staff.
		} catch (RosterException exception) {
			return exception.getMessage();
		}
		return "";
	}

	@Override
	public String assignStaff(String dayOfWeek, String startTime, String endTime, String givenName, String familyName,
			boolean isManager) {
		Staff staff;
		try {
			checkValidInput(new String[] {dayOfWeek, startTime, endTime, givenName, familyName}, new String[]{"day", "shift start time", "shift end time", "first name", "last name"});
			checkRosterIsNull();

			staff = _shop.manageStaffList(givenName, familyName); //retrieve specific staff to assign.
			_shop.manageWeekSchedule(dayOfWeek).assignStaff(staff, startTime, endTime, isManager);
		} catch (RosterException exception) {
			return exception.getMessage();
		}
		return "";
	}

	@Override
	public List<String> getRegisteredStaff() {
		try {
			checkRosterIsNull();
			return _shop.getStaffInfo("registered"); //get all registered staff information.
		} catch (RosterException exception) {
			return createExceptionList(exception.getMessage());
		}
	}

	@Override
	public List<String> getUnassignedStaff() {
		try {
			checkRosterIsNull();
			return _shop.getStaffInfo("unassigned");
		} catch (RosterException exception) {
			return createExceptionList(exception.getMessage());
		}
	}

	@Override
	public List<String> shiftsWithoutManagers() {
		try {
			checkRosterIsNull();
			return _shop.investigateWeekSchedule("without managers"); //investigate the week schedule and retrieve problems.
		} catch (RosterException exception) {
			return createExceptionList(exception.getMessage());
		}
	}

	@Override
	public List<String> understaffedShifts() {
		try {
			checkRosterIsNull();
			return _shop.investigateWeekSchedule("understaffed");
		} catch (RosterException exception) {
			return createExceptionList(exception.getMessage());
		}
	}

	@Override
	public List<String> overstaffedShifts() {
		try {
			checkRosterIsNull();
			return _shop.investigateWeekSchedule("overstaffed");
		} catch (RosterException exception) {
			return createExceptionList(exception.getMessage());
		}
	}

	@Override
	public List<String> getRosterForDay(String dayOfWeek) {
		try {
			checkValidInput(new String[] {dayOfWeek}, new String[] {"day"});
			checkRosterIsNull();

			return _shop.getRoster("roster for day", dayOfWeek); //retrieve roster for the specific day.
		} catch (RosterException exception) {
			return createExceptionList(exception.getMessage());
		}

	}

	@Override
	public List<String> getRosterForWorker(String workerName) {
		try {
			checkValidInput(new String[] {workerName}, new String[] {"worker name"});
			checkRosterIsNull();

			return _shop.getRoster("roster for worker", workerName);
		} catch (RosterException exception) {
			return createExceptionList(exception.getMessage());
		}
	}

	@Override
	public List<String> getShiftsManagedBy(String managerName) {
		try {
			checkValidInput(new String[] {managerName}, new String[] {"manager name"});
			checkRosterIsNull();

			return _shop.getRoster("roster for manager", managerName);
		} catch (RosterException exception) {
			return createExceptionList(exception.getMessage());
		}
	}

	@Override
	public String reportRosterIssues() {
		try {
			checkRosterIsNull();
			int overstaffed = overstaffedShifts().size(); //Retrieve all roster shift problems.
			int understaffed = understaffedShifts().size();
			int withoutManagers = shiftsWithoutManagers().size();
			return "There are " + overstaffed + " overstaffed shifts, " + understaffed + " understaffed shifts, " + withoutManagers + " shifts without managers.";
		} catch (RosterException exception) {
			return exception.getMessage();
		}
	}

	@Override
	public String displayRoster() {
		try {
			checkRosterIsNull();
			return (_shop.getRoster("week roster summary", "week")).get(0)
					+ (_shop.getRoster("week roster summary", "week")).get(1); //return summary formatted as a single string.
		} catch (RosterException exception) {
			return exception.getMessage();
		}
	}

	/**
	 * Checks if certain inputs are valid. Throws an exception if not.
	 * @param check String inputs to be checked if null or empty.
	 * @param inputType Each string input's original parameter name, in respective to the check input.
	 * @throws RosterException
	 */
	private void checkValidInput(String[] check, String[] inputType) throws RosterException {
		int currentType = 0;
		for (String string : check) {
			if (string == null || string.isEmpty()) {
				throw new RosterException("ERROR: " + inputType[currentType] + " given is not valid.");
			}
			currentType++;
		}
	}

	/**Checks if the roster is null (_shop variable).
	 * @throws RosterException
	 * @author Jennifer Lowe
	 */
	private void checkRosterIsNull() throws RosterException {
		if (_shop == null) {
			throw new RosterException("ERROR: no roster has been created");
		}
	}

	/**Creates an array list of string and place the input exception inside as the first element.
	 * @param exception the exception to be placed in a list.
	 * @return array list of string with the exception.
	 */
	private List<String> createExceptionList(String exception){
		List<String> exceptionList = new ArrayList<String>();
		exceptionList.add(exception);
		return exceptionList;
	}

}
