package com.example.yszm.learningword.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.yszm.learningword.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author 佐达.
 * on 2019/5/23 12:31
 */
//文件工具类
 public class   FileUtils {
    private FileUtils(){

    }
    //写入数据
    public static void writeData(Context context) {
        //准备写入的地址 data/user/0/com.example.yszm.learningword/app_databases /words.db
        String dbPath = context.getDir(Const.DB_DIR, Context.MODE_PRIVATE) + File.separator + Const.DB_NAME;
        File dbFile = new File(dbPath); //创建word.db文件

        if (!dbFile.exists()) { //如果目录文件不存在
            //储存word.db 数据
            InputStream inputStream = context.getResources().openRawResource(R.raw.words); //获取资源文件word.db
           //文件输出流
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(dbFile);
                int len;
                byte[] bytes = new byte[1024];
                while ((len = inputStream.read(bytes)) != -1) { //读到文件末尾
                    fileOutputStream.write(bytes, 0, len); //将数据写入 word.db。
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * 删除单个文件
     */
    public static Boolean deleteSingleFile(String filename) {
        File file = new File(filename);
        // 如果文件路径所对应的文件存在 则直接删除
        if (file.exists()) {
            if (file.delete()) {
                Log.d("MainActivity", "Copy_Delete.deleteSingleFile: 删除单个文件" + filename + "成功！");
           return true;
            } else {
                Log.d("MainActivity", "Copy_Delete.deleteSingleFile: 删除单个文件" + filename + "失败！");
                return false;

            }
        } else {
            Log.d("MainActivity", "Copy_Delete.deleteSingleFile: 删除不存在");
            return false;

        }
    }
    public static void firstWriteData(Context context) {
        //准备写入的地址 data/user/0/com.example.yszm.learningword/app_databases /words.db
        String dbPath = context.getDir(Const.DB_DIR, Context.MODE_PRIVATE) + File.separator + Const.DB_NAME;
        File dbFile = new File(dbPath); //创建word.db文件
            //储存word.db 数据
            InputStream inputStream = context.getResources().openRawResource(R.raw.words); //获取资源文件word.db
            //文件输出流
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(dbFile);
                int len;
                byte[] bytes = new byte[1024];
                while ((len = inputStream.read(bytes)) != -1) { //读到文件末尾
                    fileOutputStream.write(bytes, 0, len); //将数据写入 word.db。
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

    }
    public static void deleteDirectory(Context context) {

        String filePath = context.getDir(Const.DB_DIR, Context.MODE_PRIVATE)+"";
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "失败，不存在！");
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }

        }
        if (!flag) {
            Toast.makeText(context, "删除目录失败！", Toast.LENGTH_SHORT).show();
        }
        // 删除当前目录
        if (dirFile.delete()) {
            Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
        } else {
            Toast.makeText(context, "删除目录：" + filePath + "失败！", Toast.LENGTH_SHORT).show();
        }
    }



}
