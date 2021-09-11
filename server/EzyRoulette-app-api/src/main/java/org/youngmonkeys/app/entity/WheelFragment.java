package org.youngmonkeys.app.entity;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.Data;

@Data
@EzyObjectBinding
public class WheelFragment {
	private int index;
	private int prizeValue;
	private int quantity;
	private int ratio;
	private WheelPrizeType prizeType;
}
