import java.io.Console;
import java.text.MessageFormat;
import java.util.*;

/***
 * オセロ盤
 * マス目に置いてあるコマの状態を -1:黒　0:なし  1:白 で表します。
 */
public class OthelloBan {
    // コマの表示文字　黒 なし　白
    private  char [] komaChar = new char[] {'●',' ','○'};
    private  byte [][] banmen;
    public static final int RowColLen = 8;
    private  int rcLen = RowColLen;   // 盤の１辺のマスの数
    private  List<String> exitCmds = Arrays.asList("q", "quit", "exit", "bye");
    private  List<String> passCmds = Arrays.asList("!", "pass", "p");

    /**
     * @param len 盤の大きさを１辺のマス目の数で指定　4~9の数値　未指定時は8
     */
    public  void start(int len) {
        
        rcLen = len;
        // 2次元配列でマスを作る    
        banmen = new byte[rcLen][rcLen];

        // ○●の初期配置
        int r0 = rcLen / 2 - 1;
        int r1 = r0 + 1;
        int c0 = rcLen / 2 - 1;
        int c1 = c0 + 1;
        banmen[r0][c0] = -1;
        banmen[r1][c1] = -1;
        banmen[r0][c1] = 1;
        banmen[r1][c0] = 1;

        System.out.println("============ オセロ盤 ============");
        showCmdExample();
        System.out.println("----------------------------------");
        
        int turn = 1;

        boolean conti = true; // 継続フラグ 終了が入力されるまでtrue
        while (conti) {
            drawBanmen(banmen);            
            conti = inputCommand(turn);
            turn *= -1;
        }
        System.out.println("終了しました。");
    }
    
    /**
     * r,cの位置にコマを置く
     * 置く位置の周囲８方向に別の色のコマがあり、その方向の先に同じ色の石がある時に、
     * 同じ色のコマに挟まれた間を反転して同じ色にする。
     * @param r 行 0~
     * @param c 列 0~
     * @param v -1:黒 0:無 1:白
     * @return true:置けた, false:置けなかった
     */
    private boolean put(int r, int c, int v)
    {
        if (banmen[r][c] != 0)   // すでにある場所には置けない
           return false;
        
        byte[][] conv = new byte[rcLen][rcLen];   // 新しく置くコマとの掛け合わせ盤を作る
        for ( int i = 0; i < rcLen; i++ )         
            for ( int j = 0; j < rcLen; j++ )
                conv[i][j] = (byte)(banmen[i][j] * v);

        byte[][] valset = new byte[rcLen][rcLen]; // 判別したひっくり返すところ

 
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
    
        // デバッグ用
        // drawBanmen(conv);　　　// 掛け合わせを表示
        // drawBanmen(valset);   // 変わったところ
    
        boolean any = false; // どこか反転する場所が１つでもあったかを記憶するフラグ
    
        // 反転する場所のコマ表裏を盤面に転写   
        for ( int i = 0; i < rcLen; i++ )         
            for ( int j = 0; j < rcLen; j++ )
                if (valset[i][j] != 0) {
                    banmen[i][j] = valset[i][j];
                    any = true;
                }
                
        if (any)  // 反転する場所がなかったらそこには置けない
            banmen[r][c] = (byte)v; // コマを置く
            
        return any;
    }

    /**
     * コマンドの例を表示
     */
    private void showCmdExample()
    {
        System.console().printf("C:4 のように入力。 qで終了。 %n");
    }

    /**
     * コマンドを入力
     * @param turn -1:黒の番  1:白の番
     * @return true:継続　false:終了が入力された
     */
    private boolean inputCommand(int turn)
    {   
        Console con = System.console(); // ユーザーのコマンド入力を受け取るコンソール
        boolean accepted = false;       // コマが置けたらtrue
        
        while (!accepted ) {
            System.out.print(komaChar[turn + 1] + "の番>");

            String command = con.readLine();
            if (exitCmds.contains(command) )return false;
            if (passCmds.contains(command) )return true;

            try {
                MessageFormat mf = new MessageFormat("{0}:{1}");
                Object[] result = mf.parse(command);
                char r1 = (char)((String)result[0]).toUpperCase().charAt(0);
                int c1 = Integer.parseInt((String)result[1]);

                int r = (int)r1 - (int)'A';
                int c = c1 - 1;
                int v = turn;
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

    /**
     * 盤面を描画
     * @param ban 盤面を表す2次元配列
     */
    private void drawBanmen(byte[][] ban)
    {
        int len = ban[0].length;    // 盤面の列の長さを取得
        
        // 上の列のラベルを描画
        drawColumnLabel(len);
        System.out.println();

        int w = 0; // 白の数
        int b = 0; // 黒の数
        for ( int r = 0; r < len; r++ )
        {
            char A = (char)(((int)'A') + r );   // 行ラベル
            System.out.print(A);	 // 左側の行ラベル 
            System.out.print(' ');
            for ( int c = 0; c < len; c++ )
            {
                System.out.print(komaChar[ban[r][c] + 1]);
                System.out.print(' ');
                                
                switch (ban[r][c]) {
                    case -1: b++; break;
                    case  1: w++; break;
                    default: break;
                }
            }
            System.out.println(A);	 // 右側の行ラベル 
        }

        // 下の列のラベルを描画
        drawColumnLabel(len);
        System.out.print("   ");
        // 白黒の数を描画
        System.out.print(komaChar[0]);
        System.out.print(b);
        System.out.print(' ');
        System.out.print(komaChar[2]);
        System.out.println(w);
    }
    
    private  void drawColumnLabel(int len)
    {
        System.out.print("  ");
        for ( int c = 0; c < len; c++ )
        {
            System.out.print(c+1);
            System.out.print(' ');
        }
    } 
}
