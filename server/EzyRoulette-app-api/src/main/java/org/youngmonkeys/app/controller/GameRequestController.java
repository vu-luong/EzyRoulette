package org.youngmonkeys.app.controller;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import org.youngmonkeys.app.service.WheelService;

@EzyRequestController
public class GameRequestController extends EzyLoggable {
	
	@EzyAutoBind
	WheelService wheelService;
	
	@EzyAutoBind
	EzyResponseFactory responseFactory;
	
	@EzyDoHandle("spin")
	public void spin(EzyUser user) {
		int result = wheelService.spin();
		
		logger.info("user {} spin, prize result = {}", user.getName(), result);
		
		responseFactory.newObjectResponse()
				.command("spin")
				.param("result", result)
				.user(user)
				.execute();
	}
}
