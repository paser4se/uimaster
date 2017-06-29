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
package org.shaolin.uimaster.page;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.utils.ImageUtil;
import org.shaolin.uimaster.page.ajax.AFile;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.shaolin.uimaster.page.flow.WebflowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadFileServlet extends HttpServlet {
	private static final int DEFAULT_CONTENTSIZE = 5120000;// 5M

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UploadFileServlet.class);
	
	public void init() throws ServletException {
//		String uploadFileRoot = this.getInitParameter("uploadFileRoot");
//		if (uploadFileRoot == null || uploadFileRoot.trim().isEmpty()) {
//			throw new IllegalArgumentException("The 'uploadFileRoot' parameter is null!");
//		}
//		WebConfig.setUploadFileRoot(uploadFileRoot);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession httpSession = request.getSession(false);
		if (httpSession == null
				|| httpSession.getAttribute(AjaxContext.AJAX_COMP_MAP) == null) {
			IDataItem dataItem = AjaxContextHelper
					.createSessionTimeOut(WebConfig.replaceWebContext(WebConfig.getTimeoutPage()));
			JSONArray array = new JSONArray();
			array.put(new JSONObject(dataItem));
			response.getWriter().print(array.toString());
			return;
		}
		
		AFile file = null;
		try {
			Map<String, JSONObject> uiMap = AjaxContextHelper.getFrameMap(request);
			String uiid = request.getParameter("_uiid");
			if (!uiMap.containsKey(uiid)) {
				//User does not have the permission to upload the file.
				IDataItem dataItem = AjaxContextHelper.createNoPermission("\u6CA1\u6709\u8BBF\u95EE\u6743\u9650\uFF01");
				JSONArray array = new JSONArray();
				array.put(new JSONObject(dataItem));
				response.getWriter().print(array.toString());
				return;
			}
			JSONObject json = uiMap.get(uiid);
			file = (AFile)Widget.covertFromJSON(json);
		} catch (Exception e1) {
			logger.warn("Error to get the file ui widget: " + e1.getMessage(), e1);
			return;
		}
		
		HttpSession session = request.getSession();
		UserContext currentUserContext = (UserContext)session.getAttribute(WebflowConstants.USER_SESSION_KEY);
		String userLocale = WebConfig.getUserLocale(request);
		List userRoles = (List)session.getAttribute(WebflowConstants.USER_ROLE_KEY);
		String userAgent = request.getHeader("user-agent");
		boolean isMobile = MobilitySupport.isMobileRequest(userAgent);
		//add user-context thread bind
        UserContext.register(session, currentUserContext, userLocale, userRoles, isMobile);
		LocaleContext.createLocaleContext(userLocale);
		
		String orgCode = (String)UserContext.getUserData(UserContext.CURRENT_USER_ORGNAME);
        if (orgCode == null) {
        	orgCode = IServerServiceManager.INSTANCE.getMasterNodeName();
        }
        AppContext.register(IServerServiceManager.INSTANCE.getApplication(orgCode));
		
		// move the file to the real path.
		if (file == null || file.getStoredPath() == null || file.getStoredPath().isEmpty()) {
			// The file stored path is null
			IDataItem dataItem = AjaxContextHelper.createErrorDataItem("\u4E0A\u4F20\u6587\u4EF6\u6CA1\u6709\u6307\u5B9A\u8DEF\u5B58\u653E\u5F84\uFF01");
			JSONArray array = new JSONArray();
			array.put(new JSONObject(dataItem));
			response.getWriter().print(array.toString());
			return;
		}
		String path = file.getStoredPath();
		if (path.startsWith(WebConfig.getWebRoot())) {
			path = path.substring(WebConfig.getWebRoot().length());
		}
		File root = new File(WebConfig.getResourcePath() + File.separator + path);
		if (file.getStoredPath().startsWith(WebConfig.getResourcePath())) {
			root = new File(file.getStoredPath());
		} 
		if (root.isDirectory() && file.getAllowedNumbers() > 0 
				&& root.list().length >= file.getAllowedNumbers()) {
			// exceeded.
			IDataItem dataItem = AjaxContextHelper.createErrorDataItem("\u8D85\u51FA\u6700\u5927\u4E2A\u6570\u4E86\u3002");
			JSONArray array = new JSONArray();
			array.put(new JSONObject(dataItem));
			response.getWriter().print(array.toString());
			return;
		}
		if (!root.exists() && path.lastIndexOf(".") == -1) {
			//make sure it's a folder.
			root.mkdirs();
		}
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		// process only if its multipart content
		if (isMultipart) {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				// Parse the request
				int index = 0;
				List<FileItem> multiparts = upload.parseRequest(request);
				logger.info("Uploaded items: " + multiparts.size());
				for (FileItem item : multiparts) {
					if (file.getAllowedNumbers() <= (index++)) {
						logger.warn("the size of the uploading file is exceeded! size: " + item.getSize());
						break;
					}
					// TODO: security check
					// item.getContentType(); file.getSuffix(); image/jpg
					if ((file.getContentSize() > 0 && item.getSize() > file.getContentSize()) 
							|| (item.getSize() > DEFAULT_CONTENTSIZE)) {
						logger.warn("the size of the uploading file is exceeded! size: " + item.getSize());
						IDataItem dataItem = AjaxContextHelper.createErrorDataItem("\u4E0A\u4F20\u6587\u4EF6\u5FC5\u987B\u5C0F\u4E8E2M");
						JSONArray array = new JSONArray();
						array.put(new JSONObject(dataItem));
						response.getWriter().print(array.toString());
						return;
					}
					if (!item.isFormField()) {
						if (root.isDirectory()) {
							//gallery support.
							String name;
							if ((new File(root, item.getName())).exists()) {
								name = ((int)(Math.random()*1000)) + item.getName();
							} else {
								name = new File(item.getName()).getName();
							}
							String suffix = name.substring(name.lastIndexOf('.'));
							name = "f" + Math.abs(name.hashCode()) + suffix;  
							logger.info("Received the uploading file: " + name + ", saving path: " + root);
							File finalPicture = new File(root, name);
							item.write(finalPicture);
							
							if (file.getWidth() > 0 && file.getHeight() > 0) {
								try {
									ImageUtil.resizeImage(finalPicture, file.getWidth(), file.getHeight(), finalPicture);
								} catch (Exception e) {
									//maybe not an image
								}
							}
						} else {
							//single file.
							if (!root.exists() && !root.getParentFile().exists()) {
								root.getParentFile().mkdirs();
							}
							item.write(root);
							logger.info("Received the uploading file: " + item.getName() + ", saving path: " + root);
							if (file.getWidth() > 0 && file.getHeight() > 0) {
								try {
									ImageUtil.resizeImage(root, file.getWidth(), file.getHeight(), root);
								} catch (Exception e) {
									//maybe not an image
								}
							}
						}
					}
				}
				file.refresh();
			} catch (Exception e) {
				logger.warn("Failed to receive the uploading file: " + e.getMessage(), e);
				IDataItem dataItem = AjaxContextHelper.createErrorDataItem("Failed to receive the uploading file: " + e.getMessage());
				JSONArray array = new JSONArray();
				array.put(new JSONObject(dataItem));
				response.getWriter().print(array.toString());
				return;
			}
		}
	}
}