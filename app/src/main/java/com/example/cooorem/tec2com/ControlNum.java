package com.example.cooorem.tec2com;

import android.util.Log;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ControlNum {
    int num[];
    boolean isSetR,isSetS,isSetCao,isSetTo;
    public ControlNum(){
        num=new int[14];
        for(int i:num )i=0;
        isSetR=isSetS=isSetCao=isSetTo=false;
        num[7]=1;//I8~I6
        breakOff(false);
    }
    public void breakOff(boolean is){
        if(is){
            num[13]=2;
            num[12]=9;
            num[11]=0;
            num[10]=3;
            num[9]= 0;
        }else{
            num[13]=0;
            num[12]=0;
            num[11]=0;
            num[10]=14;
            num[9]= 0;
        }
    }
    public boolean plus1(boolean is){
        if(is)num[2]=4;
        else num[2]=0;
        return true;
    }
    public void setR(int mode){
        //0 0, 1 SR, 2 MEM, 3 PC
        isSetR=true;
        switch (mode){
            case 0:
                num[7]|=8;num[6]&=7;num[5]|=8;//unregister MEM
                num[5]=(num[5]&8)+3;//I2~I0
                num[1]=num[1]&7;//SA=0
                break;
            case 1:
                num[7]|=8;num[6]&=7;num[5]|=8;
                num[5]=(num[5]&8)+1;//I2~I0
                num[1]=num[1]|8;//SA=1
                break;
            case 2:
                num[7]&=7;num[6]&=7;num[5]|=8;//register MEM
                num[5]=(num[5]&8)+7;//I2~I0
                num[1]=num[1]&7;//SA=0
                num[4]=0;//A=0
                break;
            case 3:
                num[7]|=8;num[6]&=7;num[5]|=8;//unregister MEM
                num[5]=(num[5]&8)+1;//I2~I0
                num[1]=num[1]&7;//SA=0
                num[4]=5;//A=R5(PC)
                break;
        }

    }
    public boolean setS(int mode){
        //0 Q, 1 DR, 2 PC, 3 SR, 4 0
        isSetS=true;
        int a=stateR();
        if(mode>4)return false;
        if(a==1&&mode>2)return false;
        if(a==2&&mode>3)return false;
        switch (mode) {
            case 0:
                if(a==0)num[5]=(num[5]&8)+6;//I2~I0
                if(a==1)num[5]=(num[5]&8)+0;//I2~I0
                if(a==2)num[5]=(num[5]&8)+2;//I2~I0
                if(a==3)num[5]=(num[5]&8)+6;//I2~I0
                break;
            case 1:
                if(a==0)return false;//I2~I0
                if(a==1)num[5]=(num[5]&8)+1;//I2~I0
                if(a==2)num[5]=(num[5]&8)+3;//I2~I0
                if(a==3)return false;//I2~I0
                num[0]=num[0]|8;//SB=1
                break;
            case 2:
                if(a==0)return false;//I2~I0
                if(a==1)num[5]=(num[5]&8)+1;//I2~I0
                if(a==2)num[5]=(num[5]&8)+3;//I2~I0
                if(a==3)return false;//I2~I0
                num[0]=num[0]&7;//SB=0
                num[3]=5;//B=R5(PC)
                break;
            case 3:
                if(a==0)num[5]=(num[5]&8)+5;//I2~I0
                if(a==1)return false;//I2~I0
                if(a==2)num[5]=(num[5]&8)+4;//I2~I0
                if(a==3)num[5]=(num[5]&8)+5;//I2~I0
                if((num[1]>>3)==1){
                    Log.d("ControlNum","setS SA=1. Can't change it");
                    return false;
                }
                num[1]=num[1]&7;//SA=0
                break;
            case 4:
                if(a!=0&&a!=3)return false;
                num[5]=(num[5]&8)+7;//I2~I0
                break;
        }
        return true;
    }
    public void setCao(int mode){
        //0+,1-,2and,3or,4RE-
        isSetCao=true;
        switch (mode){
            case 0:
                num[6]=(num[4]&8)+0;//I5~I3
                break;
            case 1:
                num[6]=(num[4]&8)+2;//I5~I3
                break;
            case 2:
                num[6]=(num[4]&8)+4;//I5~I3
                break;
            case 3:
                num[6]=(num[4]&8)+3;//I5~I3
                break;
            case 4:
                num[6]=(num[4]&8)+1;//I5~I3
                break;
        }

    }
    public boolean setTo(int mode){
        /*无B口检测
        0 AR, 1 PC, 2 MEM, 3 DR, 4 Q
        */
        isSetTo=true;
        boolean isRMEM=stateR()==0;
        switch(mode){
            case 0:
                num[1]=(num[1]&8)+1;//DC1
                num[0]=(num[0]&8)+2;//DC2
                if(!isRMEM)
                    num[7]|=8;num[6]&=7;num[5]|=8;//unregister MEM
                num[7]=(num[7]&8)+1;//I8~I6 ->Y
                break;
            case 1:
                num[1]=(num[1]&8)+0;//DC1
                num[0]=(num[0]&8)+0;//
                num[0]=num[0]&7;//SB=0
                num[3]=5;//B=R5(PC)
                if(!isRMEM)
                    num[7]|=8;num[6]&=7;num[5]|=8;//unregister MEM
                num[7]=(num[7]&8)+3;//I8~I6 ->B
                break;
            case 2:
                if(isRMEM)return false;
                num[1]=(num[1]&8)+1;//DC1
                num[0]=(num[0]&8)+0;//DC2
                num[7]&=7;num[6]&=7;num[5]&=7;//register write MEM
                num[7]=(num[7]&8)+1;//I8~I6 ->Y
                break;
            case 3:
                num[1]=(num[1]&8)+0;//DC1
                num[0]=(num[0]&8)+0;//DC2
                if(!isRMEM)
                    num[7]|=8;num[6]&=7;num[5]|=8;//unregister MEM
                num[7]=(num[7]&8)+3;//I8~I6 ->B
                num[0]=num[0]|8;//SB=1
                break;
            case 4:
                num[1]=(num[1]&8)+0;//DC1
                num[0]=(num[0]&8)+0;//DC2
                if(!isRMEM)
                    num[7]|=8;num[6]&=7;num[5]|=8;//unregister MEM
                num[7]=(num[7]&8)+0;//I8~I6 ->Y
                break;
        }
        return true;
    }
    public int stateR(){
        //0 MEM, 1 A, 2 0, 3 D
        if((num[7]>>3)==0&&(num[6]>>3)==0&&(num[5]>>3)==1)return 0;
        if((num[5]&7)<=1)return 1;
        if((num[5]&7)<=4)return 2;
        return 3;
    }
    private char char2num(int start){
        if(num[start]<10)return (char)(num[start]+'0');
        else return (char)(num[start]-10+'A');
    }
    public String toString(){
        if(!(isSetR&&isSetS&&isSetCao&&isSetTo))return new String("None");
        StringBuilder sb=new StringBuilder();
        sb.append("00");
        for(int i=13;i>=0;--i){
            sb.append(char2num(i));
            if(i%4==0)sb.append(' ');
        }
        return sb.toString();
    }
}
