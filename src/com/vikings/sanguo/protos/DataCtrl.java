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

public final class DataCtrl implements Externalizable, Message<DataCtrl>, Schema<DataCtrl>
{

    public static Schema<DataCtrl> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static DataCtrl getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final DataCtrl DEFAULT_INSTANCE = new DataCtrl();

    
    private Integer ver;
    private Integer op;

    public DataCtrl()
    {
        
    }

    // getters and setters

    // ver

    public boolean hasVer(){
        return ver != null;
    }


    public Integer getVer()
    {
        return ver == null ? 0 : ver;
    }

    public DataCtrl setVer(Integer ver)
    {
        this.ver = ver;
        return this;
    }

    // op

    public boolean hasOp(){
        return op != null;
    }


    public Integer getOp()
    {
        return op == null ? 0 : op;
    }

    public DataCtrl setOp(Integer op)
    {
        this.op = op;
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

    public Schema<DataCtrl> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public DataCtrl newMessage()
    {
        return new DataCtrl();
    }

    public Class<DataCtrl> typeClass()
    {
        return DataCtrl.class;
    }

    public String messageName()
    {
        return DataCtrl.class.getSimpleName();
    }

    public String messageFullName()
    {
        return DataCtrl.class.getName();
    }

    public boolean isInitialized(DataCtrl message)
    {
        return true;
    }

    public void mergeFrom(Input input, DataCtrl message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.ver = input.readUInt32();
                    break;
                case 20:
                    message.op = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, DataCtrl message) throws IOException
    {
        if(message.ver != null)
            output.writeUInt32(10, message.ver, false);

        if(message.op != null)
            output.writeUInt32(20, message.op, false);
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
