/**
 * Copyright (C) 2005-2014 Rivet Logic Corporation.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package com.rivetlogic.skype.util;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.comparator.UserFirstNameComparator;
import com.liferay.portal.util.comparator.UserLastNameComparator;
import com.rivetlogic.skype.beans.UserBean;
import com.rivetlogic.skype.model.SkypeGroup;
import com.rivetlogic.skype.service.SkypeGroupLocalServiceUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author christopherjimenez
 *
 */
public class SkypeUtil {
	private static final Log LOG = LogFactoryUtil.getLog(SkypeUtil.class);
	
	private static UserBean parseUser(User user){
		UserBean userBean = new UserBean();
		userBean.setUserId(user.getUserId())
			.setFirstName(user.getFirstName())
			.setLastName(user.getLastName())
			.setPrimaryPhone(getPrimaryPhone(user))
			.setSkypeScreenName(getSkypeScreenName(user));
		return userBean;
	}
	
	public static UserBean getUserBean(Long userId){
		UserBean userBean = null;
		User user = null;
		try{
			user = UserLocalServiceUtil.getUser(userId);
			userBean = parseUser(user);
		}catch (Exception e) {
			LOG.error(e);
		}
		return userBean;
	}
	
	public static List<UserBean> getUserBeans(Long companyId, int start, int end, OrderByComparator obc){
		List<UserBean> results = new ArrayList<UserBean>();
		List<User> users = null;
		UserBean userBean = null;
		try{
			users = UserLocalServiceUtil.search(companyId, null, WorkflowConstants.STATUS_APPROVED, null, start, end, obc);
			for(User user : users){
				userBean = parseUser(user);
				results.add(userBean);
			}
		}catch (Exception e) {
			LOG.error(e);
		}
		
		return results;
	}
	
	public static int getUserBeansCount(Long companyId){
		int count = Constants.DEFAULT_INT_VALUE;
		try{
			count = UserLocalServiceUtil.searchCount(companyId, null, WorkflowConstants.STATUS_APPROVED, null);
		}catch(Exception e){
			LOG.error(e);
		}
		return count;
	}
	
	public static List<SkypeGroup> getSkypeGroups(Long userId, int start, int end, OrderByComparator obc){
		List<SkypeGroup> groups = new ArrayList<SkypeGroup>();
		try{
			groups = SkypeGroupLocalServiceUtil.getSkypeGroupByUserId(userId, start, end, obc);
		}catch (Exception e) {
			LOG.error(e);
		}
		return groups;
	}
	
	public static int getSkypeGroupsCount(Long userId){
		int count = Constants.DEFAULT_INT_VALUE;
		try{
			count = SkypeGroupLocalServiceUtil.getSkypeGroupByUserIdCount(userId);
		}catch (Exception e) {
			LOG.error(e);
		}
		return count;
	}
	
	private static String getPrimaryPhone(User user){
		String primaryPhone = StringPool.BLANK;
		try{
			List<Phone> phones = user.getPhones();
			for(Phone phone : phones){
				if(phone.isPrimary()){
					primaryPhone = phone.getNumber();
				}
			}
		}catch (Exception e) {
			LOG.debug(e);
		}
		return primaryPhone;
	}
	
	private static String getSkypeScreenName(User user){
		String skypeSN = StringPool.BLANK;
		try{
			Contact contact = user.getContact();
			if(contact.getSkypeSn() != null){
				skypeSN = contact.getSkypeSn();
			}
		}catch (Exception e) {
			LOG.debug(e);
		}
		return skypeSN;
	}
	
	public static OrderByComparator getSkypeComparator(String orderByCol, boolean asc){
		OrderByComparator obc = null;
		
		if(Constants.FIRST_NAME.equalsIgnoreCase(orderByCol)){
			obc = new UserFirstNameComparator(asc);
		}else if(Constants.LAST_NAME.equalsIgnoreCase(orderByCol)){
			obc = new UserLastNameComparator(asc);
		}
		return obc;
	}
	
	public List<SkypeGroup> getSkypeGroupByUserId(Long userId, int start, int end, OrderByComparator obc){
		List<SkypeGroup> groups = new ArrayList<SkypeGroup>();
		try{
			groups = SkypeGroupLocalServiceUtil.getSkypeGroupByUserId(userId, start, end, obc);
		}catch (Exception e) {
			LOG.error("Retrieving user skype groups: ", e);
		}
		return groups;
	}
	
	public static void returnJSONObject(PortletResponse response, JSONObject jsonObj) {
        HttpServletResponse servletResponse = PortalUtil.getHttpServletResponse(response);
        servletResponse.setHeader(HttpHeaders.CACHE_CONTROL, HttpHeaders.CACHE_CONTROL_NO_CACHE_VALUE);
		servletResponse.setHeader(HttpHeaders.PRAGMA, HttpHeaders.PRAGMA_NO_CACHE_VALUE);
		servletResponse.setHeader(HttpHeaders.EXPIRES, String.valueOf(Constants.UNDEFINED_ID));
        PrintWriter pw;
        try {
            pw = servletResponse.getWriter();
            pw.write(jsonObj.toString());
            pw.close();
        } catch (IOException e) {
            LOG.error("Error while returning json", e);
        }
    }

}
