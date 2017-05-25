package com.jg.openTenders.entitys;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

import java.util.Date;

/**
 * create by 17/5/25.
 *
 * @author yimin
 */

public class OpenTenderFactory extends AbstractPersistenceFactory<OpenTender> {
	public static final OpenTenderFactory OPEN_TENDER_FACTORY = new OpenTenderFactory();

	private OpenTenderFactory() {
		this.tableName = "open_tenders_data ";

		addField("id", Integer.class, null, true, true);
		addField("tenderee", String.class, null, true, false);
		addField("data", JSONObject.class, null, false, false);
		addField("money", Double.class, null, false, false);
		addField("location", Integer.class, null, false, false);
		addField("create_time", Date.class, "now()", true, false);
		addField("is_push", Integer.class, "0", true, false);

		init();
	}

	@Override
	protected OpenTender createObject(User user) {
		return new OpenTender(this);
	}
}
