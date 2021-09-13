package org.youngmonkeys.app.entity;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.Data;

import java.util.Date;

@Data
@EzyCollection
public class Prize {
	@EzyId
	private Long id;
	
	private Date date = new Date();
	private String username;
	private int prize;
}
