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

public class AcquireOriginalFactory extends AbstractPersistenceFactory<AcquireOriginal> {
	public static final AcquireOriginalFactory ACQUIRE_ORIGINAL_FACTORY = new AcquireOriginalFactory();

	private AcquireOriginalFactory() {
		this.tableName = "acquire_original_data";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_original_data')", true, true);
		sequenceField.setSerial("seq_original_data");

		addField("web_address_id", Integer.class, null, false, false);
		addField("is_work_out", Integer.class, null, false, false);
		addField("content", JSONObject.class, null, false, false);
		addField("absolute_url", String.class, null, true, false);
		addField("create_time", Date.class, null, true, false);

		setIsCheck(false);
		init();
	}

	@Override
	protected AcquireOriginal createObject(User user) {
		return new AcquireOriginal(this);
	}
}
