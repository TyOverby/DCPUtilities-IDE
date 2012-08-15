package com.prealpha.util;

import java.io.*;

/**
 * User: Ty
 * Date: 8/9/12
 * Time: 8:41 PM
 */
public class FileUtilities {
    public static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder((int)file.length()*2);

        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            sb.append(strLine+"\n");
        }
        in.close();

        return sb.toString();
    }

    public static void writeFile(File file, String toWrite) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(toWrite);
    }
}
