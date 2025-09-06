package com.zlc.gulu.server.test;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class test {

    public static void main(String[] args) {
        // 输入和输出的 JSON 文件路径，根据实际情况修改
        String inputFilePath = "C:\\Users\\赵联城\\Desktop\\ro_ro.json";
        String outputFilePath = "C:\\Users\\赵联城\\Desktop\\ro_ro2.json";

        try {
            // 读取输入文件的内容
            String jsonContent = new String(Files.readAllBytes(Paths.get(inputFilePath)), StandardCharsets.UTF_8);
            // 解析 JSON 字符串
            JsonElement jsonElement = JsonParser.parseString(jsonContent);
            // 对 JSON 进行递归排序
            JsonElement sortedJson = sortJson(jsonElement);
            // 使用 Gson 格式化输出（pretty print）
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String sortedJsonString = gson.toJson(sortedJson);
            // 写入到输出文件
            Files.write(Paths.get(outputFilePath), sortedJsonString.getBytes(StandardCharsets.UTF_8));

            System.out.println("排序完成，输出文件：" + outputFilePath);
        } catch (IOException e) {
            System.err.println("文件读取或写入失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 递归排序 JSON 对象中的键
     *
     * @param element 要排序的 JsonElement
     * @return 排序后的 JsonElement
     */
    private static JsonElement sortJson(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject originalObject = element.getAsJsonObject();
            JsonObject sortedObject = new JsonObject();

            // 获取原始对象中所有的键
            Set<String> keySet = originalObject.keySet();
            // 复制到 List 中并排序
            List<String> keys = new ArrayList<>(keySet);
            Collections.sort(keys);

            // 遍历排序后的键，再次递归排序每个子元素
            for (String key : keys) {
                sortedObject.add(key, sortJson(originalObject.get(key)));
            }
            return sortedObject;
        } else if (element.isJsonArray()) {
            JsonArray originalArray = element.getAsJsonArray();
            JsonArray newArray = new JsonArray();
            // 数组内的每个元素也递归排序（如果数组内有对象的话）
            for (JsonElement child : originalArray) {
                newArray.add(sortJson(child));
            }
            return newArray;
        } else {
            // 对于基本类型数据直接返回
            return element;
        }
    }
}
