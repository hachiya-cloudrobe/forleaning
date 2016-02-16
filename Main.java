/**
 * コマンドターミナルから実行するメイン
 */
public class Main {
 
    /**
     * @param args[0] 盤の大きさを１辺のマス目の数で指定　4~9の数値　未指定時は8
     */
    public static void main(String[] args) throws Exception {
        OthelloBan othelloBan = new OthelloBan();
        
        String errMesArgmentFormat = "盤面の大きさは 4~9の範囲の数値で指定してください。例 java Main 4"; 
        
        int rcLen = OthelloBan.RowColLen;
        if (args.length == 1) {
	       try{
                rcLen = Integer.parseInt(args[0]);
                if ( rcLen < 4 || rcLen > 9) {
                    System.out.println(errMesArgmentFormat);
                    return ;
                }
            } catch (java.lang.NumberFormatException ex) {
                System.out.println(errMesArgmentFormat);
                return ;
            }
        }
        othelloBan.start(rcLen);
    }
}