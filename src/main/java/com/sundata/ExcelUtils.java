package com.sundata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

public class ExcelUtils {
	public static void main(String[] args) {
		//读总成绩
		ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("g://333/理科化学成绩明细.xlsx"));
		String exportExcelDir = "g:/高三小题分析/理科化学/";

		List<Map<String, Object>> dataList = reader.readAll();
		//封装学校、班级数据
		int headSize = dataList.get(0).size();
		Set<String> schoolSet = new HashSet<String>();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> map = dataList.get(i);
			String schoolName = map.get("学校").toString();
			if (StrUtil.isNotBlank(schoolName) || StrUtil.isNotEmpty(schoolName)) {
				schoolSet.add(schoolName);
			}

		}


		System.out.println("headSize="+headSize);

		//封装科目、科目优秀分、科目高分、科目及格分、科目低分
		Map<String, String> subjectMap = new HashMap<String, String>();
		// InitMap.put("总分", "840-945-630-315");// 1050
		// InitMap.put("语文", "88-99-66-33");// 110
		// InitMap.put("英语", "80-90-60-30");// 100
		// InitMap.put("数学", "80-90-60-30");// 100
		// InitMap.put("物理", "56-63-42-21");// 70
		// InitMap.put("化学", "40-45-30-15");// 50
		// InitMap.put("历史", "40-45-30-15");// 50
		// InitMap.put("道德与法治", "40-45-30-15");// 50
		// InitMap.put("地理", "40-45-30-15");// 50
		subjectMap.put("生物", "40-45-30-15");// 50
		// InitMap.put("化学", "80-90-60-30");// 100
		// InitMap.put("美术", "80-90-60-30");// 100
		
		//封装特定多个学校数据
		Set<String> rangeSchool = new HashSet<String>();
		rangeSchool.add("建始县官店镇民族小学");
		rangeSchool.add("建始县景阳镇民族小学");
		rangeSchool.add("建始县花坪镇民族小学");
		rangeSchool.add("建始县红岩寺镇民族小学");
		rangeSchool.add("建始县三里乡民族小学");
		rangeSchool.add("建始县高坪镇高坪小学");
		rangeSchool.add("建始县龙坪乡民族小学");
		rangeSchool.add("建始县茅田乡茅田小学");
		rangeSchool.add("建始县长梁镇民族小学");
		rangeSchool.add("建始县业州镇建始县民族小学");

		//封装乡镇数据
		Set<String> townSchool = new HashSet<String>();
		// xiangZhen.add("红岩寺镇");
		townSchool.add("济水中心校");
		townSchool.add("沁园中心校");
		townSchool.add("北海中心校");
		townSchool.add("天坛中心校");
		townSchool.add("玉泉中心校");
		townSchool.add("梨林中心校");
		townSchool.add("思礼中心校");
		townSchool.add("五龙口中心校");
		townSchool.add("轵城中心校");
		townSchool.add("克井中心校");
		townSchool.add("坡头中心校");
		townSchool.add("承留中心校");
		townSchool.add("大峪中心校");
		townSchool.add("王屋中心校");
		townSchool.add("邵原中心校");
		townSchool.add("下冶中心校");
		townSchool.add("太行路学校");
		townSchool.add("济渎路学校");
		townSchool.add("中原特钢子弟中学");
		townSchool.add("延庆外国语学校");
		townSchool.add("宇华实验学校");


		
		
		//写EXCEL
		WriteExcel.writeTotal(subjectMap, dataList,exportExcelDir,headSize);
		WriteExcel.writeSchool(subjectMap, dataList, schoolSet,exportExcelDir);

	}

}
