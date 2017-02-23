package com.template.file.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Cloud on 2017-01-30.
 */
public class SaxHandler extends DefaultHandler {
    /**
     * 重写构造方法
     */
    public SaxHandler() {
        super();
    }

    /**
     * 用于遍历xml文件的开始标签
     * @param uri        文件的路径
     * @param localName  本地名
     * @param qName      标签名
     * @param attributes 属性名
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("books")) {
            // 在已知元素属性名称的情况下，获取元素的属性值
            String id = attributes.getValue("id");
            System.out.println("book的属性id的值是：" + id);
            // 不知道元素属性名称的情况下，获取元素的属性值
            // 先获取属性的个数
            int num = attributes.getLength();
            for (int i = 0; i < num; i++) {
                System.out.println("获取的属性名叫：" + attributes.getQName(i));
                System.out.println("其属性的值为" + attributes.getValue(i));// 等价于attributes.getValue(attributes.getQName(i))

            }
        }
    }

    /**
     * 获取标签内的值
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String value = new String(ch, start, length);
        if (!value.trim().equals("")) {
            // 将空格换行都去掉后再进行下一步操作
        }
    }

    /**
     * 用于标识解析开始
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("SAX解析xml开始.");
    }

    /**
     * 用于遍历xml文件的结束标签
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    /**
     * 用于标识解析结束
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("SAX解析xml结束.");
    }
}
