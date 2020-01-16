package com.sundata;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

public class WriteExcel {

	/**
	 * 写班级EXCEL
	 * 
	 * @param subjectMap
	 * @param dataList
	 * @param schoolClassSet
	 * @param exportExcelDir
	 */
	public static void writeClass(Map<String, String> subjectMap, List<Map<String, Object>> dataList,
			Set<String> schoolClassSet,String exportExcelDir) {
		String fileDir = exportExcelDir+"班级.xlsx";
		ExcelWriter writerClass = ExcelUtil.getWriter(fileDir, "班级");
		for (Map.Entry<String, String> entry : subjectMap.entrySet()) {
			String subject = entry.getKey();
			String mapValue = entry.getValue();
			String[] arr = mapValue.split("-");
			float youxiuScore = Float.parseFloat(arr[0]);
			float gaofenScore = Float.parseFloat(arr[1]);
			float jigeScore = Float.parseFloat(arr[2]);
			float difenScore = Float.parseFloat(arr[3]);

			// 写excel
			// 计算班级
			Map<String, String> headerAliasC = new LinkedHashMap<String, String>();
			headerAliasC.put("schoolName", "学校");
			headerAliasC.put("className", "班级");
			headerAliasC.put("studentCount", "实考人数");
			headerAliasC.put("avgScore", "平均分");
			headerAliasC.put("gaofenlv", "高分率");
			headerAliasC.put("youxiulv", "优秀率");
			headerAliasC.put("jigelv", "及格率");
			headerAliasC.put("difenlv", "低分率");
			writerClass.setHeaderAlias(headerAliasC);
			writerClass.merge(7, subject);
			List<Map<String, Object>> cList = StatisticUtil.statisticClass(dataList, schoolClassSet, subject,
					youxiuScore, gaofenScore, jigeScore, difenScore);
			writerClass.write(cList, true);
		}
		// 关闭writer，释放内存
		writerClass.close();
	}

	/**
	 * 写学校EXCEL
	 * 
	 * @param subjectMap
	 * @param dataList
	 * @param schoolSet
	 * @param exportExcelDir
	 */
	public static void writeSchool(Map<String, String> subjectMap, List<Map<String, Object>> dataList,
			Set<String> schoolSet,String exportExcelDir) {

		for (String str : schoolSet) {
			System.out.println(str);
			String fileDir = exportExcelDir+str+"-小题分析.xlsx";
			File file = new File(fileDir);
			deleteIfExists(file);
			ExcelWriter writerSchool = ExcelUtil.getWriter(fileDir, "小题");
			Map<String, String> headerAliasS = new LinkedHashMap<String, String>();
			headerAliasS.put("tihao", "题号");
			headerAliasS.put("manfen", "满分");
			headerAliasS.put("pingjvfen", "平均分");
			headerAliasS.put("defenlv", "得分率");
			headerAliasS.put("biaozhuncha", "标准差");
			writerSchool.setHeaderAlias(headerAliasS);
			List<Map<String, Object>> sList = StatisticUtil.statisticSchool(dataList, str);
			writerSchool.write(sList, true);
			// 关闭writer，释放内存
			writerSchool.close();
		}

	}

	/**
	 * 写总体EXCEL
	 * 
	 * @param subjectMap
	 * @param dataList
	 * @param exportExcelDir
	 */
	public static void writeTotal(Map<String, String> subjectMap, List<Map<String, Object>> dataList,String exportExcelDir,int headSize) {
		String fileDir = exportExcelDir+"总体小题分析.xlsx";
		File file = new File(fileDir);
		deleteIfExists(file);
		ExcelWriter writerQuanXian = ExcelUtil.getWriter(fileDir, "小题");
			// 写excel
			Map<String, String> headerAliasS = new LinkedHashMap<String, String>();
			headerAliasS.put("tihao", "题号");
			headerAliasS.put("manfen", "满分");
			headerAliasS.put("pingjvfen", "平均分");
			headerAliasS.put("defenlv", "得分率");
			headerAliasS.put("biaozhuncha", "标准差");
			writerQuanXian.setHeaderAlias(headerAliasS);
			List<Map<String, Object>> cList = StatisticUtil.statisticTotal(dataList,headSize);
			writerQuanXian.write(cList, true);
		// 关闭writer，释放内存
		writerQuanXian.close();
	}

	/**
	 * 写乡镇EXCEL
	 * 
	 * @param subjectMap
	 * @param dataList
	 * @param townSchool
	 * @param exportExcelDir
	 */
	public static void writeTown(Map<String, String> subjectMap, List<Map<String, Object>> dataList,
			Set<String> townSchool,String exportExcelDir) {
		String fileDir = exportExcelDir+"乡镇.xlsx";
		ExcelWriter writerXiangZhen = ExcelUtil.getWriter(fileDir, "乡镇");
		for (Map.Entry<String, String> entry : subjectMap.entrySet()) {
			String subject = entry.getKey();
			String mapValue = entry.getValue();
			String[] arr = mapValue.split("-");
			float youxiuScore = Float.parseFloat(arr[0]);
			float gaofenScore = Float.parseFloat(arr[1]);
			float jigeScore = Float.parseFloat(arr[2]);
			float difenScore = Float.parseFloat(arr[3]);

			// 写excel
			Map<String, String> headerAliasS = new LinkedHashMap<String, String>();
			headerAliasS.put("schoolName", "乡镇");
			headerAliasS.put("studentCount", "实考人数");
			headerAliasS.put("avgScore", "平均分");
			headerAliasS.put("gaofenlv", "高分率");
			headerAliasS.put("youxiulv", "优秀率");
			headerAliasS.put("jigelv", "及格率");
			headerAliasS.put("difenlv", "低分率");
			writerXiangZhen.setHeaderAlias(headerAliasS);
			writerXiangZhen.merge(6, subject);
			List<Map<String, Object>> cList = StatisticUtil.statisticTown(dataList, townSchool, subject,
					youxiuScore, gaofenScore, jigeScore, difenScore);
			writerXiangZhen.write(cList, true);
		}
		// 关闭writer，释放内存
		writerXiangZhen.close();
	}

	/**
	 * 写特定多个学校EXCEL
	 * 
	 * @param subjectMap
	 * @param dataList
	 * @param rangeSchool
	 * @param exportExcelDir
	 */
	public static void writeRangeSchool(Map<String, String> subjectMap, List<Map<String, Object>> dataList,
			Set<String> rangeSchool,String exportExcelDir) {
		String fileDir = exportExcelDir+"特定学校.xlsx";
		ExcelWriter writerXiangZhen = ExcelUtil.getWriter(fileDir, "乡镇");
		for (Map.Entry<String, String> entry : subjectMap.entrySet()) {
			String subject = entry.getKey();
			String mapValue = entry.getValue();
			String[] arr = mapValue.split("-");
			float youxiuScore = Float.parseFloat(arr[0]);
			float gaofenScore = Float.parseFloat(arr[1]);
			float jigeScore = Float.parseFloat(arr[2]);
			float difenScore = Float.parseFloat(arr[3]);

			// 写excel
			Map<String, String> headerAliasS = new LinkedHashMap<String, String>();
			headerAliasS.put("schoolName", "乡镇");
			headerAliasS.put("studentCount", "实考人数");
			headerAliasS.put("avgScore", "平均分");
			headerAliasS.put("gaofenlv", "高分率");
			headerAliasS.put("youxiulv", "优秀率");
			headerAliasS.put("jigelv", "及格率");
			headerAliasS.put("difenlv", "低分率");
			writerXiangZhen.setHeaderAlias(headerAliasS);
			writerXiangZhen.merge(6, subject);
			List<Map<String, Object>> cList = StatisticUtil.statisticRangeSchool(dataList, rangeSchool, subject,
					youxiuScore, gaofenScore, jigeScore, difenScore);
			writerXiangZhen.write(cList, true);
		}
		// 关闭writer，释放内存
		writerXiangZhen.close();
	}


	public static void deleteIfExists(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				if (!file.delete()) {
					System.out.print("@@@@@@@@@@@@@@@@@@@@@");
				}
			} else {
				File[] files = file.listFiles();
				if (files != null && files.length > 0) {
					for (File temp : files) {
						deleteIfExists(temp);
					}
				}
				if (!file.delete()) {
					System.out.print("@@@@@@@@@@@@@@@@@@@@@");
				}
			}
		}
	}
}
