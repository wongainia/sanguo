package com.vikings.sanguo.model;

import java.util.List;

public class SyncData<T> {

	private SyncCtrl ctrl;
	
	private T data;
	
	public byte getCtrlOP() {
		return ctrl.getOp();
	}
	
	public SyncCtrl getCtrl() {
		return ctrl;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public void setCtrl(SyncCtrl ctrl) {
		this.ctrl = ctrl;
	}
	
	public void update2List(List<T> ls){
		int index = ls.indexOf(data);
		switch (ctrl.getOp()) {
		case SyncCtrl.DATA_CTRL_OP_NONE:
		case SyncCtrl.DATA_CTRL_OP_ADD:
		case SyncCtrl.DATA_CTRL_OP_REP:
			if (index == -1)
				ls.add(data);
			else{
				T obj = ls.get(index);
				if(obj instanceof Copyable){
					((Copyable) obj).copyFrom(data);
				}
				else{
					ls.set(index, data);
				}
			}
			break;
		case SyncCtrl.DATA_CTRL_OP_DEL:
			if (index != -1)
				ls.remove(index);
			break;
		default:
			break;
		}
	}
	
}
