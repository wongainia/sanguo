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

public final class ExManorInfo implements Externalizable, Message<ExManorInfo>, Schema<ExManorInfo>
{

    public static Schema<ExManorInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static ExManorInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final ExManorInfo DEFAULT_INSTANCE = new ExManorInfo();

    
    private DataCtrl ctrl;
    private ManorInfo info;

    public ExManorInfo()
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

    public ExManorInfo setCtrl(DataCtrl ctrl)
    {
        this.ctrl = ctrl;
        return this;
    }

    // info

    public boolean hasInfo(){
        return info != null;
    }


    public ManorInfo getInfo()
    {
        return info == null ? new ManorInfo() : info;
    }

    public ExManorInfo setInfo(ManorInfo info)
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

    public Schema<ExManorInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public ExManorInfo newMessage()
    {
        return new ExManorInfo();
    }

    public Class<ExManorInfo> typeClass()
    {
        return ExManorInfo.class;
    }

    public String messageName()
    {
        return ExManorInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return ExManorInfo.class.getName();
    }

    public boolean isInitialized(ExManorInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, ExManorInfo message) throws IOException
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
                    message.info = input.mergeObject(message.info, ManorInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, ExManorInfo message) throws IOException
    {
        if(message.ctrl != null)
             output.writeObject(10, message.ctrl, DataCtrl.getSchema(), false);


        if(message.info != null)
             output.writeObject(20, message.info, ManorInfo.getSchema(), false);

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
