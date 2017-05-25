package com.jg.openTenders.entitys;

import com.jg.common.persistence.AbstractPersistence;
import com.jg.common.util.HttpClientUtil;

import static com.jg.openTenders.Constant.CONFIG;

/**
 * create by 17/5/25.
 *
 * @author yimin
 */

public class OpenTender extends AbstractPersistence {
	OpenTender(OpenTenderFactory factory) {
		super(factory);
	}

	/**
	 * 把自已推送至推送服务器.
	 *
	 * @return true:成功请求 false:失败请求
	 */
	public boolean push() {
		String url = CONFIG.getString("pushServer");

		String response = HttpClientUtil.post(url, get("data"));

		if (!response.startsWith("error:")) {
			set("is_push", 1);

			flush();
			return true;
		}

		return false;
	}
}
