package cn.simon.utils;

import cn.hutool.core.util.XmlUtil;
import cn.simon.entity.Weather;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author ：Simon
 * @date ：Created in 2022/8/29 1:19
 * @description：
 * @modified By：
 * @version: v1.0
 */
@Component
public class Dom4jUtil {
    public Weather parseXml(String xml){
        Document document = XmlUtil.parseXml(xml);
        String city = (String) XmlUtil.getByXPath("//resp/city", document, XPathConstants.STRING);
        String wendu = (String) XmlUtil.getByXPath("//resp/wendu", document, XPathConstants.STRING);
        String sunrise_1 = (String) XmlUtil.getByXPath("//resp/sunrise_1", document, XPathConstants.STRING);
        String sunset_1 = (String) XmlUtil.getByXPath("//resp/sunset_1", document, XPathConstants.STRING);
        //
        List<String> details = new ArrayList<String>();
        try {
            org.dom4j.Document parseText = DocumentHelper.parseText(xml);
            Element rootElement = parseText.getRootElement();
            Element element = rootElement.element("zhishus");
            //遍历某个子节点，如resultcode
            for (Iterator i = element.elementIterator("zhishu"); i.hasNext(); ){
                Element  next = (Element) i.next();
                Element detail = next.element("detail");
                String text = detail.getText();
                details.add(text);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        String chuanyi = details.get(0);
        String detail = details.get(4);
        return new Weather(city,wendu,chuanyi,sunrise_1,sunset_1,"“"+detail+"”");
    }
}
