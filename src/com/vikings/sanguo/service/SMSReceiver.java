package com.vikings.sanguo.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;

public class SMSReceiver extends BroadcastReceiver {
	// android.provider.Telephony.Sms.Intents
	private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			SmsMessage[] messages = getMessagesFromIntent(intent);
			for (SmsMessage message : messages) {
				// Log.i(TAG, message.getOriginatingAddress() + " : "
				// + message.getDisplayOriginatingAddress() + " : "
				// + message.getDisplayMessageBody() + " : "
				// + message.getTimestampMillis());
				// 106575296625 1252015986615571 10659057652422
				String msg = message.getDisplayMessageBody();
				if (!(msg.indexOf(Config.getController().getResources()
						.getString(R.string.str_msg)) == -1)) {
					// String telephone =
					// CacheMgr.dictCache.getDict(Dict.TYPE_VERIFICODE, (byte)
					// 1);
					// if (message.getOriginatingAddress().equals(telephone)) {
					Pattern pattern = Pattern.compile("(：)([0-9]+?)(,)");
					Matcher matcher = pattern.matcher(msg);
					while (matcher.find()) {
						Config.verifyCode = Integer.valueOf(matcher.group(2));
					}
					// }
				}
			}
		}
	}

	public final SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}

}
