package myh.com.imageloader.image;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Set;

/*
    第一级 内存
    
    LRU::最近最少使用算法
    
     我们的cache有上限 ，那么当我们达到存储上限之后，还要加载新的图片，该怎么办？
     
     把最近最少使用的缓存图片移除掉
     
     内部是LinkedHashMap  那么存放的就是KV   k:一个和bitmap的url相关的数据     v:bitmap对象 
     
     内存的具体存放又分了俩地方
     
         强引用内存 
         
         软引用内存   
 */
public class ImageMemoryCache {
    /**
     * 从内存读取数据速度是最快的，为了更大限度使用内存，这里使用了两层缓存。
     * 强引用缓存不会轻易被回收，用来保存常用数据，不常用的转入软引用缓存。
     */
    private static final int SOFT_CACHE_SIZE = 15;  //软引用缓存容量
    
    private static LruCache<String, Bitmap> mLruCache;  //强引用缓存
    
    private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;  //软引用缓存
    
    public ImageMemoryCache(Context context) {
        
        //获取当应用前最大可用内存
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 4;  //强引用缓存容量，为系统可用内存的1/4
        //实例化强应用内存LruCache对象，构造器中传入该对象可以占用的最大内存数
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            //计算Bitmap的大小,获取新增图片的内存占有量
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (value != null)
                    return value.getRowBytes() * value.getHeight();
                else
                    return 0;
            }
            
            // 强引用缓存容量满的时候，会根据LRU算法把最近没有被使用的图片转入此软引用缓存
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (oldValue != null)
                    mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
            }
        };
        //实例化软引用LinkedHashMap对象
        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_SIZE, 0.75f, false) {
            private static final long serialVersionUID = 6040103833179403725L;
            
            //移除最老使用的节点---LRU
            @Override
            protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
                if (size() > SOFT_CACHE_SIZE) {
                    SoftReference<Bitmap> softReference = eldest.getValue();
                    Bitmap bitmap = softReference.get();
                    bitmap.recycle();
                    bitmap = null;
                    System.gc();
                    return true;
                }
                return false;
            }
        };
    }
    
    /**
     * 从内存中获取图片
     */
    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap;
        //先从强引用缓存中获取
        bitmap = mLruCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }
        
        
        //如果强引用缓存中找不到，到软引用缓存中找
        SoftReference<Bitmap> bitmapReference = mSoftCache.get(url);
        if (bitmapReference != null) {
            
            bitmap = bitmapReference.get();
            
            if (bitmap != null) {
                //要使用的图片，我们都放到强引用，将图片移回强缓存
                mLruCache.put(url, bitmap);
                mSoftCache.remove(url);
                return bitmap;
            } 
        }
        return null;
    }
    
    /**
     * 添加图片到缓存
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (mLruCache) {
                mLruCache.put(url, bitmap);
            }
        }
    }
    
    /*
        清除全部内存缓存
     */
    public void clearCache() {
        mLruCache.evictAll();
        Set<String> keys = mSoftCache.keySet();
        for (String key : keys) {
            SoftReference<Bitmap> bitmapSoftReference = mSoftCache.get(key);
            bitmapSoftReference.get().recycle();
        }
        mSoftCache.clear();
        System.gc();
    }
}