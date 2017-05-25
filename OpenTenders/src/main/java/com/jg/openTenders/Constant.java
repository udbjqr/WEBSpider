package com.jg.openTenders;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.util.FileUtil;
import org.apache.logging.log4j.core.util.Loader;

/**
 * 本程序使用的部分常量
 * create by 17/5/25.
 *
 * @author yimin
 */

public class Constant {
	static {
		String config = FileUtil.getProperties(Loader.getResource("openTendersConfig.json", Constant.class.getClassLoader()), "UTF-8");
		CONFIG = JSONObject.parseObject(config);
	}


	public final static JSONObject CONFIG;
}
