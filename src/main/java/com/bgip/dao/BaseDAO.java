package com.bgip.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bgip.model.user.BaseBean;
import com.bgip.mongo.MongoManager;

@Component
public class BaseDAO {
	@Autowired
	MongoManager mongoManager;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BaseDAO.class);
	
	protected Object insertDB(String collectionName, BaseBean baseBean) throws Exception {
		if (baseBean.getCreated() == null) {
			baseBean.setCreated(getCurrentDate(baseBean.getTimeZone()));
		}
		return mongoManager.insert(collectionName, baseBean);
	}
	
	

	private Date getCurrentDate(String timeZone) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
		simpleFormat.setTimeZone(StringUtils.isNotEmpty(timeZone)?TimeZone.getTimeZone(timeZone):TimeZone.getTimeZone("EST"));
		try {
			return simpleFormat.parse(simpleFormat.format(new Date()));
		} catch (ParseException e) {
			LOGGER.error("ParseException while parsing date", e);
			return null;
		}
	}
	
	protected String calculateTimeDiff(Date date) {
		String diffDateStr = "";
		LOGGER.info("Date:"+date);
		if (date != null) {
			Long diff = new Date().getTime() - date.getTime();
			LOGGER.info("diff:"+diff);
			Long minutes = 0L;
			Long hrs = 0L;
			Long days = 0L;

			days = TimeUnit.MILLISECONDS.toDays(diff);
			hrs = TimeUnit.MILLISECONDS.toHours(diff);
			minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

			if (days > 2) {
				Date curdate = new Date(); // your date
				Calendar cal = Calendar.getInstance();
				cal.setTime(curdate);
				int curYear = cal.get(Calendar.YEAR);
				cal.setTime(date);
				SimpleDateFormat simpleFormat;
				if (curYear < cal.get(Calendar.YEAR)) {
					simpleFormat = new SimpleDateFormat(
							"MMMM dd, yyyy HH:mm");
				}else{
					simpleFormat = new SimpleDateFormat(
							"MMMM dd,  HH:mm");
				}
				diffDateStr = simpleFormat.format(date);
			}  else if (hrs >= 24 && hrs <= 48) {
				diffDateStr = "Yesterday";				
			} else if (hrs >= 1) {
				diffDateStr = hrs.toString() + " Hrs";
			} else if (minutes > 1) {
				diffDateStr = minutes.toString() + " Minutes";
			} else {
				diffDateStr = " Just Now";
			}
		}
		return diffDateStr;
	}
}