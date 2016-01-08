/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2014. 8. 20.		First Draft.
 */
package com.athena.meerkat.common.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class XMLGregorialCalendarUtil {
	
	public static final String DEFAULT_DATE_PATTERN = "yyyy.MM.dd HH:mm:ss";
	
    /**
     * <pre>
     * 주어진 날짜를 주어진 포맷을 이용하여 변환 후 리턴한다.
     * 만약, pattern이 주어지지 않으면 기본 변경 포맷인 'yyyyMMddHHmmss'를 사용한다.
     * </pre>
     * 
     * @param date 변환할 Date 객체
     * @return 변환된 날짜
     */
    public static String getFormattedDate(String pattern, Date date) {
        if (date == null) {
            return null;
        }
        
        if(StringUtils.isEmpty(pattern)) {
            pattern = DEFAULT_DATE_PATTERN;
        }
        
        return new SimpleDateFormat(pattern, Locale.KOREA).format(date);
    }// end of getFormattedDate()

    
    /**
     * <pre>
     * XMLGregorialCalendar 형태의 GMT 스트링에 대해 시간대를 GMT+09:00로 변경하고 DEFAULT_DATE_PATTERN의 포맷화된 문자열로 변경한다.
     * </pre>
     * @param gmtString
     * @return
     */
    public static String convertXmlGregorianCalendarToFormattedString(String gmtString) {
        Assert.notNull(gmtString, "gmtString must not be null.");
        
        Calendar calendar = null;
        
        try {
            calendar = gmtStringToGregorianCalendar(gmtString);
        } catch (DatatypeConfigurationException e) {
            throw new IllegalArgumentException("주어진 날짜를 변환할 수 없습니다. : [GMT : " + gmtString + "]");
        }
        
        return getFormattedDate(DEFAULT_DATE_PATTERN, calendar.getTime());
    }//end of convertXmlGregorianCalendarToFormattedString()
    
    /**
     * <pre>
     * YYYYMMDDHH24MISS의 포맷화된 문자열에 대해 시간대를 XMLGregorialCalendar 형태의 GMT(GMT+09:00) 스트링으로 변경한다.
     * </pre>
     * @param dateString
     * @return
     */
    public static String convertFormattedStringToXmlGregorianCalendarStr(String dateString) {
        Assert.notNull(dateString, "dateString must not be null.");
        
        if(dateString.length() < 14) {
            dateString = StringUtils.rightPad(dateString, 14, '0');
        }
        
        Assert.isTrue(dateString.length() == 14, "dateString's length must be 14.");
        
        GregorianCalendar cal = null;
        XMLGregorianCalendar calender = null;
        try {
            cal = convertTimezone(getDate(dateString), TimeZone.getTimeZone("ROK"));
            calender = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            
            return calender.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("주어진 날짜를 변환할 수 없습니다. : [Date : " + dateString + " ]");
        }
    }//end of convertFormattedStringToXmlGregorianCalendar()
    
    /**
     * <pre>
     * YYYYMMDDHH24MISS의 포맷화된 문자열에 대해 시간대를XMLGregorialCalendar 형태로 변경한다.
     * </pre>
     * @param dateString
     * @return
     */
    public static XMLGregorianCalendar convertFormattedStringToXmlGregorianCalendar(String dateString) {
        Assert.notNull(dateString, "dateString must not be null.");
        
        GregorianCalendar cal = null;
        XMLGregorianCalendar calender = null;
        try {
            cal = convertTimezone(getDate(dateString), TimeZone.getTimeZone("ROK"));
            calender = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {
            throw new IllegalArgumentException("주어진 날짜를 변환할 수 없습니다. : [Date : " + dateString + " ]");
        }
        
        return calender;
    }//end of convertFormattedStringToXmlGregorianCalendar()

    /**
     * <pre>
     * YYYY-MM-DDTHH:MI:SS.SSS+09:00 형태의 문자열(예:2007-02-13T10:25:05.986+07:00)로 한국 표준시의 GregorianCalendar 생성한다.
     * </pre>
     * 
     * @param stringTypeDate YYYY-MM-DDTHH:MI:SS.SSS+09:00 형태의 문자열(예:2007-02-13T10:25:05.986+07:00)
     * @return 한국 표준시의 GregorianCalendar
     */
    private static GregorianCalendar gmtStringToGregorianCalendar(String stringTypeDate) throws DatatypeConfigurationException {
        String yyyy = stringTypeDate.substring(0, 4);
        String mm = stringTypeDate.substring(5, 7);
        String dd = stringTypeDate.substring(8, 10);
        
        String hh = "00";
        String mi = "00";
        String ss = "00";
        String ms = "00";
        String tz = null;
        
        if(stringTypeDate.length() > 23) {
            hh = stringTypeDate.substring(11, 13);
            mi = stringTypeDate.substring(14, 16);
            ss = stringTypeDate.substring(17, 19);
            ms = stringTypeDate.substring(20, 23);
            tz = stringTypeDate.substring(23);
        } else {
            tz = stringTypeDate.substring(10);
            //tz = "+09:00";
        }
        
        if(tz.equals("Z")) {
            tz = "UTC";
        } else {
            tz = "GMT" + tz;
        }

        int iyyyy = Integer.parseInt(yyyy);
        int imm = Integer.parseInt(mm) - 1;
        int idd = Integer.parseInt(dd);
        int ihh = Integer.parseInt(hh);
        int imi = Integer.parseInt(mi);
        int iss = Integer.parseInt(ss);
        int ims = Integer.parseInt(ms);
        
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(tz));
        c.set(iyyyy, imm, idd, ihh, imi, iss);
        c.set(Calendar.MILLISECOND, ims);
        
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(c.getTime());
        cal.setTimeZone(TimeZone.getTimeZone("ROK"));
        
        return cal;
    }//end of gmtStringToGregorianCalendar()

    /**
     * <pre>
     * 주어진 날짜를 기본 포맷인 'yyyyMMddHHmmss'를 Date 타입으로 변경하여 리턴한다.
     * </pre>
     * 
     * @param date 변환할 날짜 문자열
     * @return 변환된 Date 객체
     */
    private static Date getDate(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        
        int yyyy = Integer.parseInt(date.substring(0, 4));
        int mm = Integer.parseInt(date.substring(4, 6));
        int dd = Integer.parseInt(date.substring(6, 8));        
        int hh = Integer.parseInt(date.substring(8, 10));
        int mi = Integer.parseInt(date.substring(10, 12));
        int ss = Integer.parseInt(date.substring(12, 14));
        
        if(yyyy <= 1900 || yyyy >= 2999) {
            throw new ParseException("Invalid year.", yyyy);
        }
        if(mm < 1 || mm > 12) {
            throw new ParseException("Invalid month.", mm);
        }
        if(dd < 1 || dd > 31) {
            throw new ParseException("Invalid Day.", dd);
        }
        if(hh < 0 || hh > 23) {
            throw new ParseException("Invalid hour.", hh);
        }
        if(mi < 0 || mi > 59) {
            throw new ParseException("Invalid minute.", mi);
        }
        if(ss < 0 || ss > 59) {
            throw new ParseException("Invalid second.", ss);
        }
        
        return new SimpleDateFormat(DEFAULT_DATE_PATTERN, Locale.KOREA).parse(date);
    }//end of getDate()
    
    /**
     * <pre>
     * 날짜의 시간대를 GMT+09:00에서 UTC로 변환하여 리턴한다.
     * </pre>
     * @param date 날짜
     * @param tz 시간대
     * @return 변환된 날짜
     * @throws DatatypeConfigurationException 
     */
    private static GregorianCalendar convertTimezone(Date date, TimeZone tz) throws DatatypeConfigurationException {  
        Assert.notNull(date, "date must not be null.");  
        Assert.notNull(tz, "tz must not be null.");

        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("ROK"));
        cal.setTime(date);        
        cal.setTimeZone(tz);
        return cal;
    }//end of convertTimezone()
}
// end of XMLGregorialCalendarUtil.java