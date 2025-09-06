//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class Split {
//
//    /**
//     * 从原始数据中构建新的数据结构
//     * 原始数据结构：
//     * {
//     *   province: {
//     *     city: {
//     *       region: code数据 (数组或者单个字符串)
//     *     },
//     *     ...
//     *   },
//     *   ...
//     * }
//     *
//     * 目标数据结构：
//     * {
//     *   city: {
//     *     region: code数据
//     *   },
//     *   ...
//     * }
//     */
//    private static Map<String, Object> buildRegionCode(Map<String, Object> polandData) {
//        Map<String, Object> regionCode = new LinkedHashMap<>();
//        for (Map.Entry<String, Object> provinceEntry : polandData.entrySet()) {
//            Map<String, Object> cities = (Map<String, Object>) provinceEntry.getValue();
//            for (Map.Entry<String, Object> cityEntry : cities.entrySet()) {
//                String cityName = cityEntry.getKey();
//                Map<String, Object> regions = (Map<String, Object>) cityEntry.getValue();
//                if (!regionCode.containsKey(cityName)) {
//                    regionCode.put(cityName, new LinkedHashMap<>());
//                }
//                Map<String, Object> cityRegions = (Map<String, Object>) regionCode.get(cityName);
//                for (Map.Entry<String, Object> regionEntry : regions.entrySet()) {
//                    String regionName = regionEntry.getKey();
//                    Object codeData = regionEntry.getValue();
//                    cityRegions.put(regionName, codeData);
//                }
//            }
//        }
//        return regionCode;
//    }
//
//    /**
//     * 将最终的数据写入到新的 JS 文件中
//     */
//    private static void writeJsFile(Map<String, Object> regionCode, String outputPath, ObjectMapper mapper) throws IOException {
//        String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(regionCode);
//        String jsContent = "let regionCode = " + jsonStr + ";\nexport default regionCode;\n";
//        Files.write(Paths.get(outputPath), jsContent.getBytes("UTF-8"));
//    }
//
//
//    public static void main(String[] args) {
//        String inputFile = "C:\\Users\\赵联城\\Desktop\\pl_pl.js";
//        String outputFile = "C:\\Users\\赵联城\\Desktop\\pl_pl2.js";
//
//        try {
//            // 读取整个输入文件（你已经手动删除 let poland =）
//            String fileContent = new String(Files.readAllBytes(Paths.get(inputFile)), "UTF-8");
//
//            // 简单修复非标准 JSON：加 key 引号、去尾逗号
//            String fixedJson = fileContent
//                    .replaceAll("([a-zA-Z0-9_]+)\\s*:", "\"$1\":")
//                    .replaceAll(",\\s*}", "}")
//                    .replaceAll(",\\s*]", "]");
//
//            // 使用 Jackson 解析
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> polandData = mapper.readValue(fixedJson, new TypeReference<Map<String, Object>>() {});
//
//            // 构造目标结构
//            Map<String, Object> regionCode = buildRegionCode(polandData);
//
//            // 写入新文件
//            writeJsFile(regionCode, outputFile, mapper);
//
//            System.out.println("转换完成，已生成文件：" + outputFile);
//        } catch (Exception e) {
//            System.err.println("处理出现异常：" + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}

package com.zlc.gulu.server.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Split {

    public static void main(String[] args) {
        String inputFile = "C:\\Users\\赵联城\\Desktop\\pl_pl2.js";  // 第二个 JS 文件
        String outputDir = "C:\\Users\\赵联城\\Desktop\\cities";     // 输出目录

        try {
            String content = new String(Files.readAllBytes(Paths.get(inputFile)), "UTF-8");

            // 提取 JSON 内容（去掉 let regionCode = ...; export default）
            String jsonPart = content
                    .replaceFirst("let\\s+regionCode\\s*=\\s*", "")
                    .replaceFirst(";\\s*export\\s+default\\s+regionCode;\\s*", "")
                    .trim();

            // 解析 JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> regionCode = mapper.readValue(jsonPart, new TypeReference<Map<String, Object>>() {});

            // 创建输出文件夹
            Files.createDirectories(Paths.get(outputDir));

            // 拆分每个城市写成一个 JS 文件
            for (Map.Entry<String, Object> entry : regionCode.entrySet()) {
                String city = entry.getKey();
                Object data = entry.getValue();

                // 安全的文件名（避免特殊字符）
                String safeFileName = city.replaceAll("[\\\\/:*?\"<>|]", "_") + ".js";
                String outPath = outputDir + File.separator + safeFileName;

                String fileContent = "export default " +
                        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data) + ";\n";

                Files.write(Paths.get(outPath), fileContent.getBytes("UTF-8"));
            }

            System.out.println("成功生成城市 JS 文件，输出目录：" + outputDir);

        } catch (Exception e) {
            System.err.println("处理出错：" + e.getMessage());
            e.printStackTrace();
        }
    }
}

