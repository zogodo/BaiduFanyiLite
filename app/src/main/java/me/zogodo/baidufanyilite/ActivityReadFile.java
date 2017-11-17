package me.zogodo.baidufanyilite;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zogod on 17/2/22.
 */
public class ActivityReadFile
{
    public static String readTextFileFromRaw(Activity avtivity, int file_id) throws IOException
    {
        //从raw中读取文本文件
        InputStream stream = avtivity.getResources().openRawResource(file_id);
        InputStreamReader isReader = new InputStreamReader(stream, "UTF-8");
        BufferedReader reader = new BufferedReader(isReader);
        String file_string = "";
        String temp;
        while ((temp = reader.readLine()) != null)
        {
            file_string += temp.trim() + " ";
        }
        reader.close();
        return file_string;
    }
}
