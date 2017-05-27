package com.jg.spider.crawler;

import com.jg.spider.entitys.SpiderRecord;
import com.jg.spider.entitys.WEBAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import static com.jg.common.sql.DBHelperFactory.DB_HELPER;
import static com.jg.spider.entitys.SpiderRecordFactory.SPIDER_RECORD_FACTORY;

/**
 * 针对江西省公共资源网的房建及市政数据获取方式.
 * <p>
 * create by 17/5/26.
 *
 * @author yimin
 */

public class JXFJSZParentCrawler extends ParentCrawler {
	private final static Logger log = LogManager.getLogger(JXFJSZParentCrawler.class.getName());

	/**
	 * 从数据库当中获取相应的数据初始化.
	 *
	 * @param web web的对象
	 */
	public JXFJSZParentCrawler(WEBAddress web) {
		super(web);
	}


	@Override
	public boolean obtainDetail() {
		children.clear();
		Document doc;
		String detailUrl;

		SpiderRecord record = SPIDER_RECORD_FACTORY.getNewObject(null);
		record.set("get_url", url);
		try {
			doc = Jsoup.connect(url).get();
			doc.setBaseUri(baseUrl);
		} catch (IOException e) {
			log.warn("获取网址信息异常。", e);
			record.set("record", e.getMessage()).set("is_succeed", 0).flush();

			return false;
		}

		record.set("record", doc.body().toString()).set("is_succeed", 1).flush();

		Element element = doc.getElementById("MoreInfoList1_DataGrid1");
		for (Element el : element.getElementsByTag("a")) {
			detailUrl = doc.baseUri() + el.attr("href");
			if (!alreadyObtain(detailUrl)) {
				children.add(new JXFJSZChildCrawler(el.getElementsByTag("font").text(), el.attr("title"), detailUrl, type, id));
			}
		}

		log.info("此次获取明细数据{}条。",children.size());
		return true;
	}

	private boolean alreadyObtain(String detailUrl) {
		return (Long) DB_HELPER.selectOneValues("select count(*) from acquire_original_data where absolute_url = " + DB_HELPER.getString(detailUrl)) > 0;
	}
}
