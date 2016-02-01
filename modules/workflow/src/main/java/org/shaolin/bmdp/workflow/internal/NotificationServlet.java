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
package org.shaolin.bmdp.workflow.internal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shaolin.bmdp.runtime.spi.IAppServiceManager;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.utils.FileUtil;
import org.shaolin.bmdp.utils.SerializeUtil;
import org.shaolin.bmdp.workflow.be.NotificationImpl;
import org.shaolin.bmdp.workflow.coordinator.ICoordinatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This notification is a receiver that accepts the notifications from 
 * either the master node or another node.
 * 
 * @author wushaol
 *
 */
public class NotificationServlet extends HttpServlet {
	
	private static final long serialVersionUID = 236538261853041089L;
	private static final Logger logger = LoggerFactory.getLogger(NotificationServlet.class);
	
	private String charset = "UTF-8";
	
	public void init() throws ServletException {
		String value = getServletConfig().getInitParameter("content");
        if (value != null)
        {
            String content = value;
            //parse charset
            String[] s = content.split(";", 0);
            for (int i = 0, n = s.length; i < n; i++)
            {
                if (s[i].startsWith("charset="))
                {
                    charset = s[i].substring(8);
                    break;
                }
            }
        }
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		process(request, response);
	}
	
	protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
		if (request.getProtocol().compareTo("HTTP/1.0") == 0) {
			response.setHeader("Pragma", "no-cache");
		} else if (request.getProtocol().compareTo("HTTP/1.1") == 0) {
			response.setHeader("Cache-Control", "no-cache");
		}
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding(charset);
		response.setContentType("json");
		request.setCharacterEncoding(charset);
		
		NotificationImpl notification = null;
		try {
			notification = SerializeUtil.readData(FileUtil.read(request.getInputStream()), NotificationImpl.class);
		} catch (Throwable e) {
			logger.warn("Faile to decode the request: " + e.getMessage());
			PrintWriter out = response.getWriter();
			out.print("{result: 0}");
			return;
		}
		
		// only add notification to the master node.
		IAppServiceManager serviceManager= IServerServiceManager.INSTANCE.getApplication(
				IServerServiceManager.INSTANCE.getMasterNodeName());
		ICoordinatorService coordinator = serviceManager.getService(ICoordinatorService.class);
		if (coordinator != null) {
			coordinator.addNotification(notification, false);
		}
		
		PrintWriter out = response.getWriter();
		out.print("{result: 1}");
    }
}
