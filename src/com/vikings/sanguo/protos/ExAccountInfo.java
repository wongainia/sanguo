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

public final class ExAccountInfo implements Externalizable, Message<ExAccountInfo>, Schema<ExAccountInfo>
{

    public static Schema<ExAccountInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ExAccountInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ExAccountInfo DEFAULT_INSTANCE = new ExAccountInfo();

    
    private DataCtrl ctrl;
    private AccountInfo info;

    public ExAccountInfo()
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

    public ExAccountInfo setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public AccountInfo getInfo()
    {
        return info == null ? new AccountInfo() : info;
    }

    public ExAccountInfo setInfo(AccountInfo info)
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

    public Schema<ExAccountInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ExAccountInfo newMessage()
    {
        return new ExAccountInfo();
    }

    public Class<ExAccountInfo> typeClass()
    {
        return ExAccountInfo.class;
    }

    public String messageName()
    {
        return ExAccountInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ExAccountInfo.class.getName();
    }

    public boolean isInitialized(ExAccountInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ExAccountInfo message) throws IOException
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
                    message.info = input.mergeObject(message.info, AccountInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ExAccountInfo message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.info != null)
             output.writeObject(20, message.info, AccountInfo.getSchema(), false);

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
