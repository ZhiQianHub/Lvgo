package com.bjlvgo.lvgo.utils.http.Cache;


import android.os.Environment;

import com.bjlvgo.lvgo.base.LvgoApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Chuxi on 2016/3/22.
 */
public class StringCache {

    private static StringCache cache = null;
    private final String STORAGE_DIR = "LvgoCache";
    private StringCache() {

    }

    public static StringCache newInstance() {

        if (cache == null) {
            synchronized (StringCache.class) {
                cache = new StringCache();
            }
        }
        return cache;
    }

    /**
     * 判断外部存储是否存在
     *
     * @return
     */
    private boolean existExternal() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            return true;
        }
        return false;
    }

    /**
     * 写入缓存目录文件（内存/外存）
     *
     * @param url
     * @return
     */
    private File writeFile(String url) {

        File fileCache = null;
        File dirCache = null;
        if (existExternal()) {
            dirCache = LvgoApplication.getContext().getExternalCacheDir();
        } else {
            dirCache = LvgoApplication.getContext().getCacheDir();
        }
        fileCache = new File(dirCache + STORAGE_DIR);
        if (!fileCache.exists()) {
            fileCache.mkdirs();
        }
        return new File(fileCache, url);

    }

    /**
     * 读取缓存的目录文件
     * @param url
     * @return
     */
    private File readFile(String url) {
        File fileCache = null;
        File dirCache = null;
        if (existExternal()) {
            dirCache = LvgoApplication.getContext().getExternalCacheDir();
        } else {
            dirCache = LvgoApplication.getContext().getCacheDir();
        }
        fileCache = new File(dirCache + STORAGE_DIR);
        return new File(fileCache, url);
    }

    /**
     * 将网络获取的数据写入缓存
     *
     * @param cacheString
     * @param url
     */
    public void writeCache(String cacheString, String url) {

        BufferedWriter outPut = null;
        File fileCache = writeFile(url);
        long lastTime = 0;
        if (fileCache.exists()) {
            lastTime = fileCache.lastModified();
        }
        if (System.currentTimeMillis() - lastTime > 5 * 1000) {
            try {
                outPut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileCache)));
                outPut.write(cacheString);
                outPut.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outPut != null) {
                    try {
                        outPut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 从缓存中读取数据
     *
     * @param url 访问网络的url
     * @return 返回字符串数据
     */
    public String readCache(String url) {

        BufferedReader input = null;
        File fileCache = readFile(url);
        StringBuffer stringB = null;
        long lastTime = 0;
        if (fileCache.exists()) {
            lastTime = fileCache.lastModified();
        } else {
            return null;
        }
        if (System.currentTimeMillis() - lastTime > 5 * 1000) {
            fileCache.delete();
            return null;
        } else {
            try {
                input = new BufferedReader(new InputStreamReader(new FileInputStream(fileCache)));
                stringB = new StringBuffer();
                byte[] data = new byte[1024];
                int length = -1;
                while (input.read() != -1) {
                    stringB.append(input.readLine());
                }
                return stringB.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
