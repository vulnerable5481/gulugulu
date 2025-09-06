package com.zlc.gulu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ModiTest {

    /**
     * 读取 JSON 文件，提取 data 数组中每个对象的 accountId，
     * 并以逗号拼接成一个字符串，写入到指定输出文件。
     *
     * @param jsonFilePath   输入 JSON 文件路径（包含 data 数组）
     * @param outputFilePath 输出结果文件路径（会被覆盖或新建）
     * @throws IOException 文件读写或解析异常
     */
    public static void extractAndWriteToFile(String jsonFilePath, String outputFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // 1. 读取并解析 JSON
        JsonNode root = mapper.readTree(new File(jsonFilePath));
        JsonNode dataArray = root.get("data");
        if (dataArray == null || !dataArray.isArray()) {
            throw new IOException("JSON 中未找到 data 数组或格式不正确");
        }
        // 2. 提取 accountId 并拼接
        String joinedIds = StreamSupport.stream(dataArray.spliterator(), false)
                .map(node -> node.path("accountId").asText(""))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));

        // 3. 将结果写入文件
        Files.write(
                Paths.get(outputFilePath),
                joinedIds.getBytes(StandardCharsets.UTF_8)
        );
        System.out.println("已将所有 accountId 写入：" + outputFilePath);
    }

    public static void main(String[] args) {
        try {
            extractAndWriteToFile("C:\\Users\\赵联城\\Desktop\\新建文本文档.json", "C:\\Users\\赵联城\\Desktop\\新建文本文档2.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("处理失败：" + e.getMessage());
        }
    }
}


