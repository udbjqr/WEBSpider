import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * create by 17/5/26.
 *
 * @author yimin
 */

public class BaseTet {


	@Test
	public void testRead() throws IOException {
		Document doc = Jsoup.connect("http://ggzy.jiangxi.gov.cn/jxzbw/jyxx/002001/002001002/MoreInfo.aspx?CategoryNum=002001002").get();
		doc.setBaseUri("http://ggzy.jiangxi.gov.cn/");

		Element element = doc.getElementById("MoreInfoList1_DataGrid1");
		element.getElementsByTag("a").forEach(el -> {
			System.out.println(el.getElementsByTag("font").text() + "\t" + el.attr("title"));
			System.out.println(doc.baseUri() + el.attr("href"));
		});
	}


	@Test
	public void testRegex(){
		Pattern pattern = Pattern.compile("项目\\S*地址$");


		System.out.println(pattern.matcher("项目\\S*地址").find());
		System.out.println(pattern.matcher("项目地址").find());
		System.out.println(pattern.matcher("工程项目建设地址").find());
		System.out.println(pattern.matcher("工程项目建设地址ab").find());
		System.out.println(pattern.matcher("招标人名称1234").find());

	}

}
