// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from client.proto

package com.vikings.sanguo.protos;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

public final class MsgReqAccountRestore implements Externalizable, Message<MsgReqAccountRestore>, Schema<MsgReqAccountRestore>
{

    public static Schema<MsgReqAccountRestore> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqAccountRestore getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqAccountRestore DEFAULT_INSTANCE = new MsgReqAccountRestore();

    
    private Integer flag;
    private String value;
    private ByteString key;

    public MsgReqAccountRestore()
    {
        
    }

    // getters and setters

    // flag

    public boolean hasFlag(){
        return flag != null;
    }


    public Integer getFlag()
    {
        return flag == null ? 0 : flag;
    }

    public MsgReqAccountRestore setFlag(Integer flag)
    {
        this.flag = flag;
        return this;
    }

    // value

    public boolean hasValue(){
        return value != null;
    }


    public String getValue()
    {
        return value == null ? "" : value;
    }

    public MsgReqAccountRestore setValue(String value)
    {
        this.value = value;
        return this;
    }

    // key

    public boolean hasKey(){
        return key != null;
    }


    public ByteString getKey()
    {
        return key == null ? ByteString.bytesDefaultValue("") : key;
    }

    public MsgReqAccountRestore setKey(ByteString key)
    {
        this.key = key;
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

    public Schema<MsgReqAccountRestore> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqAccountRestore newMessage()
    {
        return new MsgReqAccountRestore();
    }

    public Class<MsgReqAccountRestore> typeClass()
    {
        return MsgReqAccountRestore.class;
    }

    public String messageName()
    {
        return MsgReqAccountRestore.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqAccountRestore.class.getName();
    }

    public boolean isInitialized(MsgReqAccountRestore message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqAccountRestore message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.flag = input.readUInt32();
                    break;
                case 20:
                    message.value = input.readString();
                    break;
                case 40:
                    message.key = input.readBytes();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqAccountRestore message) throws IOException
    {
        if(message.flag != null)
            output.writeUInt32(10, message.flag, false);

        if(message.value != null)
            output.writeString(20, message.value, false);

        if(message.key != null)
            output.writeBytes(40, message.key, false);
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
