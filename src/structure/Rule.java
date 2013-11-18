package structure;

import java.util.ArrayList;
import java.util.Date;

public class Rule {

	public static final String KEY_STARTDATE = "startdate";
	public static final String KEY_ENDDATE = "enddate";
	public static final String KEY_ACTIVE = "active";
	public static final String KEY_WEEKDAYS = "weekdays";

	private ArrayList<Boolean> weekdays;
	private Date startDate;
	private Date finishDate;
	private boolean active;

	public Rule (Date sDate, Date fDate, boolean isOn){
		startDate = sDate;
		finishDate = fDate;
		active = isOn;
	}

	public ArrayList<Boolean> getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(ArrayList<Boolean> weekdays) {
		this.weekdays = weekdays;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
