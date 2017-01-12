package com.template.file;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Cloud on 2017-01-06.
 */
public class XmlUtils {
    /* DOM，通过java自带的类进行xml的解析 */

    /**
     * 读取xml文件:本用例采用api.xml文件做测试文件
     *
     * @param xml
     */
    public static void readXmlByDOM(File xml) throws ParserConfigurationException, IOException, SAXException {
        // 创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // 创建一个DocumentBuilder的对象
        DocumentBuilder db = dbf.newDocumentBuilder();
        // 通过DocumentBuilder的parse方法解析xml，获取一个Document对象
        Document document = db.parse(xml);
        /* 以上都是解析xml文件获取出xml的内容，一下开始解析xml的内容及Document对象 */
        // 获取所有的状态节点
        Element statusElement = (Element) document.getElementsByTagName("status").item(0);
        // 获取节点里的值
//        String status = statusElement.getFirstChild().getNodeValue();
        String status = statusElement.getTextContent();// 对于节点下只有值的节点可直接使用getTextContent方法进行获取值
        System.out.println(status);
    }

    public static void main(String[] arg) {
        File xml = new File("E:\\api.xml");
        try {
            XmlUtils.readXmlByDOM(xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
