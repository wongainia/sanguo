// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from client.proto

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

public final class MsgQueryRichOtherUserInfoRsp implements Externalizable, Message<MsgQueryRichOtherUserInfoRsp>, Schema<MsgQueryRichOtherUserInfoRsp>
{

    public static Schema<MsgQueryRichOtherUserInfoRsp> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static MsgQueryRichOtherUserInfoRsp getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final MsgQueryRichOtherUserInfoRsp DEFAULT_INSTANCE = new MsgQueryRichOtherUserInfoRsp();

    
    private OtherUserInfo ui;
    private OtherLordInfo li;
    private ManorInfo mi;
    private UserTroopEffectInfo troopEffectInfo;

    public MsgQueryRichOtherUserInfoRsp()
    {
        
    }

    // getters and setters

    // ui

    public boolean hasUi(){
        return ui != null;
    }


    public OtherUserInfo getUi()
    {
        return ui == null ? new OtherUserInfo() : ui;
    }

    public MsgQueryRichOtherUserInfoRsp setUi(OtherUserInfo ui)
    {
        this.ui = ui;
        return this;
    }

    // li

    public boolean hasLi(){
        return li != null;
    }


    public OtherLordInfo getLi()
    {
        return li == null ? new OtherLordInfo() : li;
    }

    public MsgQueryRichOtherUserInfoRsp setLi(OtherLordInfo li)
    {
        this.li = li;
        return this;
    }

    // mi

    public boolean hasMi(){
        return mi != null;
    }


    public ManorInfo getMi()
    {
        return mi == null ? new ManorInfo() : mi;
    }

    public MsgQueryRichOtherUserInfoRsp setMi(ManorInfo mi)
    {
        this.mi = mi;
        return this;
    }

    // troopEffectInfo

    public boolean hasTroopEffectInfo(){
        return troopEffectInfo != null;
    }


    public UserTroopEffectInfo getTroopEffectInfo()
    {
        return troopEffectInfo == null ? new UserTroopEffectInfo() : troopEffectInfo;
    }

    public MsgQueryRichOtherUserInfoRsp setTroopEffectInfo(UserTroopEffectInfo troopEffectInfo)
    {
        this.troopEffectInfo = troopEffectInfo;
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

    public Schema<MsgQueryRichOtherUserInfoRsp> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public MsgQueryRichOtherUserInfoRsp newMessage()
    {
        return new MsgQueryRichOtherUserInfoRsp();
    }

    public Class<MsgQueryRichOtherUserInfoRsp> typeClass()
    {
        return MsgQueryRichOtherUserInfoRsp.class;
    }

    public String messageName()
    {
        return MsgQueryRichOtherUserInfoRsp.class.getSimpleName();
    }

    public String messageFullName()
    {
        return MsgQueryRichOtherUserInfoRsp.class.getName();
    }

    public boolean isInitialized(MsgQueryRichOtherUserInfoRsp message)
    {
        return true;
    }

    public void mergeFrom(Input input, MsgQueryRichOtherUserInfoRsp message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 10:
                    message.ui = input.mergeObject(message.ui, OtherUserInfo.getSchema());
                    break;

                case 20:
                    message.li = input.mergeObject(message.li, OtherLordInfo.getSchema());
                    break;

                case 40:
                    message.mi = input.mergeObject(message.mi, ManorInfo.getSchema());
                    break;

                case 50:
                    message.troopEffectInfo = input.mergeObject(message.troopEffectInfo, UserTroopEffectInfo.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, MsgQueryRichOtherUserInfoRsp message) throws IOException
    {
        if(message.ui != null)
             output.writeObject(10, message.ui, OtherUserInfo.getSchema(), false);


        if(message.li != null)
             output.writeObject(20, message.li, OtherLordInfo.getSchema(), false);


        if(message.mi != null)
             output.writeObject(40, message.mi, ManorInfo.getSchema(), false);


        if(message.troopEffectInfo != null)
             output.writeObject(50, message.troopEffectInfo, UserTroopEffectInfo.getSchema(), false);

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