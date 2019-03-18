package myh.com.imageloader.image;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Copyright (c) 2018 All Rights Reserved
 * <p>
 * 作者：马彦虎  Email：1184265546@qq.com
 * 创建时间： 2019/3/18.
 * 修改历史:
 * 修改日期         作者        版本        描述说明
 * </p>
 */

public class ImageGetFromHttp{
    
    public static Bitmap downloadBitmap(String urlStr, Context context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            if (urlConnection.getResponseCode() == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                Bitmap bitmap= ImageSizeUtil.getScaledBitmap(inputStream,urlStr,context);
                System.out.println(bitmap.getByteCount());
                return bitmap;
            } else {
                System.out.println("加载图片错误！");
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            System.out.println("加载图片错误！");
        } catch (IOException e) {
            System.out.println("加载图片错误！");
        }
        return null;
    }
}