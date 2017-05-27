package com.jg.spider.crawler;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.util.DateUtil;
import com.jg.spider.entitys.AcquireOriginal;
import com.jg.spider.entitys.CleanUpData;
import com.jg.spider.entitys.SpiderRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Pattern;

import static com.jg.spider.entitys.AcquireOriginalFactory.ACQUIRE_ORIGINAL_FACTORY;
import static com.jg.spider.entitys.CleanUpDataFactory.CLEAN_UP_DATA_FACTORY;
import static com.jg.spider.entitys.SpiderRecordFactory.SPIDER_RECORD_FACTORY;

/**
 * create by 17/5/26.
 *
 * @author yimin
 */

class JXFJSZChildCrawler extends ChildCrawler {
	private final static Logger log = LogManager.getLogger(JXFJSZChildCrawler.class.getName());

	private final String region;
	private final String title;
	private final String href;
	private final String type;
	private final int webAddressId;
	private int id = -1;
	private AcquireOriginal acquireOriginal = null;

	JXFJSZChildCrawler(String region, String title, String href, String type, int webAddressId) {
		this.webAddressId = webAddressId;
		this.region = region;
		this.title = title;
		this.href = href;
		this.type = type;
	}

	/**
	 * 用来获取明细数据，目前仅处理表格形式，文字形式后续增加.
	 */
	@Override
	public boolean transition() {
		Document doc;
		SpiderRecord record = SPIDER_RECORD_FACTORY.getNewObject(null);
		record.set("get_url", href);

		try {
			doc = Jsoup.connect(href).get();
		} catch (IOException e) {
			log.warn("获取明细信息出现异常：", e);

			record.set("record", e.getMessage());
			record.set("is_succeed", 0);
			record.flush();

			return false;
		}

		Element mainBody = doc.getElementById("tblInfo");

		record.set("record", mainBody.toString());
		record.set("is_succeed", 1);
		record.flush();

		Element table = mainBody.getElementById("TDContent").getElementsByTag("table").first();

		acquireOriginal = ACQUIRE_ORIGINAL_FACTORY.getNewObject(null);
		acquireOriginal.set("absolute_url", href).set("content", title).set("is_work_out", 0).set("web_address_id", webAddressId).flush();
		this.id = acquireOriginal.get("id");

		analyze(table);
		return false;
	}

	public void analyze(Element table) {
		JSONObject analyzeJson = new JSONObject();

		analyzeJson.put("url", href);
		analyzeJson.put("webType", type);
		analyzeJson.put("title", title);
		analyzeJson.put("project", title);
		analyzeJson.put("createTime", DateUtil.convertNowToString());
		analyzeJson.put("region", region);

		Pattern tendereeP = Pattern.compile("^招标(人|单位)名称$");
		Pattern addressP = Pattern.compile("项目\\S*地址$");
		Pattern moneyP = Pattern.compile("总投资$");

		//招标单位  项目地址  总投资
		for (Element td : table.getElementsByTag("td")) {
			matcher(td, tendereeP, analyzeJson, "tenderee");
			matcher(td, addressP, analyzeJson, "address");
			matcher(td, moneyP, analyzeJson, "money");
		}

		CleanUpData cleanUpData = CLEAN_UP_DATA_FACTORY.getNewObject(null);
		cleanUpData.set("id", this.id).set("clean_up_data", analyzeJson).set("is_audit", 0).flush();

		if (acquireOriginal == null) {
			acquireOriginal = ACQUIRE_ORIGINAL_FACTORY.getObject("id", this.id);
		}

		acquireOriginal.set("is_work_out", 1).flush();
	}

	private void matcher(Element element, Pattern pattern, JSONObject analyzeJson, String name) {
		if (pattern.matcher(element.text()).find()) {
			String text = element.nextElementSibling().text();
			analyzeJson.put(name, text);
		}
	}


	@Override
	public String toString() {
		return String.format("id:%d,region:%s,title:%s,href:%s,type:%s,webAddressId:%d", id, region, title, href, type, webAddressId);
	}
}
