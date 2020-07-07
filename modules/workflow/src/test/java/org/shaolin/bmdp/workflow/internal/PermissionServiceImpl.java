package org.shaolin.bmdp.workflow.internal;

import java.util.List;

import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.bmdp.runtime.security.IPermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements IPermissionService {

	@Override
	public void reloadRolePermissions(IConstantEntity partyType) {
		
	}

	@Override
	public int checkModule(long moduleId, IConstantEntity role) {
		return 0;
	}

	@Override
	public int checkModule(long moduleId, List<IConstantEntity> roles) {
		return 0;
	}

	@Override
	public int checkModule(long moduleId, String orgId, List<IConstantEntity> roles) {
		return 0;
	}

	@Override
	public int checkModule(String orgCode, String chunkName, String nodeName, List<IConstantEntity> roles) {
		return 0;
	}

	@Override
	public int checkUIWidget(String pageName, String widgetId, IConstantEntity role) {
		return 0;
	}

	@Override
	public int checkUIWidget(String pageName, String widgetId, List<IConstantEntity> roles) {
		return 0;
	}

	@Override
	public int checkUITableWidget(String pageName, String widgetId, String columnId, IConstantEntity role) {
		return 0;
	}

	@Override
	public int checkUITableWidget(String pageName, String widgetId, String columnId, List<IConstantEntity> roles) {
		return 0;
	}

	@Override
	public int checkBEDate(String beName, String field, IConstantEntity role) {
		return 0;
	}

	@Override
	public int checkBEDate(String beName, String field, List<IConstantEntity> roles) {
		return 0;
	}
	
}
