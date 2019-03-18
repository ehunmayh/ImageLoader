package myh.com.imageloader.image;

import android.os.StatFs;

/**
 * Created by SJ on 2016/1/27.
 */
public class SDUtil {

    public static boolean canUseSDCard(long size){
        if (getExternalStoragePath()==null){
            return  false;
        }
        if(getAvailableStore(getExternalStoragePath())>=size){
            return  true;
        }
        return  false;
    }

    // 获取SD卡路径
    public static String getExternalStoragePath() {
        // 获取SdCard状态
        String state = android.os.Environment.getExternalStorageState();

        // 判断SdCard是否存在并且是可用的

        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {

            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {

                return android.os.Environment.getExternalStorageDirectory()
                        .getPath();

            }

        }
        return null;

    }


    /**
     * 　　* 获取存储卡的剩余容量，单位为字节
     *
     * 　　* @param filePath
     *
     * 　　* @return availableSpare
     *
     */
    public static long getAvailableStore(String filePath) {

        // 取得sdcard文件路径

        StatFs statFs = new StatFs(filePath);

        // 获取block的SIZE

        long blocSize = statFs.getBlockSize();

        // 获取BLOCK数量

        // long totalBlocks = statFs.getBlockCount();

        // 可使用的Block的数量
        long availaBlock = statFs.getAvailableBlocks();

        // long total = totalBlocks * blocSize;

        long availableSpare = availaBlock * blocSize;

        return availableSpare;

    }
}
