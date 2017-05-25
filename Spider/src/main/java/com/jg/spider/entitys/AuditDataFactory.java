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

public class AuditDataFactory extends AbstractPersistenceFactory<AuditData> {
	public static final AuditDataFactory WEB_ADDRESS_FACTORY = new AuditDataFactory();

	private AuditDataFactory() {
		this.tableName = "audit_data";

		addField("id", Integer.class, null, true, true);
		addField("audit_data", JSONObject.class, null, false, false);
		addField("create_time", Date.class, null, true, false);
		addField("is_upload", Integer.class, null, false, false);

		setIsCheck(false);
		init();
	}

	@Override
	protected AuditData createObject(User user) {
		return new AuditData(this);
	}
}
