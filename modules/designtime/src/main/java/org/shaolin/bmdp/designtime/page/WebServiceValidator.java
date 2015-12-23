package org.shaolin.bmdp.designtime.page;

import org.shaolin.bmdp.datamodel.common.DiagramType;
import org.shaolin.bmdp.datamodel.page.WebService;
import org.shaolin.bmdp.designtime.tools.GeneratorOptions;
import org.shaolin.bmdp.runtime.entity.EntityAddedEvent;
import org.shaolin.bmdp.runtime.entity.EntityManager;
import org.shaolin.bmdp.runtime.entity.EntityUpdatedEvent;
import org.shaolin.bmdp.runtime.entity.IEntityEventListener;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.cache.PageCacheManager;

public class WebServiceValidator implements IEntityEventListener<WebService, DiagramType> {

	protected GeneratorOptions option = null;
	
	public WebServiceValidator(GeneratorOptions option) {
		this.option = option;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		
	}

	@Override
	public void notifyLoadFinish(DiagramType diagram) {
		
	}

	@Override
	public void notifyAllLoadFinish() {
	}

	@Override
	public void notify(EntityAddedEvent<WebService, DiagramType> event) {
		try {
			PageCacheManager.addWebService(event.getEntity());
		} catch (ParsingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notify(EntityUpdatedEvent<WebService, DiagramType> event) {
	}

	@Override
	public Class<WebService> getEventType() {
		return WebService.class;
	}
}
