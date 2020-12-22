// IPool.aidl
package com.seanyj.mysamples.ipc;

interface IPool {
    IBinder getBinder(int type);
}
