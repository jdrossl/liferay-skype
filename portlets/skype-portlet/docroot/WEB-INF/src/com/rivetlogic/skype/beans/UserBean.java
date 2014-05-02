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

package com.rivetlogic.skype.beans;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;
import com.rivetlogic.skype.util.Constants;


/**
 * @author christopherjimenez
 *
 */
public class UserBean {
	
	private Long userId;
	private String firstName;
	private String lastName;
	private String primaryPhone;
	private String skypeScreenName;
	
	public UserBean(){
		this.userId = Constants.DEFAULT_ELEMENT_ID;
		this.firstName = StringPool.BLANK;
		this.lastName = StringPool.BLANK;
		this.primaryPhone= StringPool.BLANK;
		this.skypeScreenName = StringPool.BLANK;
	}

	public JSONObject toJSON(){
		JSONObject document = JSONFactoryUtil.createJSONObject();
		document.put(Constants.USER_ID, userId);
		document.put(Constants.FIRST_NAME, firstName);
		document.put(Constants.LAST_NAME, lastName);
		document.put(Constants.PRIMARY_PHONE, primaryPhone);
		document.put(Constants.SKYPE_SCREEN_NAME, skypeScreenName);
		return document;
	}
	
	public Long getUserId(){
		return userId;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPrimaryPhone() {
		return primaryPhone;
	}

	public String getSkypeScreenName() {
		return skypeScreenName;
	}

	public UserBean setUserId(Long userId){
		this.userId = userId;
		return this;
	}
	
	public UserBean setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public UserBean setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public UserBean setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
		return this;
	}

	public UserBean setSkypeScreenName(String skypeScreenName) {
		this.skypeScreenName = skypeScreenName;
		return this;
	}
	
}
