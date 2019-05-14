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

public final class UserAttrScoreInfo implements Externalizable, Message<UserAttrScoreInfo>, Schema<UserAttrScoreInfo>
{

    public static Schema<UserAttrScoreInfo> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static UserAttrScoreInfo getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final UserAttrScoreInfo DEFAULT_INSTANCE = new UserAttrScoreInfo();

    
    private Integer userid;
    private Integer score;
    private Boolean hot;

    public UserAttrScoreInfo()
    {
        
    }

    // getters and setters

    // userid

    public boolean hasUserid(){
        return userid != null;
    }


    public Integer getUserid()
    {
        return userid == null ? 0 : userid;
    }

    public UserAttrScoreInfo setUserid(Integer userid)
    {
        this.userid = userid;
        return this;
    }

    // score

    public boolean hasScore(){
        return score != null;
    }


    public Integer getScore()
    {
        return score == null ? 0 : score;
    }

    public UserAttrScoreInfo setScore(Integer score)
    {
        this.score = score;
        return this;
    }

    // hot

    public boolean hasHot(){
        return hot != null;
    }


    public Boolean getHot()
    {
        return hot == null ? false : hot;
    }

    public UserAttrScoreInfo setHot(Boolean hot)
    {
        this.hot = hot;
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

    public Schema<UserAttrScoreInfo> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public UserAttrScoreInfo newMessage()
    {
        return new UserAttrScoreInfo();
    }

    public Class<UserAttrScoreInfo> typeClass()
    {
        return UserAttrScoreInfo.class;
    }

    public String messageName()
    {
        return UserAttrScoreInfo.class.getSimpleName();
    }

    public String messageFullName()
    {
        return UserAttrScoreInfo.class.getName();
    }

    public boolean isInitialized(UserAttrScoreInfo message)
    {
        return true;
    }

    public void mergeFrom(Input input, UserAttrScoreInfo message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.userid = input.readUInt32();
                    break;
                case 20:
                    message.score = input.readUInt32();
                    break;
                case 30:
                    message.hot = input.readBool();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, UserAttrScoreInfo message) throws IOException
    {
        if(message.userid != null)
            output.writeUInt32(10, message.userid, false);

        if(message.score != null)
            output.writeUInt32(20, message.score, false);

        if(message.hot != null)
            output.writeBool(30, message.hot, false);
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