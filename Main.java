/**
 * コマンドターミナルから実行するメイン
 */
public class Main {
 
    /**
     * @param args[0] 盤の大きさを１辺のマス目の数で指定　3~9の数値　未指定時は8
     */
    public static void main(String[] args) throws Exception {
        OthelloBan othelloBan = new OthelloBan();
        
        int rcLen = OthelloBan.RowColLen;
        if (args.length == 1)
            rcLen = Integer.parseInt(args[0]);

        othelloBan.start(rcLen);
    }
}