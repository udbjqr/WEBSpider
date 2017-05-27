package com.jg.spider.entitys;

import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.identification.User;

import java.util.Date;

/**
 * create by 17/5/25.
 *
 * @author yimin
 */

public class SpiderRecordFactory extends AbstractPersistenceFactory<SpiderRecord> {
	public static final SpiderRecordFactory SPIDER_RECORD_FACTORY = new SpiderRecordFactory();

	private SpiderRecordFactory() {
		this.tableName = "spider_record";
		this.sequenceField = addField("id", Integer.class, "nextval('seq_spider_record')", true, true);
		sequenceField.setSerial("seq_spider_record");

		addField("create_time", Date.class, "now()", true, false);
		addField("get_url", String.class, null, true, false);
		addField("is_succeed", Integer.class, null, false, false);
		addField("record", String.class, null, false, false);

		setIsCheck(false);
		init();
	}

	@Override
	protected SpiderRecord createObject(User user) {
		return new SpiderRecord(this);
	}
}
