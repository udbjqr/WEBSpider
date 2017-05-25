package com.jg.spider.entitys;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

import java.util.Date;

/**
 * create by 17/5/25.
 *
 * @author yimin
 */

public class CleanUpDataFactory extends AbstractPersistenceFactory<CleanUpData> {
	public static final CleanUpDataFactory CLEAN_UP_DATA_FACTORY = new CleanUpDataFactory();

	private CleanUpDataFactory() {
		this.tableName = "clean_up_data";

		addField("id", Integer.class, null, true, true);
		addField("clean_up_data", JSONObject.class, null, false, false);
		addField("create_time", Date.class, null, true, false);
		addField("is_audit", Integer.class, null, false, false);

		setIsCheck(false);
		init();
	}

	@Override
	protected CleanUpData createObject(User user) {
		return new CleanUpData(this);
	}
}
