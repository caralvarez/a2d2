// Copyright 2018-2020 Elimu Informatics
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.elimu.a2d2.genericmodel;

public class ClientRequest extends ServiceRequest{

	private static final long serialVersionUID = 1L;

	String authToken;

	String url;

	String basicUserName;

	String basicPassword;


	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * @param authToken the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the basicUserName
	 */
	public String getBasicUserName() {
		return basicUserName;
	}

	/**
	 * @param basicUserName the basicUserName to set
	 */
	public void setBasicUserName(String basicUserName) {
		this.basicUserName = basicUserName;
	}

	/**
	 * @return the basicPassword
	 */
	public String getBasicPassword() {
		return basicPassword;
	}

	/**
	 * @param basicPassword the basicPassword to set
	 */
	public void setBasicPassword(String basicPassword) {
		this.basicPassword = basicPassword;
	}


}
