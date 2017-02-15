package com.template.file;

import com.template.file.xml.XmlDomUtils;
import com.template.file.xml.XmlSaxUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by Cloud on 2017-01-06.
 */
public class XmlUtils {


    public static void main(String[] arg) {
        File xml = new File("E:\\api.xml");
        try {
//            XmlDomUtils.readXmlByDOM(xml);
            XmlSaxUtils.readXmlBySAX(xml);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
