/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\GitWorkspace\\PutaoLifeRobin\\PutaoLifeRobin\\PutaoLifeRobin\\src\\com\\putao\\live\\aidl\\IPutaoService.aidl
 */
package com.putao.live.aidl;
public interface IPutaoService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.putao.live.aidl.IPutaoService
{
private static final java.lang.String DESCRIPTOR = "com.putao.live.aidl.IPutaoService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.putao.live.aidl.IPutaoService interface,
 * generating a proxy if needed.
 */
public static com.putao.live.aidl.IPutaoService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.putao.live.aidl.IPutaoService))) {
return ((com.putao.live.aidl.IPutaoService)iin);
}
return new com.putao.live.aidl.IPutaoService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_userIsBind:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.userIsBind();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_plugPause:
{
data.enforceInterface(DESCRIPTOR);
this.plugPause();
reply.writeNoException();
return true;
}
case TRANSACTION_plugResume:
{
data.enforceInterface(DESCRIPTOR);
this.plugResume();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.putao.live.aidl.IPutaoService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean userIsBind() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_userIsBind, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void plugPause() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_plugPause, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void plugResume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_plugResume, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_userIsBind = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_plugPause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_plugResume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public boolean userIsBind() throws android.os.RemoteException;
public void plugPause() throws android.os.RemoteException;
public void plugResume() throws android.os.RemoteException;
}
