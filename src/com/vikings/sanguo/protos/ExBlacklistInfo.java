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

public final class ExBlacklistInfo implements Externalizable, Message<ExBlacklistInfo>, Schema<ExBlacklistInfo>
{

    public static Schema<ExBlacklistInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ExBlacklistInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ExBlacklistInfo DEFAULT_INSTANCE = new ExBlacklistInfo();

    
    private DataCtrl ctrl;
    private BlacklistInfo info;

    public ExBlacklistInfo()
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

    public ExBlacklistInfo setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public BlacklistInfo getInfo()
    {
        return info == null ? new BlacklistInfo() : info;
    }

    public ExBlacklistInfo setInfo(BlacklistInfo info)
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

    public Schema<ExBlacklistInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ExBlacklistInfo newMessage()
    {
        return new ExBlacklistInfo();
    }

    public Class<ExBlacklistInfo> typeClass()
    {
        return ExBlacklistInfo.class;
    }

    public String messageName()
    {
        return ExBlacklistInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ExBlacklistInfo.class.getName();
    }

    public boolean isInitialized(ExBlacklistInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ExBlacklistInfo message) throws IOException
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
                    message.info = input.mergeObject(message.info, BlacklistInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ExBlacklistInfo message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.info != null)
             output.writeObject(20, message.info, BlacklistInfo.getSchema(), false);

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
