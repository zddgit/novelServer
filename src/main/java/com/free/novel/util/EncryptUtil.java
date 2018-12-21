package com.free.novel.util;

/**
 *加解密工具类
 */
public class EncryptUtil {
    private static int gene = 0xDB6;
    private static  String key = "freeNovel@zdd";
    public static String encryptInt(int src){
        int _src = src ^ gene;
        String _ret = "b";
        if(_src < 0 ){
            _src = -_src;
            _ret = "e";
        }
        return Integer.toString(_src, 16) + _ret;
    }

    public static int decryptInt(String src){
        String _f =  src.substring(src.length() - 1 );
        int _src = Integer.parseInt(src.substring(0,src.length()-1), 16);
        if("e".equals(_f)){
            _src = -_src;
        }
        return _src ^ gene;
    }

    public static String encryptStr(String str){
        byte[] strbyte = str.getBytes();
        byte[] keybyte = key.getBytes();
        byte bt[] = {0,0};
        String retstr = "";
        for(int i = 0,j = 0; i < strbyte.length; i++){
            int ret = strbyte[i] ^ keybyte[j];
            bt[0] = (byte) (65+ret/26);
            bt[1] = (byte) (65+ret%26);

            String s = new String(bt);
            retstr += s;
            if(j == keybyte.length - 1){
                j = 0;
            }else {
                j++;
            }
        }

        return retstr;
    }

    public static String decryptStr(String str){
        byte[] strbyte = str.getBytes();
        byte[] keybyte = key.getBytes();
        String mstr = "";
        byte bt[] = {0,0};
        byte retbt[] = {0};
        for(int i = 0; i < strbyte.length/2; i++){
            bt[0] = (byte) ((byte) (strbyte[2*i]-65)*26);
            bt[1] =  ((byte) (strbyte[2*i + 1]-65));
            retbt[0] = (byte) (bt[0] + bt[1]);

            String s = new String(retbt);
            mstr += s;
        }

        strbyte = mstr.getBytes();
        String retstr = "";
        for(int i = 0 ,j = 0; i < strbyte.length; i++){
            retbt[0] = (byte) (strbyte[i] ^ keybyte[j]);

            String s = new String(retbt);
            retstr += s;

            if(j == keybyte.length -1){
                j = 0;
            }else {
                j++;
            }
        }
        return retstr;
    }

    public static void main(String[] args) {
        System.out.println(encryptStr("786"));
        System.out.println(decryptStr("DDCWDF"));
    }
}

