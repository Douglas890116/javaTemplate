package com.template.file.xml;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by Cloud on 2017-01-30.
 */
public class XmlSaxUtils {
    /* SAX，通过java自带的类进行xml的解析 */


    public static void readXmlBySAX(File xml) throws ParserConfigurationException, SAXException, IOException {
        // 获取一个SAXParserFactory的实例
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 通过factory获取SAXParser实例
        SAXParser parser = factory.newSAXParser();
        // 创建SAXParseHandler对象
        parser.parse(xml, new SaxHandler());
    }
}
