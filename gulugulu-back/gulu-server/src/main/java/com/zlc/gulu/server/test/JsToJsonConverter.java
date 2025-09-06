package com.zlc.gulu.server.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class JsToJsonConverter {

    public static void main(String[] args) throws IOException {
        // 输入文件夹路径（含 .js 文件）
        String inputFolderPath = "C:\\Users\\赵联城\\Desktop\\post_code";

        // 输出文件夹路径（用于保存 .json 文件）
        String outputFolderPath = "C:\\Users\\赵联城\\Desktop\\converted_json";

        File inputFolder = new File(inputFolderPath);
        File outputFolder = new File(outputFolderPath);

        // 创建输出文件夹（如果不存在）
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        if (!inputFolder.exists() || !inputFolder.isDirectory()) {
            System.out.println("输入路径无效：" + inputFolderPath);
            return;
        }

        File[] jsFiles = inputFolder.listFiles((dir, name) -> name.endsWith(".js"));
        if (jsFiles == null || jsFiles.length == 0) {
            System.out.println("未找到任何 .js 文件");
            return;
        }

        for (File jsFile : jsFiles) {
            convertToJson(jsFile, outputFolderPath);
        }

        System.out.println("转换完成！");
    }

    private static void convertToJson(File jsFile, String outputFolderPath) {
        try {
            // 读取内容
            String content = new String(Files.readAllBytes(jsFile.toPath()), StandardCharsets.UTF_8);

            // 去掉开头的 "export default"
            content = content.trim();
            if (content.startsWith("export default")) {
                content = content.replaceFirst("export\\s+default", "").trim();
            }

            // 去掉结尾的分号
            if (content.endsWith(";")) {
                content = content.substring(0, content.length() - 1).trim();
            }

            // 输出文件路径（保持文件名不变，只改后缀）
            String jsonFileName = jsFile.getName().replaceAll("\\.js$", ".json");
            Path newPath = Paths.get(outputFolderPath, jsonFileName);

            // 写入 json 文件
            Files.write(newPath, content.getBytes(StandardCharsets.UTF_8));

            System.out.println("转换成功：" + jsFile.getName() + " -> " + jsonFileName);
        } catch (IOException e) {
            System.err.println("转换失败：" + jsFile.getName());
            e.printStackTrace();
        }
    }
}

