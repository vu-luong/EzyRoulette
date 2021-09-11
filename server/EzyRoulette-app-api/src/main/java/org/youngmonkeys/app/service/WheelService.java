package org.youngmonkeys.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzyPostInit;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import org.youngmonkeys.app.entity.Wheel;
import org.youngmonkeys.app.repo.WheelRepo;

import java.io.IOException;
import java.io.InputStream;

@EzySingleton
public class WheelService {
	
	private Wheel wheel;
	
	@EzyAutoBind
	private WheelRepo wheelRepo;
	
	@EzyPostInit
	public void postInit() {
		wheel = wheelRepo.findById("wheel");
		
		if (wheel == null) {
			wheel = loadWheelFromJsonFile();
			wheelRepo.save(wheel);
		}
	}
	
	private Wheel loadWheelFromJsonFile() {
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream inputStream = new EzyAnywayInputStreamLoader().load("wheel.json");
		try {
			Wheel answer = objectMapper.readValue(inputStream, Wheel.class);
			answer.setId("wheel");
			return answer;
		} catch (IOException e) {
			throw new IllegalStateException("Cannot load wheel data", e);
		}
	}
}
