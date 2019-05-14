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

public final class MsgReqAccountRestore2 implements Externalizable, Message<MsgReqAccountRestore2>, Schema<MsgReqAccountRestore2>
{

    public static Schema<MsgReqAccountRestore2> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgReqAccountRestore2 getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgReqAccountRestore2 DEFAULT_INSTANCE = new MsgReqAccountRestore2();

    
    private Integer flag;
    private String value;
    private Integer code;
    private ByteString key;

    public MsgReqAccountRestore2()
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

    public MsgReqAccountRestore2 setFlag(Integer flag)
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

    public MsgReqAccountRestore2 setValue(String value)
    {
        this.value = value;
        return this;
    }

    // code

    public boolean hasCode(){
        return code != null;
    }


    public Integer getCode()
    {
        return code == null ? 0 : code;
    }

    public MsgReqAccountRestore2 setCode(Integer code)
    {
        this.code = code;
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

    public MsgReqAccountRestore2 setKey(ByteString key)
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

    public Schema<MsgReqAccountRestore2> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgReqAccountRestore2 newMessage()
    {
        return new MsgReqAccountRestore2();
    }

    public Class<MsgReqAccountRestore2> typeClass()
    {
        return MsgReqAccountRestore2.class;
    }

    public String messageName()
    {
        return MsgReqAccountRestore2.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgReqAccountRestore2.class.getName();
    }

    public boolean isInitialized(MsgReqAccountRestore2 message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgReqAccountRestore2 message) throws IOException
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
                case 30:
                    message.code = input.readUInt32();
                    break;
                case 40:
                    message.key = input.readBytes();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgReqAccountRestore2 message) throws IOException
    {
        if(message.flag != null)
            output.writeUInt32(10, message.flag, false);

        if(message.value != null)
            output.writeString(20, message.value, false);

        if(message.code != null)
            output.writeUInt32(30, message.code, false);

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
