package org.tradecore.dao;

import org.tradecore.dao.domain.WorkCalendar;


public interface WorkCalendarDAO {

	/**
	 * 
	 * 方法描述 :获取工作日
	 * @param dayId
	 * @return WorkCalendar
	 */
	WorkCalendar get(String dayId);


}
