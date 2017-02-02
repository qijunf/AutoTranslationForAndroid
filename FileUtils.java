package com.qijunf.googletrans.translation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.DocumentException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qijunf on 2017/1/22.
 */
public class FileUtils {
	public static int DELETE_COUNT = 0;
	public static int INSERT_COUNT = 0;

	/**
	 * 
	 * @param file
	 *            需要添加内容的XML文件
	 * @param name
	 *            string字符串的名字即"name"属性
	 * @param content
	 *            string字符串的内容，即<string name="***">content</string>
	 * @throws IOException
	 */
	public static void appendXMLByDOM4J(File file, String name, String content) throws IOException {
		// 创建一个SAXReader对象reader
		SAXReader reader = new SAXReader();
		try {

			// 通过reader对象的read方法加载xml文件，获取Document对象
			Document document = reader.read(file);
			// System.out.print(file.getName());
			Element resource = document.getRootElement();// 通过document对象获取根节点bookstore
			List<Element> list = resource.elements();
			for (Element e : list) { // 如果已经有命名的字符串，不再追加字符串
				if (e.attribute(new QName("name")).getValue().equals(name)) {
					System.out.println("输入字符串在文件中" + file.getAbsoluteFile() + "已经存在,如有需要请删除后，重新执行命令");
					return;
				}
			}
			QName tempName = new QName("name");
			Element string = resource.addElement("string");// 添加一条string字符串
			Element tempContent = string.addAttribute(tempName, name);// 为string添加name属性
			// System.out.println("tempContent==" + tempContent == null);
			tempContent.addText(content); // 设置字符串的内容
			INSERT_COUNT++;
			System.out.println("路径：" + file.getAbsoluteFile());
			System.out.println("追加字符串：" + "<string name=" + name + ">" + content + "</string>");
			// 3.设置输出格式和输出流
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(document);// 将文档写入到输出流
			writer.flush();
			writer.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static void deleteStringFromXML(File file, String name) {
		SAXReader reader = new SAXReader();
		boolean flag = false;
		try {

			// 通过reader对象的read方法加载xml文件，获取Document对象
			Document document = reader.read(file);
			// System.out.print(file.getName());
			Element resource = document.getRootElement();// 通过document对象获取根节点bookstore
			List<Element> list = resource.elements();
			for (Element e : list) { // 如果已经有命名的字符串，不再追加字符串
				if (e.attribute(new QName("name")).getValue().equals(name)) {
					e.getParent().remove(e);
					flag = true;
					System.out.println("字符串" + name + "在文件中" + file.getAbsoluteFile() + "删除成功");
					DELETE_COUNT++;
					// return;
				}
			}
			if (!flag)
				System.out.println("没有可以删除的字符串");
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(document);// 将文档写入到输出流
			writer.flush();
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 将拿到的翻译结果写入各个文件夹对应的String文件中
	 * 
	 * @param file
	 * @param nameValue
	 * @param data
	 */

	public static void writeTranslateDataToStringXML(File file, String nameValue, HashMap<String, String> data) {
		File flist[] = file.listFiles();
		if (flist == null || flist.length == 0) {
			return;
		}
		for (File f : flist) {
			if (f.isDirectory()) {
				writeTranslateDataToStringXML(f, nameValue, data);

			} else {
				if ("strings.xml".equals(f.getName())) {
					try {
						if (f.getParentFile().getName().startsWith("values")
								&& (f.getParentFile().getName().length() == 9)) {
							if (data.containsKey(f.getParentFile().getName().substring(7))) {

								appendXMLByDOM4J(f, nameValue, data.get(f.getParentFile().getName().substring(7)));

							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void deleteStringFromAllXML(File file, String nameValue) {
		File flist[] = file.listFiles();
		if (flist == null || flist.length == 0) {
			return;
		}
		for (File f : flist) {
			if (f.isDirectory()) {
				deleteStringFromAllXML(f, nameValue);

			} else {
				if ("strings.xml".equals(f.getName())) {
					try {
						if (f.getParentFile().getName().startsWith("values")
								&& (f.getParentFile().getName().length() == 9)) {

							deleteStringFromXML(f, nameValue);

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 根据多语言的文件夹特征拿到所有需要翻译的语言 ：eg values-en 获取后面两位 en 即英语是需要翻译的语言
	 * 
	 * @param file
	 * @param language
	 */
	public static void getNeedTranslateLanguage(File file, ArrayList<String> language) {

		File flist[] = file.listFiles();

		if (flist == null || flist.length == 0) {
			return;
		}
		for (File f : flist) {
			if (f.isDirectory()) {
				if (f.getName().startsWith("values") && (f.getName().length() == 9)) {
					language.add(f.getName().substring(7));
					// System.out.println("=====" + f.getName().substring(7));
				}
				getNeedTranslateLanguage(f, language);
			}

		}

	}

}
