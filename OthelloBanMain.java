import java.io.Console;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;

public class OthelloBanMain {
    private static char [] banChar = new char[] {'●',' ','○'};
    private static byte [][] banme;
    private static int rcLen = 8;   // 盤のマスの数
    private static List<String> exitCmds = Arrays.asList("q", "quit", "exit", "bye");
    private static List<String> passCmds = Arrays.asList("!", "pass", "p");

    public static void main(String[] args) throws Exception {
        if (args.length == 1)
            rcLen = Integer.parseInt(args[0]);
            
        banme = new byte[rcLen][rcLen]; // マスをつくりました

        // ○●の初期配置
        if (rcLen > 3) {        
            int r0 = rcLen / 2 - 1;
            int r1 = r0 + 1;
            int c0 = rcLen / 2 - 1;
            int c1 = c0 + 1;
            banme[r0][c0] = -1;
            banme[r1][c1] = -1;
            banme[r0][c1] = 1;
            banme[r1][c0] = 1;
        }
        System.out.println("========= オセロ盤 =========");
        showCmdExample();
        
        int turn = 1;

        boolean conti = true;
        while (conti) {
            drawBanme(banme);            
            conti = inputCommand(turn);
            turn *= -1;
        }
        System.out.println("終了しました。");
    }
    
    private static boolean put(int r, int c, int v)
    {
        if (banme[r][c] != 0)
        {
           //System.console().printf("ここには置けません%n");
           return false;
        }
        
        byte[][] valset = new byte[rcLen][rcLen]; // ひっくり返すところ

        byte[][] conv = new byte[rcLen][rcLen]; // 掛け合わせ
        for ( int i = 0; i < rcLen; i++ )         
            for ( int j = 0; j < rcLen; j++ )
                conv[i][j] = (byte)(banme[i][j] * v);
        
        // 置く位置の周囲８方向に別の色の石があり、その方向の先に同じ色の石がある。
        // 同じ色の石に挟まれた間を反転して同じ色にする。        

    {   // 下方向の反転箇所を調べる
        int j = c;
        for ( int i = r+1; i < rcLen; i++ ) {
            if (conv[i][j] == 0) {
                break;
            } 
            else if (conv[i][j] == 1){
                for ( int k = i-1; k > r; k-- ) {
                    valset[k][j] = (byte)v;
                }
                break;
            }                 
        }
    }
    {   // 上方向の反転箇所を調べる
        int j = c;
        for ( int i = r-1; i >= 0; i-- ) {
            if (conv[i][j] == 0) {
                break;
            } 
            else if (conv[i][j] == 1){
                for ( int k = i+1; k < r; k++ ) {
                    valset[k][j] = (byte)v;
                }
                break;
            }                 
        }
    }
    
    {   // 右方向の反転箇所を調べる
        int i = r;
        for ( int j = c+1; j < rcLen; j++ ) {
            if (conv[i][j] == 0) {
                break;
            } 
            else if (conv[i][j] == 1){
                for ( int l = j-1; l > c; l-- ) {
                    valset[i][l] = (byte)v;
                }
                break;
            }                 
        }
    }
    {   // 左方向の反転箇所を調べる
        int i = r;
        for ( int j = c-1; j >= 0; j-- ) {
            if (conv[i][j] == 0) {
                break;
            } 
            else if (conv[i][j] == 1){
                for ( int l = j+1; l < c; l++ ) {
                    valset[i][l] = (byte)v;
                }
                break;
            }                 
        }
    }
    
    {   // 右下方向の反転箇所を調べる
        int i = r + 1;
        int j = c + 1;
        while ( i < rcLen && j < rcLen) {
            if (conv[i][j] == 0) {
                break;
            } 
            else if (conv[i][j] == 1){
                int k = i - 1;
                int l = j - 1;                
                while ( k > r && l > c) {
                    valset[k][l] = (byte)v;
                    k--;
                    l--;
                }
                break;
            }
            i++;
            j++; 
        }        
    }
    {   // 左上方向の反転箇所を調べる
        int i = r - 1;
        int j = c - 1;
        while (i >= 0 && j >= 0) {
            if (conv[i][j] == 0) {
                break;
            }
            else if (conv[i][j] == 1){
                int k = i + 1;
                int l = j + 1;
                while (k < r && l < c ) {
                    valset[k][l] = (byte)v;
                    k++;
                    l++;
                }
                break;
            }                 
            i--;
            j--;
        }
    }
    
    {   // 右上方向の反転箇所を調べる
        int i = r + 1;
        int j = c - 1;
        while (i < rcLen && j >= 0) {
            if (conv[i][j] == 0) {
                break;
            } 
            else if (conv[i][j] == 1){
                int k = i - 1;
                int l = j + 1;
                while ( k > r && l < c) {
                    valset[k][l] = (byte)v;
                    k--;
                    l++;
                }
                break;
            }
            i++;
            j--;      
        }        
    }
    {   // 左下方向の反転箇所を調べる
        int i = r - 1;
        int j = c + 1;
        while (i >= 0 && j < rcLen ) {
            if (conv[i][j] == 0) {
                break;
            } 
            else if (conv[i][j] == 1){
                int k = i + 1;
                int l = j - 1;
                while ( k < r && l > c) {
                    valset[k][l] = (byte)v;
                    k++;
                    l--;
                }
                break;
            }
            i--;
            j++;      
        }        
    }
        // drawBanme(conv);
        // drawBanme(valset);   // 変わったところ
    
        boolean any = false; // どこか反転する場所があったかを記憶するフラグ
        
        for ( int i = 0; i < rcLen; i++ )         
            for ( int j = 0; j < rcLen; j++ )
                if (valset[i][j] != 0) {
                    banme[i][j] = valset[i][j];
                    any = true;
                }
                
        if (any)  // 反転する場所がなかったらそこには置けない
            banme[r][c] = (byte)v;
        return any;
    }

    private static void showCmdExample()
    {
        System.console().printf("C:4 のように入力。 qで終了。 %n");
    }

    private static boolean inputCommand(int turn)
    {   
        Console con = System.console(); 
        boolean accepted = false;
        
        while ( !accepted ) {
            System.out.print(banChar[turn + 1] + "の番>");

            String command = con.readLine();
            if (exitCmds.contains(command) )return false;
            if (passCmds.contains(command) )return true;

            try {
             //   MessageFormat mf = new MessageFormat("{0},{1}={2}");
                MessageFormat mf = new MessageFormat("{0}:{1}");
                Object[] result = mf.parse(command);
                char r1 = (char)((String)result[0]).toUpperCase().charAt(0);
                int c1 = Integer.parseInt((String)result[1]);
//                int v1 = Integer.parseInt((String)result[2]);
                
                // if ( v1 < 0 ||  v1 > 1 ) {
                //     con.printf("=%dは範囲外です。%n", v1);
                //     showCmdExample();
                //     continue;
                // }
                //con.printf("%s,%dを%sに設定します%n", r1, c1, banChar[v]);

                int r = (int)r1 - (int)'A';
                int c = c1 - 1;
                int v = turn;    // == ) ? 1 : -1;
                accepted = put(r,c,v);
                
                if (!accepted)
                    con.printf("そこには置けません。%n");
                                   
            } catch (Exception ex)
            {
                con.printf("書式が違います。%n");
                showCmdExample();
            }
        }
        return true;
    }

    private static void drawBanme(byte[][] ban)
    {
        drawColumnLabel();
        System.out.println();

        int w = 0;
        int b = 0;
        for ( int r = 0; r < rcLen; r++ )
        {
            char A = (char)(((int)'A') + r );
            System.out.print(A);
            System.out.print(' ');
            for ( int c = 0; c < rcLen; c++ )
            {
                System.out.print(banChar[ban[r][c] + 1]);
                System.out.print(' ');
                                
                if (ban[r][c] < 0) {
                    b++;
                }
                else if (ban[r][c] > 0) {
                    w++;
                }
            }
            System.out.println(A);
        }
        drawColumnLabel();
        System.out.print("   ");
        System.out.print(banChar[0]);
        System.out.print(b);
        System.out.print(' ');
        System.out.print(banChar[2]);
        System.out.println(w);
    }
    private static void drawColumnLabel()
    {
        System.out.print("  ");
        for ( int c = 0; c < rcLen; c++ )
        {
            System.out.print(c+1);
            System.out.print(' ');
        }
    } 
}
