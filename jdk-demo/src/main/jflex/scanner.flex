package com.github.xiafou.demo.analysis;

import java_cup.runtime.Symbol;
import java.io.*;

%%
//参数设置和声明段
%class Scanner
%line
%column
%cup
%unicode

%{
    public static void init(){}/**//* Just为了兼容手写版*/
    private Symbol symbol(int type){
        return new Symbol(type,yyline,yycolumn);
    }
    private Symbol symbol(int type,Object value){
        return new Symbol(type,yyline,yycolumn,value);
    }
%}
digit=[0-9]
number={digit}+
LineTerminator=/r|/n|/r/n
WhiteSpace={LineTerminator}|[ /t/f]
%%
//词法规则段
{
    ";" { return symbol(sym.SEMI); /**//*case ";"*/}
    "+" { return symbol(sym.PLUS); /**//*case "+"*/}
    "-" { return symbol(sym.MINUS); /**//*case "-"*/}
    "*" { return symbol(sym.TIMES); /**//*case "*"*/}
    "/" { return symbol(sym.DIVIDE); /**//*case "/"*/}
    "%" { return symbol(sym.MOD); /**//*case "%"*/}
    "(" { return symbol(sym.LPAREN); /**//*case "("*/}
    ")" { return symbol(sym.RPAREN); /**//*case ")"*/}
    {number} { return symbol(sym.NUMBER,new Integer(yytext())); /**//*case {number}*/}
    {WhiteSpace} {/**//*case {WhiteSpace}:  do nothing*/}
   
}
. {
    System.out.println("Error:"+yytext()+" is illegal!");
}