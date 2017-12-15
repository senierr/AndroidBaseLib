package com.senierr.utils.diskcache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 磁盘缓存工具类
 *
 * @author zhouchunjie
 * @date 2017/8/15
 */

public class DiskCacheUtil {

    private static final String TAG = DiskCacheUtil.class.getSimpleName();
    // 最大缓存字节
    private static final int MAX_SIZE = 10 * 1024 * 1024;
    // 默认应用版本号
    private static final int DEFAULT_APP_VERSION = 1;
    // 默认KEY对应数据数量
    private static final int DEFAULT_VALUE_COUNT = 1;

    private DiskLruCache diskLruCache;

    public DiskCacheUtil(DiskLruCache diskLruCache) {
        this.diskLruCache = diskLruCache;
    }

    public static DiskCacheUtil open(File dir, int appVersion, long maxSize) {
        DiskLruCache diskLruCache = null;
        try {
            diskLruCache = DiskLruCache.open(dir, appVersion, DEFAULT_VALUE_COUNT, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (diskLruCache != null) {
            return new DiskCacheUtil(diskLruCache);
        } else {
            return null;
        }
    }

    public static DiskCacheUtil open(Context context, File dir, long maxSize) {
        DiskLruCache diskLruCache = null;
        try {
            diskLruCache = DiskLruCache.open(dir, getAppVersion(context), DEFAULT_VALUE_COUNT, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (diskLruCache != null) {
            return new DiskCacheUtil(diskLruCache);
        } else {
            return null;
        }
    }

    public static DiskCacheUtil open(Context context, File dir) {
        DiskLruCache diskLruCache = null;
        try {
            diskLruCache = DiskLruCache.open(dir, getAppVersion(context), DEFAULT_VALUE_COUNT, MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (diskLruCache != null) {
            return new DiskCacheUtil(diskLruCache);
        } else {
            return null;
        }
    }

    /**
     * 存String
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        DiskLruCache.Editor edit = getEditor(key);
        if (edit == null) {
            return;
        }

        BufferedWriter bw = null;
        try {
            OutputStream os = edit.newOutputStream(0);
            bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(value);
            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                edit.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取String
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        InputStream inputStream = getInputStream(key);
        if (inputStream == null) {
            return null;
        }

        String value = null;
        try {
            value = Util.readFully(new InputStreamReader(inputStream, Util.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     * 存byte[]
     *
     * @param key
     * @param value
     */
    public void put(String key, byte[] value) {
        DiskLruCache.Editor edit = getEditor(key);
        if (edit == null) {
            return;
        }
        OutputStream os = null;
        try {
            os = edit.newOutputStream(0);
            os.write(value);
            os.flush();
            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                edit.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取byte[]
     *
     * @param key
     * @return
     */
    public byte[] getBytes(String key) {
        InputStream is = getInputStream(key);
        if (is == null) {
            return null;
        }

        byte[] res = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[256];
            int len;
            while ((len = is.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            res = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 存Serializable
     *
     * @param key
     * @param value
     */
    public void put(String key, Serializable value) {
        DiskLruCache.Editor edit = getEditor(key);
        if (edit == null) {
            return;
        }

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(edit.newOutputStream(0));
            oos.writeObject(value);
            oos.flush();
            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                edit.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取Serializable
     *
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getSerializable(String key) {
        InputStream is = getInputStream(key);
        if (is == null) {
            return null;
        }

        T t = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(is);
            try {
                t = (T) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    /**
     * 获取Editor
     *
     * @param key
     * @return
     */
    public DiskLruCache.Editor getEditor(String key) {
        DiskLruCache.Editor edit = null;
        try {
            key = hashKeyForDisk(key);
            edit = diskLruCache.edit(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return edit;
    }

    /**
     * 获取InputStream
     *
     * @param key
     * @return
     */
    public InputStream getInputStream(String key) {
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(hashKeyForDisk(key));
            if (snapshot == null) {
                return null;
            }
            return snapshot.getInputStream(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除一条记录
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            key = hashKeyForDisk(key);
            return diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() {
        try {
            diskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        try {
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return diskLruCache.isClosed();
    }

    public long size() {
        return diskLruCache.size();
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    private static int getAppVersion(Context context) {
        if (context != null) {
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return DEFAULT_APP_VERSION;
    }

    /**
     * 字符串转哈希值
     *
     * @param key
     * @return
     */
    private static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytes2HexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制小写字符串
     */
    private static String bytes2HexString(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }
}
