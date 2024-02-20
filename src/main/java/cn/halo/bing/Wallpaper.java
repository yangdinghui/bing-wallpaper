package cn.halo.bing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @author yangdh
 * @date 2024/2/20 15:19
 */
public class Wallpaper {

    // BING API
    private static String BING_API = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&nc=1612409408851&pid=hp&FORM=BEHPTB&uhd=1&uhdwidth=3840&uhdheight=2160";

    private static String BING_URL = "https://cn.bing.com";

    public static void main(String[] args) throws IOException {

        String httpContent = HttpUtils.getHttpContent(BING_API);
        JSONObject jsonObject = JSON.parseObject(httpContent);
        JSONArray jsonArray = jsonObject.getJSONArray("images");

        // 图片地址
        String url = BING_URL + jsonArray.getJSONObject(0).get("url");
        url = url.substring(0, url.indexOf("&"));

        // 图片时间
        String enddate = (String) jsonArray.getJSONObject(0).get("enddate");

        // 图片版权
        String copyright = (String) jsonArray.getJSONObject(0).get("copyright");

        // 格式化为 MD 格式
        String text = String.format("%s | [%s](%s) ", enddate, copyright, url) + System.lineSeparator();
        System.out.println(text);

        // 写入 MD 文件
        Path path = Paths.get("README.md");
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        List<String> allLines = Files.readAllLines(path);
        if (allLines.isEmpty()) {
            allLines.add(text);
        } else {
            allLines.set(0, text);
        }
        Files.write(path, "## Bing Wallpaper".getBytes());
        Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        Files.write(path, allLines, StandardOpenOption.APPEND);
    }
}
