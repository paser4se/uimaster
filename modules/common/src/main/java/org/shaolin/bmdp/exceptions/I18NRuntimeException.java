/*
* Copyright 2015 The UIMaster Project
*
* The UIMaster Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package org.shaolin.bmdp.exceptions;

import org.shaolin.bmdp.i18n.Localizer;

public class I18NRuntimeException extends BaseRuntimeException {
	private static final long serialVersionUID = 1L;

	public I18NRuntimeException(String message) {
		super(message);
	}
	
	public I18NRuntimeException(String msg, Localizer aLocalizer) {
		super(msg, null, null, aLocalizer);
	}

	public I18NRuntimeException(String message, Object... args) {
		super(message, args);
	}

	public I18NRuntimeException(String msg, Object[] args, Localizer aLocalizer) {
		super(msg, null, args, aLocalizer);
	}
	
	public I18NRuntimeException(String message, Throwable t) {
		super(message, t);
	}

	public I18NRuntimeException(String msg, Throwable t, Object... args) {
		super(msg, t, args);
	}
	
	public I18NRuntimeException(String msg, Throwable t, Object[] args, Localizer aLocalizer) {
		super(msg, t, args, aLocalizer);
	}
}
