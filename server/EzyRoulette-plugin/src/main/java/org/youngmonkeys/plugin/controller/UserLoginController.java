package org.youngmonkeys.plugin.controller;

import static com.tvd12.ezyfoxserver.constant.EzyEventNames.USER_LOGIN;

import com.tvd12.ezyfox.security.EzySHA256;
import com.tvd12.ezyfoxserver.constant.EzyLoginError;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import org.youngmonkeys.common.entity.User;
import org.youngmonkeys.common.service.UserService;
import org.youngmonkeys.plugin.service.WelcomeService;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;

@EzySingleton
@EzyEventHandler(USER_LOGIN)
public class UserLoginController extends EzyAbstractPluginEventController<EzyUserLoginEvent> {

	@EzyAutoBind
	private WelcomeService welcomeService;
	
	@EzyAutoBind
	private UserService userService;
	
	@Override
	public void handle(EzyPluginContext ctx, EzyUserLoginEvent event) {
		logger.info("{} login in", welcomeService.welcome(event.getUsername()));
		
		String username = event.getUsername();
		String password = encodePassword(event.getPassword());
		
		User user = userService.getUser(username);
		
		if (user == null) { // user doesn't exist in db
			logger.info("user doesn't exist in db, create a new one!");
			user = userService.createUser(username, password);
		}
		
		if (!user.getPassword().equals(password)) {
			throw new EzyLoginErrorException(EzyLoginError.INVALID_PASSWORD);
		}
		
		logger.info("user and password match, accept user {}", username);
	}
	
	private String encodePassword(String password) {
		return EzySHA256.cryptUtfToLowercase(password);
	}
	
}
