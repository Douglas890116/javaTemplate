package com.template.file.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cloud on 2017-01-30.
 */
public class XmlDom4jUtils {
    /* DOM4J，通过第三方的类库进行xml的解析 */

    public static void readXmlByDom4J(File xml) throws DocumentException {
        // 1、创建SAXReader对象
        SAXReader saxReader = new SAXReader();
        // 2、通过saxReader解析xml文件获取Document对象
        Document document = saxReader.read(xml);
        // ※若读取出现乱码情况可使用一下方式读取
//        FileInputStream fis = new FileInputStream(xml);
//        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
//        Document document = saxReader.read(isr);
        // 3、通过document对象获取xml文件根节点
        Element rootElement = document.getRootElement();
        // 4、通过更节点获取子节点的迭代器
        Iterator<Element> elementIterator = rootElement.elementIterator();
//        Iterator<Element> elementIterator = rootElement.elementIterator("子节点名称");
//        Element element = rootElement.element("子节点名称");
        Element element;
        List<Attribute> attributes;
        while (elementIterator.hasNext()) {
            element = elementIterator.next();
            // 获取属性
            attributes = element.attributes();
            for (Attribute attr : attributes) {
                // 获取属性的 属性名 与 属性值
                System.out.println("节点【" + attr.getName() + "】属性的值为：" + attr.getValue());
            }
            for (int i = 0; i < attributes.size(); i++) {
                // 获取属性的 属性名 与 属性值
                System.out.println("节点【" + attributes.get(i).getName() + "】属性的值为：" + attributes.get(i).getValue());
            }
            // 在知道属性名称的情况下可以使用一下方式
//            Attribute attribute = element.attribute("属性名称");
            System.out.println("节点【" + element.getName() + "】的值为：" + element.getStringValue());
            element.getText();
            element.getTextTrim();
            // 获取子节点
            element.elementIterator("子节点名称");
        }
    }

    public static void main(String[] arg) {
        File xml = new File("src/main/resources/book.xml");
        try {
            XmlDom4jUtils.readXmlByDom4J(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
