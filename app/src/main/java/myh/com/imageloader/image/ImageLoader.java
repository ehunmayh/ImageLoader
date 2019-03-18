package myh.com.imageloader.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/5/8.
 */
public class ImageLoader {
    public static final String TAG="ImageLoader------";
    private static ImageLoader mImageLoader;
    private final Context mContext;
    private ImageMemoryCache mImageMemoryCache;
    private ImageFileCache mImageFileCache;
    private Handler mHandler=new Handler();
    
    private ImageLoader(Context context) {
        mImageMemoryCache = new ImageMemoryCache(context);
        mImageFileCache = new ImageFileCache(context);
        mContext = context;
    }
    
    public static ImageLoader getInstance(Context context) {
        if (mImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (mImageLoader == null) {
                    mImageLoader = new ImageLoader(context);
                }
            }
        }
        return mImageLoader;
        
    }
    
    public void display(final String url, final ImageView imageView) {
        Bitmap bitmap;
        bitmap = mImageMemoryCache.getBitmapFromCache(url);
        if (bitmap!=null){
            Log.e(TAG,"从内存加载");
            imageView.setImageBitmap(bitmap);
            return;
        }
        if (bitmap == null) {
            bitmap = mImageFileCache.getImage(url);
            if (bitmap!=null){
                Log.e(TAG,"从文件加载");
                mImageMemoryCache.addBitmapToCache(url,bitmap);
                imageView.setImageBitmap(bitmap);
                return;
            }
            if (bitmap == null) {
                new Thread(){
                    @Override
                    public void run() {
                        final Bitmap bitmap1 = ImageGetFromHttp.downloadBitmap(url,mContext);
                        mImageMemoryCache.addBitmapToCache(url,bitmap1);
//                        mImageFileCache.saveBitmap(bitmap1,url);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //主线程执行
                                imageView.setImageBitmap(bitmap1);
                                Log.e(TAG,"从网络加载");
        
                            }
                        });
                    }
                }.start();
            }
        }
    }
}
