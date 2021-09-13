package org.youngmonkeys.app.repo;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import org.youngmonkeys.app.entity.Prize;

@EzyRepository("prizeRepo")
public interface PrizeRepo extends EzyMongoRepository<Long, Prize> {
}
