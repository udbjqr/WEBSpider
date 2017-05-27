package com.jg.spider.servlet;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.spider.entitys.CleanUpData;

import javax.servlet.annotation.WebServlet;

import static com.jg.common.result.HttpResult.*;
import static com.jg.common.sql.DBHelperFactory.DB_HELPER;
import static com.jg.spider.entitys.AuditDataFactory.AUDIT_DATA_FACTORY;
import static com.jg.spider.entitys.CleanUpDataFactory.CLEAN_UP_DATA_FACTORY;

/**
 * create by 17/5/27.
 *
 * @author yimin
 */

@WebServlet(name = "audit", urlPatterns = "/audit")
public class AuditServlet extends BaseServlet {
	@Override
	protected HttpResult execute(HttpRequestType type, ServletData servletData) {
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);

		switch (type) {
			case weblist:
				return listWeb();
			case save:
				return audit(jsonData);
			case list:
				return list(jsonData);
			default:
				return UNKNOWN;
		}
	}

	private HttpResult listWeb() {
		final HttpResult[] httpResult = {null};

		DB_HELPER.selectWithSet("select id as value,name as label from web_address", set -> httpResult[0] = SUCCESS.clone().setListToData(LIST, set, "label", "value"));

		return httpResult[0];
	}

	private HttpResult list(JSONObject data) {
		Integer webId = data.getInteger("web_id");
		if (webId == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：web_id");
		}

		final HttpResult[] httpResult = {null};

		DB_HELPER.selectWithSet("select c.id,c.clean_up_data from clean_up_data c INNER JOIN acquire_original_data a on a.id = c.id where a.web_address_id = " + webId + " and c.is_audit = 0;", set -> httpResult[0] = SUCCESS.clone().setListToData(LIST, set, "id", "clean_up_data"));

		return httpResult[0];
	}

	private HttpResult audit(JSONObject data) {
		Integer id = data.getInteger("id");
		JSONObject saveData = new JSONObject(data);

		if (id == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：id");
		}

		CleanUpData cleanUpData = CLEAN_UP_DATA_FACTORY.getObject("id", id);
		if (cleanUpData == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("指定的定义Id未找到对象，id：" + id);
		}

		saveData.remove("type");
		AUDIT_DATA_FACTORY.getNewObject(null).set("id", id).set("id", id).set("audit_data", saveData).set("is_upload", 0).flush();
		cleanUpData.set("is_audit", 1).flush();

		return SUCCESS.clone().addInfoToValue("id", id);
	}


}
