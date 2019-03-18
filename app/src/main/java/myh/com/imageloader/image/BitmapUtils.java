package myh.com.imageloader.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import java.io.File;
import java.io.FileDescriptor;

/**
 * Copyright (c) 2018 All Rights Reserved
 * <p>
 * 作者：马彦虎  Email：1184265546@qq.com
 * 创建时间： 2019/3/18.
 * 修改历史:
 * 修改日期         作者        版本        描述说明
 * </p>
 *
 * Bitmap 的高效加载BitmapFactory.Options中的
 */
public class BitmapUtils {
    /**
     * Bitmap 压缩资源文件中的图片
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
  public static Bitmap decodeSampledBitmapFromResoure(Resources res, @DrawableRes int resId,int reqWidth,int reqHeight){
      BitmapFactory.Options options=new BitmapFactory.Options();
      //inJustDecodeBounds=true只会解析图片的原始宽高信息，不会真正的去加载图片
      options.inJustDecodeBounds=true;
      //decode原始流
      BitmapFactory.decodeResource(res,resId,options);
      //计算缩放比例
      options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
      //重新设置加载图片
      options.inJustDecodeBounds=false;
      return BitmapFactory.decodeResource(res,resId,options);

  }

    /**
     * 文件夹流
     * @param fileDescriptor  文件夹流
     * @param reqWidth
     * @param reqHeight
     * @return
     */
  public static Bitmap decodeSampledBitmapFileDescriptor(FileDescriptor fileDescriptor,int reqWidth,int reqHeight){
      BitmapFactory.Options options=new BitmapFactory.Options();
      //inJustDecodeBounds=true只会解析图片的原始宽高信息，不会真正的去加载图片
      options.inJustDecodeBounds=true;
      //第一次decode  加载原始图片数据
      BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
      //计算缩放比例
      options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
      //重新设置加载图片
      options.inJustDecodeBounds=false;
      return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
  }


    /**
     * Bitmap 压缩文件中的图片
     * @param file  压缩的文件
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(File file , int reqWidth, int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        //inJustDecodeBounds=true只会解析图片的原始宽高信息，不会真正的去加载图片
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(file.getAbsolutePath(),options);
        //计算缩放比例
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        //重新设置加载图片
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(),options);

    }

    /**
     * Bitmap 压缩文件中的图片
     * @param filePath  压缩的文件的路径
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath , int reqWidth, int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        //inJustDecodeBounds=true只会解析图片的原始宽高信息，不会真正的去加载图片
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(filePath,options);
        //计算缩放比例
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        //重新设置加载图片
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(filePath,options);

    }



    /**
     * 根据三个参数计算inSampleSize的值 等于1时不缩放  大于1时才有缩放效果
     * 建议inSampleSize的取值为2的指数次幂，或者取宽高比中的取小，保证缩放后的图片大小大于等于Imageview的期望大小
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int originWidth=options.outWidth;
        int originHeight=options.outHeight;
        int inSampleSize=1;//等于1时不缩放

        if (originWidth>reqWidth||originHeight>reqHeight){
            int halfHeight=originHeight/2;
            int halfWidth=originWidth/2;
            while (halfHeight/inSampleSize>=reqHeight&&(halfWidth/inSampleSize>=reqWidth)){
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }
}
