package com.sundata;

import java.math.BigDecimal;
import java.util.*;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class StatisticUtil {

    private static Map<String,Double> fullScoreMap = new HashMap<String, Double>();
    /**
     * 计算班级
     *
     * @param dataList
     * @param schoolClassSet
     * @param subject
     * @param youxiuScore
     * @param gaofenScore
     * @param jigeScore
     * @param difenScore
     * @return
     */
    public static List<Map<String, Object>> statisticClass(List<Map<String, Object>> dataList,
                                                           Set<String> schoolClassSet, String subject, float youxiuScore, float gaofenScore, float jigeScore,
                                                           float difenScore) {
        List<Map<String, Object>> dList = new ArrayList<Map<String, Object>>();
        // 班级科目总人数
        Map<String, Integer> classSubjectCountMap = new HashMap<String, Integer>();
        // 班级科目总分数
        Map<String, Float> classSubjectTtotalScoreMap = new HashMap<String, Float>();
        // 班级科目优秀总人数
        float youxiu = youxiuScore;
        Map<String, Integer> youxiuCountMap = new HashMap<String, Integer>();
        // 班级科目高分总人数
        float gaofen = gaofenScore;
        Map<String, Integer> gaofenCountMap = new HashMap<String, Integer>();
        // 班级科目及格总人数
        float jige = jigeScore;
        Map<String, Integer> jigeCountMap = new HashMap<String, Integer>();
        // 班级科目低分总人数
        float difen = difenScore;
        Map<String, Integer> difenCountMap = new HashMap<String, Integer>();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            String schoolName = map.get("学校").toString();
            String className = map.get("班级").toString();
            float score = Float.parseFloat(map.get(subject).toString());

            if (StrUtil.isNotBlank(schoolName) && StrUtil.isNotEmpty(schoolName) && StrUtil.isNotBlank(className)
                    && StrUtil.isNotEmpty(className)) {
                String schoolClassName = schoolName + "-" + className;
                for (String str : schoolClassSet) {
                    // 算实考人数
                    int cCount = 0;
                    if (str.equals(schoolClassName)) {
                        if (classSubjectCountMap.get(schoolClassName) == null) {
                            cCount = 0;
                        } else {
                            cCount = classSubjectCountMap.get(schoolClassName);
                        }
                        if (score > 0f) {
                            classSubjectCountMap.put(schoolClassName, cCount + 1);
                        }

                        float scoreSum = 0f;
                        if (classSubjectTtotalScoreMap.get(schoolClassName) == null) {
                            scoreSum = 0f;
                        } else {
                            scoreSum = classSubjectTtotalScoreMap.get(schoolClassName);
                        }

                        // 算平均分
                        classSubjectTtotalScoreMap.put(schoolClassName, scoreSum + score);
                        // 算高分总人数
                        int gaofenCount = 0;
                        if (score >= gaofen) {
                            if (gaofenCountMap.get(schoolClassName) == null) {
                                gaofenCount = 0;
                            } else {
                                gaofenCount = gaofenCountMap.get(schoolClassName);
                            }
                            gaofenCountMap.put(schoolClassName, gaofenCount + 1);
                        }
                        // 算优秀总人数
                        int youxiuCount = 0;
                        if (score >= youxiu) {
                            if (youxiuCountMap.get(schoolClassName) == null) {
                                youxiuCount = 0;
                            } else {
                                youxiuCount = youxiuCountMap.get(schoolClassName);
                            }
                            youxiuCountMap.put(schoolClassName, youxiuCount + 1);
                        }
                        // 算及格总人数
                        int jigeCount = 0;
                        if (score >= jige) {
                            if (jigeCountMap.get(schoolClassName) == null) {
                                jigeCount = 0;
                            } else {
                                jigeCount = jigeCountMap.get(schoolClassName);
                            }
                            jigeCountMap.put(schoolClassName, jigeCount + 1);
                        }
                        // 算低分总人数
                        int difenCount = 0;
                        if (score <= difen && score > 0) {
                            if (difenCountMap.get(schoolClassName) == null) {
                                difenCount = 0;
                            } else {
                                difenCount = difenCountMap.get(schoolClassName);
                            }
                            difenCountMap.put(schoolClassName, difenCount + 1);
                        }

                    }

                }
            }

        }

        for (String str : schoolClassSet) {

            String[] arr = str.split("-");
            // 学校
            String schoolName = arr[0];
            // 班级
            String className = arr[1];
            // 实考人数
            int classCount = 0;
            if (classSubjectCountMap.get(str) == null) {
                classCount = 0;
            } else {
                classCount = classSubjectCountMap.get(str);
                // 平均分
                float aScore = 0f;
                if (classSubjectTtotalScoreMap.get(str) != null) {
                    aScore = (float) classSubjectTtotalScoreMap.get(str) / classCount;
                }
                float avgScore = new BigDecimal(aScore).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                // 高分率
                float glv = 0f;
                if (gaofenCountMap.get(str) != null) {
                    glv = ((float) gaofenCountMap.get(str) / classCount) * 100f;
                }
                float gaofenlv = new BigDecimal(glv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                // 优秀率
                float ylv = 0f;
                if (youxiuCountMap.get(str) != null) {
                    ylv = ((float) youxiuCountMap.get(str) / classCount) * 100f;
                }
                float youxiulv = new BigDecimal(ylv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                // 及格率
                float jlv = 0f;
                if (jigeCountMap.get(str) != null) {
                    jlv = ((float) jigeCountMap.get(str) / classCount) * 100f;
                }
                float jigelv = new BigDecimal(jlv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                // 低分率
                float dlv = 0f;
                if (difenCountMap.get(str) != null) {
                    dlv = ((float) difenCountMap.get(str) / classCount) * 100f;
                }
                float difenlv = new BigDecimal(dlv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

                Map<String, Object> row = new HashMap<String, Object>();
                row.put("schoolName", schoolName);
                row.put("className", className);
                row.put("studentCount", classCount);
                row.put("avgScore", avgScore);
                row.put("gaofenlv", gaofenlv);
                row.put("youxiulv", youxiulv);
                row.put("jigelv", jigelv);
                row.put("difenlv", difenlv);
                dList.add(row);
            }

        }

        return dList;
    }

    /**
     * 计算学校
     *
     * @param dataList
     * @param schoolSet
     * @param subject
     * @param youxiuScore
     * @param gaofenScore
     * @param jigeScore
     * @param difenScore
     * @return
     */
    public static List<Map<String, Object>> statisticSchool(List<Map<String, Object>> dataList, String schoolStr) {

        List<Map<String, Object>> dList = new ArrayList<Map<String, Object>>();

        List<String> quesitonHearList = new ArrayList<String>();
        Map<String, Object> head = dataList.get(0);
        int num = 0;
        for (String key : head.keySet()) {
            if (num >= 5) {
                quesitonHearList.add(key);
            }
            num++;
        }

        Map<String, List<Double>> questionGroup = new HashMap<String, List<Double>>();

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            String schoolName = map.get("学校").toString();

            if (StrUtil.isNotBlank(schoolName) && schoolName.equals(schoolStr)) {
                float totalScore = Float.parseFloat(map.get("总分").toString());
                if (totalScore == 0) {
                    continue;
                }
                for (int j = 0; j < quesitonHearList.size(); j++) {
                    String questionName = quesitonHearList.get(j);
                    double questionScore;
                    if(map.get(questionName).toString().equals("-")){
                        questionScore=0;
                    }else{
                        questionScore = Double.parseDouble(map.get(questionName).toString());
                    }
                    if (questionGroup.containsKey(questionName)) {
                        questionGroup.get(questionName).add(questionScore);
                    } else {
                        List<Double> firstScore = new ArrayList<Double>();
                        questionGroup.put(questionName, firstScore);
                    }
                }
            }
        }
        for (int k = 0; k < quesitonHearList.size(); k++) {
            String questionName = quesitonHearList.get(k);
            List<Double> scoreList = questionGroup.get(questionName);
            double[] arry = List2Array(scoreList);
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("tihao", questionName);
            double manfen = fullScoreMap.get(questionName);
            row.put("manfen", manfen);
            double pingjvfen = getMean(arry);
            row.put("pingjvfen", pingjvfen);
            row.put("defenlv", pingjvfen / (manfen==0?1:manfen));
            row.put("biaozhuncha", getStandardDevition(arry));
            dList.add(row);
        }

        return dList;

    }

    /**
     * 计算总体
     *
     * @param dataList
     * @param subject
     * @param youxiuScore
     * @param gaofenScore
     * @param jigeScore
     * @param difenScore
     * @return
     */
    public static List<Map<String, Object>> statisticTotal(List<Map<String, Object>> dataList, int headSize) {
        List<Map<String, Object>> dList = new ArrayList<Map<String, Object>>();

        List<String> quesitonHearList = new ArrayList<String>();
        Map<String, Object> head = dataList.get(0);
        int num = 0;
        for (String key : head.keySet()) {
            if (num >= 5) {
                quesitonHearList.add(key);
            }
            num++;
        }

        Map<String, List<Double>> questionGroup = new HashMap<String, List<Double>>();

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);

            float totalScore = Float.parseFloat(map.get("总分").toString());
            if (totalScore == 0) {
                continue;
            }
            for (int j = 0; j < quesitonHearList.size(); j++) {
                String questionName = quesitonHearList.get(j);
                double questionScore;
                if(map.get(questionName).toString().equals("-")){
                    questionScore = 0;
                }else{
                    questionScore = Double.parseDouble(map.get(questionName).toString());
                }
                if (questionGroup.containsKey(questionName)) {
                    questionGroup.get(questionName).add(questionScore);
                } else {
                    List<Double> firstScore = new ArrayList<Double>();
                    questionGroup.put(questionName, firstScore);
                }
            }
        }

        for (int k = 0; k < quesitonHearList.size(); k++) {
            String questionName = quesitonHearList.get(k);
            List<Double> scoreList = questionGroup.get(questionName);
            double[] arry = List2Array(scoreList);
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("tihao", questionName);
            double manfen = getMax(questionName,arry);
            fullScoreMap.put(questionName,manfen);
            row.put("manfen", manfen);
            double pingjvfen = getMean(arry);
            row.put("pingjvfen", pingjvfen);
            row.put("defenlv", pingjvfen / manfen);
            row.put("biaozhuncha", getStandardDevition(arry));
            dList.add(row);
        }

        return dList;

//		float difenlv = new BigDecimal(dlv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 计算特定多个学校
     *
     * @param dataList
     * @param rangeSchool
     * @param subject
     * @param youxiuScore
     * @param gaofenScore
     * @param jigeScore
     * @param difenScore
     * @return
     */
    public static List<Map<String, Object>> statisticRangeSchool(List<Map<String, Object>> dataList,
                                                                 Set<String> rangeSchool, String subject, float youxiuScore, float gaofenScore, float jigeScore,
                                                                 float difenScore) {
        List<Map<String, Object>> dList = new ArrayList<Map<String, Object>>();
        // 学校科目总人数
        Map<String, Integer> xiangZhenZhiSubjectCountMap = new HashMap<String, Integer>();
        // 学校科目总分数
        Map<String, Float> xiangZhenZhiSubjectTtotalScoreMap = new HashMap<String, Float>();
        // 学校科目优秀总人数
        float youxiu = youxiuScore;
        Map<String, Integer> youxiuCountMap = new HashMap<String, Integer>();
        // 学校科目高分总人数
        float gaofen = gaofenScore;
        Map<String, Integer> gaofenCountMap = new HashMap<String, Integer>();
        // 学校科目及格总人数
        float jige = jigeScore;
        Map<String, Integer> jigeCountMap = new HashMap<String, Integer>();
        // 学校科目低分总人数
        float difen = difenScore;
        Map<String, Integer> difenCountMap = new HashMap<String, Integer>();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            String schoolName = map.get("学校").toString();
            float score = Float.parseFloat(map.get(subject).toString());

            if (StrUtil.isNotBlank(schoolName) && StrUtil.isNotEmpty(schoolName)) {
                for (String str : rangeSchool) {
                    // 算实考人数
                    int cCount = 0;
                    if (str.equals(schoolName)) {
                        if (xiangZhenZhiSubjectCountMap.get(schoolName) == null) {
                            cCount = 0;
                        } else {
                            cCount = xiangZhenZhiSubjectCountMap.get(schoolName);
                        }
                        if (score > 0f) {
                            xiangZhenZhiSubjectCountMap.put(schoolName, cCount + 1);
                        }

                        float scoreSum = 0f;
                        if (xiangZhenZhiSubjectTtotalScoreMap.get(schoolName) == null) {
                            scoreSum = 0f;
                        } else {
                            scoreSum = xiangZhenZhiSubjectTtotalScoreMap.get(schoolName);
                        }

                        // 算平均分
                        xiangZhenZhiSubjectTtotalScoreMap.put(schoolName, scoreSum + score);
                        // 算高分总人数
                        int gaofenCount = 0;
                        if (score >= gaofen) {
                            if (gaofenCountMap.get(schoolName) == null) {
                                gaofenCount = 0;
                            } else {
                                gaofenCount = gaofenCountMap.get(schoolName);
                            }
                            gaofenCountMap.put(schoolName, gaofenCount + 1);
                        }
                        // 算优秀总人数
                        int youxiuCount = 0;
                        if (score >= youxiu) {
                            if (youxiuCountMap.get(schoolName) == null) {
                                youxiuCount = 0;
                            } else {
                                youxiuCount = youxiuCountMap.get(schoolName);
                            }
                            youxiuCountMap.put(schoolName, youxiuCount + 1);
                        }
                        // 算及格总人数
                        int jigeCount = 0;
                        if (score >= jige) {
                            if (jigeCountMap.get(schoolName) == null) {
                                jigeCount = 0;
                            } else {
                                jigeCount = jigeCountMap.get(schoolName);
                            }
                            jigeCountMap.put(schoolName, jigeCount + 1);
                        }
                        // 算低分总人数
                        int difenCount = 0;
                        if (score <= difen && score > 0) {
                            if (difenCountMap.get(schoolName) == null) {
                                difenCount = 0;
                            } else {
                                difenCount = difenCountMap.get(schoolName);
                            }
                            difenCountMap.put(schoolName, difenCount + 1);
                        }

                    }

                }
            }

        }

        for (String str : rangeSchool) {
            // 学校
            String schoolName = str;

            // 实考人数
            int schoolCount = xiangZhenZhiSubjectCountMap.get(str);
            // 平均分
            float aScore = 0f;
            if (xiangZhenZhiSubjectTtotalScoreMap.get(str) != null) {
                aScore = (float) xiangZhenZhiSubjectTtotalScoreMap.get(str) / schoolCount;
            }
            float avgScore = new BigDecimal(aScore).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            // 高分率
            float glv = 0f;
            if (gaofenCountMap.get(str) != null) {
                glv = ((float) gaofenCountMap.get(str) / schoolCount) * 100f;
            }
            float gaofenlv = new BigDecimal(glv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            // 优秀率
            float ylv = 0f;
            if (youxiuCountMap.get(str) != null) {
                ylv = ((float) youxiuCountMap.get(str) / schoolCount) * 100f;
            }
            float youxiulv = new BigDecimal(ylv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            // 及格率
            float jlv = 0f;
            if (jigeCountMap.get(str) != null) {
                jlv = ((float) jigeCountMap.get(str) / schoolCount) * 100f;
            }
            float jigelv = new BigDecimal(jlv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            // 低分率
            float dlv = 0f;
            if (difenCountMap.get(str) != null) {
                dlv = ((float) difenCountMap.get(str) / schoolCount) * 100f;
            }
            float difenlv = new BigDecimal(dlv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

            Map<String, Object> row = new HashMap<String, Object>();
            row.put("schoolName", schoolName);
            row.put("studentCount", schoolCount);
            row.put("avgScore", avgScore);
            row.put("gaofenlv", gaofenlv);
            row.put("youxiulv", youxiulv);
            row.put("jigelv", jigelv);
            row.put("difenlv", difenlv);
            dList.add(row);
        }

        return dList;
    }

    /**
     * 计算乡镇
     *
     * @param dataList
     * @param townSchool
     * @param subject
     * @param youxiuScore
     * @param gaofenScore
     * @param jigeScore
     * @param difenScore
     * @return
     */
    public static List<Map<String, Object>> statisticTown(List<Map<String, Object>> dataList,
                                                          Set<String> townSchool, String subject, float youxiuScore, float gaofenScore, float jigeScore,
                                                          float difenScore) {
        List<Map<String, Object>> dList = new ArrayList<Map<String, Object>>();
        // 学校科目总人数
        Map<String, Integer> xiangZhenSubjectCountMap = new HashMap<String, Integer>();
        // 学校科目总分数
        Map<String, Float> xiangZhenSubjectTtotalScoreMap = new HashMap<String, Float>();
        // 学校科目优秀总人数
        float youxiu = youxiuScore;
        Map<String, Integer> youxiuCountMap = new HashMap<String, Integer>();
        // 学校科目高分总人数
        float gaofen = gaofenScore;
        Map<String, Integer> gaofenCountMap = new HashMap<String, Integer>();
        // 学校科目及格总人数
        float jige = jigeScore;
        Map<String, Integer> jigeCountMap = new HashMap<String, Integer>();
        // 学校科目低分总人数
        float difen = difenScore;
        Map<String, Integer> difenCountMap = new HashMap<String, Integer>();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            String schoolName = map.get("学校").toString();
            float score = Float.parseFloat(map.get(subject).toString());

            if (StrUtil.isNotBlank(schoolName) && StrUtil.isNotEmpty(schoolName)) {
                for (String str : townSchool) {
                    // 算实考人数
                    int cCount = 0;
                    if (schoolName.contains(str)) {
                        if (xiangZhenSubjectCountMap.get(str) == null) {
                            cCount = 0;
                            xiangZhenSubjectCountMap.put(str, 0);
                        } else {
                            cCount = xiangZhenSubjectCountMap.get(str);
                        }
                        if (score > 0f) {
                            xiangZhenSubjectCountMap.put(str, cCount + 1);
                        }

                        float scoreSum = 0f;
                        if (xiangZhenSubjectTtotalScoreMap.get(str) == null) {
                            scoreSum = 0f;
                        } else {
                            scoreSum = xiangZhenSubjectTtotalScoreMap.get(str);
                        }

                        // 算平均分
                        xiangZhenSubjectTtotalScoreMap.put(str, scoreSum + score);
                        // 算高分总人数
                        int gaofenCount = 0;
                        if (score >= gaofen) {
                            if (gaofenCountMap.get(str) == null) {
                                gaofenCount = 0;
                            } else {
                                gaofenCount = gaofenCountMap.get(str);
                            }
                            gaofenCountMap.put(str, gaofenCount + 1);
                        }
                        // 算优秀总人数
                        int youxiuCount = 0;
                        if (score >= youxiu) {
                            if (youxiuCountMap.get(str) == null) {
                                youxiuCount = 0;
                            } else {
                                youxiuCount = youxiuCountMap.get(str);
                            }
                            youxiuCountMap.put(str, youxiuCount + 1);
                        }
                        // 算及格总人数
                        int jigeCount = 0;
                        if (score >= jige) {
                            if (jigeCountMap.get(str) == null) {
                                jigeCount = 0;
                            } else {
                                jigeCount = jigeCountMap.get(str);
                            }
                            jigeCountMap.put(str, jigeCount + 1);
                        }
                        // 算低分总人数
                        int difenCount = 0;
                        if (score <= difen && score > 0) {
                            if (difenCountMap.get(str) == null) {
                                difenCount = 0;
                            } else {
                                difenCount = difenCountMap.get(str);
                            }
                            difenCountMap.put(str, difenCount + 1);
                        }

                    }

                }
            }

        }

        for (String str : townSchool) {
            if (xiangZhenSubjectCountMap.get(str) != null) {
                // 学校
                String schoolName = str;

                // 实考人数
                int xiangZhenCount = xiangZhenSubjectCountMap.get(str);
                // 平均分
                float aScore = 0f;
                if (xiangZhenSubjectTtotalScoreMap.get(str) != null) {
                    aScore = (float) xiangZhenSubjectTtotalScoreMap.get(str) / xiangZhenCount;
                }
                float avgScore = new BigDecimal(aScore).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                // 高分率
                float glv = 0f;
                if (gaofenCountMap.get(str) != null) {
                    glv = ((float) gaofenCountMap.get(str) / xiangZhenCount) * 100f;
                }
                float gaofenlv = new BigDecimal(glv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                // 优秀率
                float ylv = 0f;
                if (youxiuCountMap.get(str) != null) {
                    ylv = ((float) youxiuCountMap.get(str) / xiangZhenCount) * 100f;
                }
                float youxiulv = new BigDecimal(ylv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                // 及格率
                float jlv = 0f;
                if (jigeCountMap.get(str) != null) {
                    jlv = ((float) jigeCountMap.get(str) / xiangZhenCount) * 100f;
                }
                float jigelv = new BigDecimal(jlv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                // 低分率
                float dlv = 0f;
                if (difenCountMap.get(str) != null) {
                    dlv = ((float) difenCountMap.get(str) / xiangZhenCount) * 100f;
                }
                float difenlv = new BigDecimal(dlv).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

                Map<String, Object> row = new HashMap<String, Object>();
                row.put("schoolName", schoolName);
                row.put("studentCount", xiangZhenCount);
                row.put("avgScore", avgScore);
                row.put("gaofenlv", gaofenlv);
                row.put("youxiulv", youxiulv);
                row.put("jigelv", jigelv);
                row.put("difenlv", difenlv);
                dList.add(row);
            }

        }

        return dList;
    }


    public static double[] List2Array(List<Double> list) {
        if(list==null||list.size()==0){
            return new double[]{};
        }
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }


    public static double getSum(double[] array) {
        double sum = 0;
        for (double num : array) {
            sum += num;
        }
        return sum;
    }

    /**
     * 求均值
     *
     * @param array
     * @return
     */
    public static double getMean(double[] array) {
        if(array==null||array.length==0)return 0;
        return getSum(array) / array.length;
    }

    public static double getMax(String questionName,double[] array) {
//        if("小题30".equals(questionName)){
//            return 7;
//        }
//        if("小题28".equals(questionName)){
//            return 12;
//        }
//        if("小题26".equals(questionName)){
//            return 10;
//        }
//        if("小题25".equals(questionName)){
//            return 10;
//        }
        if (array.length != 0) {
            Arrays.sort(array);
            return array[array.length - 1];
        } else {
            return 0;
        }
    }

    public static double getStandardDevition(double[] array) {
        if(array==null||array.length==0)return 0;
        StandardDeviation standardDeviation = new StandardDeviation();
        return standardDeviation.evaluate(array);
    }

}
