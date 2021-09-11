package org.youngmonkeys.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzyPostInit;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import org.youngmonkeys.app.entity.Wheel;
import org.youngmonkeys.app.entity.WheelFragment;
import org.youngmonkeys.app.entity.WheelPrizeType;
import org.youngmonkeys.app.repo.WheelRepo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@EzySingleton
public class WheelService {
	
	private static final int MAX_VALUE = 1000000;
	private Wheel wheel;
	private List<WheelFragment> emptyFragments;
	private List<Integer> randomRanges;
	private final Object lock = new Object();
	
	@EzyAutoBind
	private WheelRepo wheelRepo;
	
	@EzyPostInit
	public void postInit() {
		wheel = wheelRepo.findById("wheel");
		
		if (wheel == null) {
			wheel = loadWheelFromJsonFile();
			wheelRepo.save(wheel);
		}
		
		emptyFragments = wheel.getFragments()
				.stream()
				.filter(it -> it.getPrizeType() == WheelPrizeType.EMPTY)
				.collect(Collectors.toList());
		AtomicInteger lastUpperBound = new AtomicInteger(0);
		randomRanges = wheel.getFragments()
				.stream()
				.map(it -> lastUpperBound.addAndGet((int) ((it.getRatio() / 100.0) * MAX_VALUE)))
				.collect(Collectors.toList());
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
	
	public int spin() {
		int randomFragmentIndex = getRandomFragmentIndex();
		synchronized (lock) {
			WheelFragment fragment = wheel.getFragments().get(randomFragmentIndex);
			if (fragment.getPrizeType() == WheelPrizeType.EMPTY) {
				return randomFragmentIndex;
			}
			
			if (fragment.getQuantity() > 0) {
				int remainQuantity = fragment.getQuantity() - 1;
				fragment.setQuantity(remainQuantity);
				wheelRepo.save(wheel);
				return randomFragmentIndex;
			}
		}
		return randomEmptyIndex();
	}
	
	private int getRandomFragmentIndex() {
		int randomValue = ThreadLocalRandom.current().nextInt(MAX_VALUE);
		int randomIndex = -1;
		for (int i = 0; i < randomRanges.size(); i++) {
			int upperBound = randomRanges.get(i);
			if (randomValue <= upperBound) {
				randomIndex = i;
				break;
			}
		}
		if (randomIndex == -1) {
			randomIndex = randomEmptyIndex();
		}
		return randomIndex;
	}
	
	private int randomEmptyIndex() {
		int emptyIndex = ThreadLocalRandom.current().nextInt(emptyFragments.size());
		return emptyFragments.get(emptyIndex).getIndex();
	}
}
