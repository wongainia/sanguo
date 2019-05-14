// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from common.proto

package com.vikings.sanguo.protos;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

public final class ExBattleInfo implements Externalizable, Message<ExBattleInfo>, Schema<ExBattleInfo>
{

    public static Schema<ExBattleInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ExBattleInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ExBattleInfo DEFAULT_INSTANCE = new ExBattleInfo();

    
    private DataCtrl ctrl;
    private BattleInfo info;

    public ExBattleInfo()
    {
        
    }

    // getters and setters

    // ctrl

    public boolean hasCtrl(){
        return ctrl != null;
    }


    public DataCtrl getCtrl()
    {
        return ctrl == null ? new DataCtrl() : ctrl;
    }

    public ExBattleInfo setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public BattleInfo getInfo()
    {
        return info == null ? new BattleInfo() : info;
    }

    public ExBattleInfo setInfo(BattleInfo info)
    {
        this.info = info;
        return this;
    }

    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<ExBattleInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ExBattleInfo newMessage()
    {
        return new ExBattleInfo();
    }

    public Class<ExBattleInfo> typeClass()
    {
        return ExBattleInfo.class;
    }

    public String messageName()
    {
        return ExBattleInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ExBattleInfo.class.getName();
    }

    public boolean isInitialized(ExBattleInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ExBattleInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.ctrl = input.mergeObject(message.ctrl, DataCtrl.getSchema());
                    break;

                case 20:
                    message.info = input.mergeObject(message.info, BattleInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ExBattleInfo message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.info != null)
             output.writeObject(20, message.info, BattleInfo.getSchema(), false);

    }

    public String getFieldName(int number)
    {
        return Integer.toString(number);
    }

    public int getFieldNumber(String name)
    {
        return Integer.parseInt(name);
    }
    
}
