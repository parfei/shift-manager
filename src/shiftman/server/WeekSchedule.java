package shiftman.server;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that holds all the information of every working day. Grants access to manage each work day.
 */
public class WeekSchedule {

	public final List<WorkDay> _daysList = new ArrayList<WorkDay>(); //list of all the days.
	private final String[] _days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

	public WeekSchedule() {
		for (String day : _days) {
			_daysList.add(new WorkDay(day)); //Add the weekdays into the week schedule.
		}
	}

	/**
	 * Get the desired work day to manage.
	 * @param workDay
	 * @return work day to manage.
	 * @throws RosterException
	 */
	public WorkDay getWorkDay(String workDay) throws RosterException{
		int _currentidx = 0;

		for (String day : _days) {
			if (workDay == day) {
				return _daysList.get(_currentidx);
			}
			_currentidx++;
		}
		throw new RosterException("ERROR: " + workDay + " is not a valid day.");
	}

	/**Get problems of all work days in the week schedule.
	 * 
	 * @param problemType arguments can only be "without managers", "overstaffed" or "understaffed"
	 * @return arraylist of string of all relevant shifts that have problems for the entire week.
	 */
	public List<String> getProblems(String problemType) {

		List<String> problems = new ArrayList<String>();
		for (WorkDay workDay : _daysList) { //A day will be empty if it has no problems because it will have no shifts.
			problems.addAll(workDay.problems(problemType));
		}

		return problems;
	}

	/**
	 * Get a summary of the week.
	 * @return a string of the summary of the week.
	 */
	public String weekSummary() {
		String info = "";
		for (WorkDay workDay : _daysList) {
			try {
				info = info + "DAY:" + workDay.getDayRoster().get(0) + " " + workDay.getDayRoster().get(1);
			} catch (RosterException exception){
				info = info + ""; //If there is a day where it is not rostered on, the information for it is skipped.
			}
		}
		return info;
	}

}

