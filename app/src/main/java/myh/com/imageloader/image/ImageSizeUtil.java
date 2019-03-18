package myh.com.imageloader.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Copyright (c) 2018 All Rights Reserved
 * <p>
 * 作者：马彦虎  Email：1184265546@qq.com
 * 创建时间： 2019/3/18.
 * 修改历史:
 * 修改日期         作者        版本        描述说明
 * </p>
 */
public class ImageSizeUtil {
    
    public static final int MAX_WIDTH = 720;
    public static final int MAX_HEIGHT = 1280;
    private static ImageFileCache mImageFileCache;
    
    
    public void setImageSize(int maxWidth, int maxHeight){
        
    }
    //返回给我一个经过压缩的Bitmap
    private static Bitmap getScaledBitmap(String url) {
        //先读边
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        //由边获取bitmap的宽和高
      //  System.out.println("bitmapBounds  宽    " + options.outWidth + "  高：  " + options.outHeight);
        //计算宽和高的缩放倍数
        /*
        inSampleSize 只接受2的整数次幂
        如果我们传的数字不是2的整数次幂，那么会默认向下取最近的2的整数次幂
         */
        options.inSampleSize=  getSample(options.outWidth,options.outHeight);
        
        options.inJustDecodeBounds=false;
        Bitmap bitmap = BitmapFactory.decodeFile(url, options);
        System.out.println("占用内存--->"+bitmap.getByteCount());
        return  bitmap;
    }
    
    
    //返回给我一个经过压缩的Bitmap
    /*
        因为Inputstream只能读一遍
        而我们读边需要一遍，读取Bitmap真是全数据有需要一遍，这是2遍，那么Inputstream无法做到
        
        只是用一次Inputstream，把我们的网络资源下载保存到本地文件
        那么随后我们就可以直接使用BitmapFactory.decodeFile方法来读边，读数据了
     */
    public static Bitmap getScaledBitmap(InputStream inputStream, String url, Context context) {
        if (mImageFileCache==null) {
            mImageFileCache =new ImageFileCache(context);
        }
     
        String FilePath=mImageFileCache.saveBitmap(inputStream,url);
       
        return  getScaledBitmap(FilePath);
    }
    
    
    //获取缩放倍数
    public static int getSample(int width, int height) {
        int scale=1;
        while(true){
            if (width/scale>MAX_WIDTH||height/scale>MAX_HEIGHT){
                scale*=2;
            }else {
                break;
            }
            
        }
    
        System.out.println("缩放倍数:  "+scale);
        return scale;
    }
}
