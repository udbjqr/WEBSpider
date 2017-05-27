package com.jg.spider.crawler;

import com.alibaba.fastjson.JSONObject;
import com.jg.spider.entitys.WEBAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.jg.spider.entitys.WEBAddressFactory.WEB_ADDRESS_FACTORY;

/**
 * 执行获取网站信息动作，在获取完之后将生成详细地址列表，并返回.
 * <p>
 * 此类的对象不保证线程安全，调用类需要保证线程安全
 * <p>
 * 子类从此类继承并重写获取网站数据代码，此分离每一个不同的网站获取方式
 * <p>
 * create by 17/5/26.
 *
 * @author yimin
 */

public abstract class ParentCrawler {
	private final static Logger log = LogManager.getLogger(ParentCrawler.class.getName());

	protected final int id;
	protected final String type;
	protected final String url;
	protected final String baseUrl;
	protected final String regularModel;
	protected final JSONObject parallelism;
	protected final List<ChildCrawler> children = new ArrayList<>();

	public List<ChildCrawler> getChildren() {
		return children;
	}

	public static ParentCrawler getInstance(int webId) {
		WEBAddress web = WEB_ADDRESS_FACTORY.getObject("id", webId);
		if (web == null) {
			throw new NullPointerException("指定的ID未找到对应数据。id:" + webId);
		}

		if (1 != (Integer) web.get("flag")) {
			throw new RuntimeException("指定的ID已经不可用。id:" + webId);
		}

		switch (webId) {
			case 200:
			case 201:
				return new JXFJSZParentCrawler(web);
			default:
				return null;
		}
	}

	/**
	 * 从数据库当中获取相应的数据初始化.
	 *
	 * @param web web的对象
	 */
	protected ParentCrawler(WEBAddress web) {
		this.id = web.get("id");
		this.url = web.get("url");
		this.baseUrl = web.get("base_url");
		this.regularModel = web.get("regular_model");
		this.parallelism = web.get("parallelism");
		this.type = web.get("type");
	}

	public abstract boolean obtainDetail();

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getRegularModel() {
		return regularModel;
	}

	public JSONObject getParallelism() {
		return parallelism;
	}
}
