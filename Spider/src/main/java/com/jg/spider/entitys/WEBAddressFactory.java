package com.jg.spider.entitys;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

/**
 * create by 17/5/25.
 *
 * @author yimin
 */

public class WEBAddressFactory extends AbstractPersistenceFactory<WEBAddress> {
	public static final WEBAddressFactory WEB_ADDRESS_FACTORY = new WEBAddressFactory();

	private WEBAddressFactory() {
		this.tableName = "web_address";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_web_address')", true, true);
		sequenceField.setSerial("seq_web_address");

		addField("name", String.class, null, false, false);
		addField("url", String.class, null, false, false);
		addField("regular_model", String.class, null, false, false);
		addField("flag", Integer.class, null, false, false);
		addField("parallelism", JSONObject.class, null, true, false);
		addField("type", String.class, "0", true, false);
		addField("base_url", String.class, "", true, false);

		init();
	}

	@Override
	protected WEBAddress createObject(User user) {
		return new WEBAddress(this);
	}
}
