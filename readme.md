# オセロ盤 java console
* version 1.0

コンソールに表示するオセロ盤
<pre>
  1 2 3 4 5 6 7 8   
A                 A  
B                 B  
C                 C  
D       ○ ●       D  
E       ● ○       E  
F                 F  
G                 G  
H                 H  
  1 2 3 4 5 6 7 8    ●2 ○2  
●の番>  
</pre>

# 実行方法

javac OthelloBan.java Main.java でコンパイルして、  
java Main で実行。  

java Main 4 のように引数を指定すると、盤の大きさを4~9の範囲で変更可能。

# 操作方法
白と黒の手番が交互にくるので、コマンドプロンプトに >C:4 のようにマスを指定する。  
置けない場合は >p でパスできる。 >q で終了。

