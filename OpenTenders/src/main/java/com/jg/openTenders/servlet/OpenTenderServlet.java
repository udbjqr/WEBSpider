package com.jg.openTenders.servlet;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.openTenders.entitys.OpenTender;

import javax.servlet.annotation.WebServlet;

import static com.jg.common.result.HttpResult.*;
import static com.jg.openTenders.entitys.OpenTenderFactory.OPEN_TENDER_FACTORY;

/**
 * 接收爬虫得到的数据并保存至数据库当中.
 * <p>
 * create by 17/5/25.
 *
 * @author yimin
 */
@WebServlet(name = "openTender", urlPatterns = "/openTender")
public class OpenTenderServlet extends BaseServlet {
	@Override
	protected HttpResult execute(HttpRequestType type, ServletData servletData) {
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);

		switch (type) {
			case save:
			case insert:
				return saveTo(jsonData);
			case load:
				return load(jsonData, servletData);
			default:
				return UNKNOWN;
		}
	}

	private HttpResult load(JSONObject data, ServletData servletData) {
		String token = data.getString("token");
		if (token == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：token");
		}

		//TODO 现在全部返回，以后根据条件增加控制
		Integer userId = getUserId(token);
		if (userId == null || userId == -1) {
			return NOT_LOGIN;
		}

		return SUCCESS.clone().setListToData(DATA, OPEN_TENDER_FACTORY.getAllObjects(null), "id", "data", "money", "tenderee", "location");
	}

	//TODO 这里需要去替换掉
	private Integer getUserId(String token) {
		return -1;
	}

	private HttpResult saveTo(JSONObject data) {
		OpenTender tender = OPEN_TENDER_FACTORY.getNewObject(null);

		tender.set(ID, data.get(ID));
		tender.set(DATA, data.get(DATA));
		tender.set("money", data.get("money"));
		tender.set("tenderee", data.get("tenderee"));
		tender.set("location", data.get("location"));
		tender.set("is_push", 0);

		tender.flush();
		tender.push();

		return SUCCESS;
	}
}
