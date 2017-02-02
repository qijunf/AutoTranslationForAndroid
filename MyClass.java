package com.qijunf.googletrans.translation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qijunf on 2017/1/22.
 */
public class MyClass {
	private static ArrayList<String> mLanguage = new ArrayList<String>();
	public static boolean DELETE = false;

	public static void main(String args[]) {
		System.out.println("******此jar包用来将任意语言通过google翻译自动转换为各种语言，并且写入语言文件夹下的strings.xml中,插入有错误支持回退操作******");

		mLanguage.clear();
		if (args.length == 3) {
			if (args[0].contentEquals("delete")) {
				FileUtils.getNeedTranslateLanguage(new File(args[0]), mLanguage);
				FileUtils.deleteStringFromAllXML(new File(args[1]), args[2]);
				System.out.println("删除字串" + FileUtils.DELETE_COUNT + "处");
			} else {
				System.out.println("字符串翻译中..........");
				FileUtils.getNeedTranslateLanguage(new File(args[0]), mLanguage);
				FileUtils.writeTranslateDataToStringXML(new File(args[0]), args[1],
						getTranslateResult(args[2], "auto", mLanguage));
				System.out.println("插入字串" + FileUtils.INSERT_COUNT + "处");
			}
		} else {
			System.out.println("请输入3个有效参数");
		}
	}

	/**
	 * 翻译单种语言并返回结果
	 * 
	 * @param translate
	 * @param src
	 * @param target
	 * @return
	 */

	public static String translate(String translate, String src, String target) {
		String html = Google.translate(translate, src, target);
		if (html == null) {
			return null;
		} else {
			// System.out.println("【返回数据】" + html);
			// 字符串函数解析 .也可以用json-lib
			String s = WebRequest.mid(html, "[[[", "]]");
			String[] ss = s.split(",");
			// System.out.println("【翻译结果】"+ "原语言："+ss[1] + " 翻译后：" + ss[0]);
			return ss[0];

		}
	}

	/**
	 * 将获取到的语言翻译结果保存在HashMap中，便于后去插入Strings.xml中
	 * 
	 * @param content
	 * @param srcLang
	 * @param trgLang
	 * @return
	 */
	public static HashMap<String, String> getTranslateResult(String content, String srcLang,
			ArrayList<String> trgLang) {
		HashMap<String, String> temp = new HashMap<String, String>();
		for (int i = 0; i < trgLang.size(); i++) {
			// System.out.println(trgLang.get(i)+ "===targLang");
			if (translate(content, srcLang, trgLang.get(i)) == null)
				System.out.println("翻译失败==" + Google.getKey((HashMap) Google.LANGUAGE, trgLang.get(i)));
			temp.put(trgLang.get(i), translate(content, srcLang, trgLang.get(i)));
		}
		System.out.println("翻译结果：" + temp.toString());
		if (temp.size() == 0) {
			System.out.println("你输入的路径不包含语言文件夹");
		}
		return temp;
	}

}
