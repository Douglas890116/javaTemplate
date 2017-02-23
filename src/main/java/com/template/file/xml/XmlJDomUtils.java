package com.template.file.xml;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cloud on 2017-01-30.
 */
public class XmlJDomUtils {
    /* JDOM， 通过第三方的类库进行xml的解析*/


    public static void readXmlByJDom(File xml) throws JDOMException, IOException {


        // 1、创建一个SAXBuilder对象
        SAXBuilder saxBuilder = new SAXBuilder();
        // 2、解析xml文件获取Document对象
        Document document = saxBuilder.build(xml);
        // ※如果有乱码情况，可以使用一下方式进行读取
//        FileInputStream fis = new FileInputStream(xml);
//        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
//        Document document = saxBuilder.build(isr);

        // 3、通过document获取xml文件的根节点
        Element rootElement = document.getRootElement();
        // 4、获取根节点下的子节点
        List<Element> elements = rootElement.getChildren();
//        List<Element> elements = rootElement.getChildren("book");// 如果知道节点名称可直接
//        Element element = rootElement.getChild("status");// 通过名称获取单个节点
        List<Attribute> attributes;
        for (Element element : elements) {
            System.out.println("获取第" + elements.indexOf(element) + "个节点，节点名称是：" + element.getName());
            System.out.println(element.getValue());
            attributes = element.getAttributes();
            // 知道属性名的情况下获取 属性对象
//            Attribute attribute = element.getAttribute("count");
            // 知道属性名的情况下 直接获取 属性值
//            String value = element.getAttributeValue("count");
            for (Attribute attribute : attributes) {
                System.out.println("获取属性-" + attribute.getName() + "：" + attribute.getValue());
            }
        }
    }

    public static void main(String[] arg) {
        File xml = new File("src/main/resources/book.xml");
        try {
            XmlJDomUtils.readXmlByJDom(xml);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
