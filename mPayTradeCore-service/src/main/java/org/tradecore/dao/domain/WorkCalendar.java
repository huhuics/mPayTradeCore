package org.tradecore.dao.domain;

public class WorkCalendar extends BaseDomain {

    private static final long serialVersionUID = -7157559451604505672L;

    private String            dayId;

    private String            workDayType;

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getWorkDayType() {
        return workDayType;
    }

    public void setWorkDayType(String workDayType) {
        this.workDayType = workDayType;
    }

}
