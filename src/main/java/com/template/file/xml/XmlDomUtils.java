package com.template.file.xml;

import jodd.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Cloud on 2017-01-30.
 */
public class XmlDomUtils {
    /* DOM，通过java自带的类进行xml的解析
      ┌─────────────────────────────────────────────────────────────────────────┐
      │                             常用节点类型说明                             │
      ├──────────┬──────────┬────────────────┬────────────────┬─────────────────┤
      │          │ NodeType │ Named Constant │nodeName's value│nodeValue's value│
      ├──────────┼──────────┼────────────────┼────────────────┼─────────────────┤
      │ Element  │     1    │  ELEMENT_NODE  │  element name  │      null       │
      ├──────────┼──────────┼────────────────┼────────────────┼─────────────────┤
      │   Attr   │     2    │ ATTRIVUTE_NODE │ attrivute name │ attrivute value │
      ├──────────┼──────────┼────────────────┼────────────────┼─────────────────┤
      │   Text   │     3    │   TEXT_NODE    │      #text     │  note content   │
      └──────────┴──────────┴────────────────┴────────────────┴─────────────────┘
    */
    /**
     * 采用jdk自带的DOM类进行读取
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
        Element status_ = (Element) document.getElementsByTagName("status").item(0);
        // 获取节点里的值
//        String status = status_.getFirstChild().getNodeValue();
        String status = status_.getTextContent().trim();// 对于节点下只有值的节点可直接使用getTextContent方法进行获取值
        if (StringUtil.isBlank(status) || !status.equals("0")) return;
        // 获取table标签
        NodeList tables = document.getElementsByTagName("table");
        if (null != tables && tables.getLength() > 0) {
            NamedNodeMap attributes = null;
            NodeList rows = null;
            NodeList values = null;
            String name = null;
            for (int i = 0; i < tables.getLength(); i++) {
                attributes = tables.item(i).getAttributes();
                name = attributes.getNamedItem("name").getNodeValue();
                if (name == null) {
                    continue;
                } else if (name.equals("deptcode")) {
                    System.out.println("==========下面开始读取部门数据==========");
                    rows = tables.item(i).getChildNodes();
                    if (null != rows) {
                        for (int j = 0; j < rows.getLength(); j++) {
                            values = rows.item(j).getChildNodes();
                            if (null != values) {
                                for (int k = 0; k < values.getLength(); k++) {
                                    attributes = values.item(k).getAttributes();
                                    if (null != attributes && null != attributes.getNamedItem("name")) {
                                        name = attributes.getNamedItem("name").getNodeValue();
                                        if (null == name) {
                                            continue;
                                        } else if (name.equals("GRADE")) {
                                            System.out.println("部门 " + j + " 的 GRADE 是: " + values.item(k).getTextContent());
                                        } else if (name.equals("DEPT_CODE")) {
                                            System.out.println("部门 " + j + " 的DEPT_CODE是: " + values.item(k).getTextContent());
                                        } else if (name.equals("DEPT_ID")) {
                                            System.out.println("部门 " + j + " 的DEPT_ID是: " + values.item(k).getTextContent());
                                        } else if (name.equals("PARENT")) {
                                            System.out.println("部门 " + j + " 的PARENT是: " + values.item(k).getTextContent());
                                        } else if (name.equals("CONTENT")) {
                                            System.out.println("部门 " + j + " 的CONTENT是: " + values.item(k).getTextContent());
                                        }
                                    }
                                }
                            }
                        }
                        System.out.println("----------部 门 数据 读取结 束----------");
                    }
                } else if (name.equals("employee")) {
                    System.out.println("==========下面开始读取员工数据==========");
                }
            }
        }
    }
}
