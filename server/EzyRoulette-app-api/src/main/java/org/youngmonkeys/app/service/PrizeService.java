package org.youngmonkeys.app.service;

import com.tvd12.ezydata.database.repository.EzyMaxIdRepository;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import org.youngmonkeys.app.entity.Prize;
import org.youngmonkeys.app.repo.PrizeRepo;

@EzySingleton
public class PrizeService {
	
	@EzyAutoBind
	private PrizeRepo prizeRepo;
	
	@EzyAutoBind
	private EzyMaxIdRepository maxIdRepository;
	
	public Prize createPrize(String username, int prizeRecord) {
		Prize prize = new Prize();
		prize.setId(maxIdRepository.incrementAndGet("prize"));
		prize.setUsername(username);
		prize.setPrize(prizeRecord);
		
		prizeRepo.save(prize);
		return prize;
	}
}
