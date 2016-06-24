//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.keeper;

import android.os.Environment;
import com.iqiyi.sdk.android.vcop.api.VCOPClient;
import com.iqiyi.sdk.android.vcop.unit.AppTokenInfor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccessTokenkeeper {
    private static final String accessTokenLogName;
    private static AccessTokenkeeper instance;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private List<AppTokenInfor> appTokenInforList = null;

    static {
        accessTokenLogName = Environment.getExternalStorageDirectory().getPath() + "/iqiyisdk/vcop" + "/" + VCOPClient.app_key + "/vcoptoken.log";
        instance = null;
    }

    public AccessTokenkeeper() {
    }

    public static AccessTokenkeeper getInstance() {
        if(instance == null) {
            instance = new AccessTokenkeeper();
            instance.readAll();
        }

        return instance;
    }

    public boolean isTokenExist(String appkey) {
        this.readAll();
        if(this.appTokenInforList == null) {
            return false;
        } else {
            Iterator var3 = this.appTokenInforList.iterator();

            while(var3.hasNext()) {
                AppTokenInfor tokenInfor = (AppTokenInfor)var3.next();
                if(tokenInfor.getAppKey().compareTo(appkey) == 0) {
                    return true;
                }
            }

            return false;
        }
    }

    public AppTokenInfor getTokenInfor(String appkey) {
        this.readAll();
        if(this.appTokenInforList == null) {
            return null;
        } else {
            Iterator var3 = this.appTokenInforList.iterator();

            while(var3.hasNext()) {
                AppTokenInfor tokenInfor = (AppTokenInfor)var3.next();
                if(tokenInfor.getAppKey().compareTo(appkey) == 0) {
                    return tokenInfor;
                }
            }

            return null;
        }
    }

    public AppTokenInfor getTokenInfor(String appkey, String ouid, String nickName) {
        this.readAll();
        if(this.appTokenInforList == null) {
            return null;
        } else {
            Iterator var5 = this.appTokenInforList.iterator();

            AppTokenInfor tokenInfor;
            do {
                if(!var5.hasNext()) {
                    return null;
                }

                tokenInfor = (AppTokenInfor)var5.next();
            } while(tokenInfor.getAppKey().compareTo(appkey) != 0 || tokenInfor.getOuid().compareTo(ouid) != 0 || tokenInfor.getNickName().compareTo(nickName) != 0);

            return tokenInfor;
        }
    }

    public void SaveAppInfor(AppTokenInfor infor) {
        if(infor != null) {
            if(this.appTokenInforList != null) {
                if(this.getTokenInfor(infor.getAppKey()) != null) {
                    this.updateInfor(infor, infor.getAppKey());
                } else {
                    this.appTokenInforList.add(infor);
                    this.saveAll();
                }
            }
        }
    }

    public void updateInfor(AppTokenInfor infor, String appKey) {
        if(this.appTokenInforList != null) {
            if(infor != null) {
                if(appKey != null && appKey != "") {
                    AppTokenInfor delinfor = null;
                    Iterator var5 = this.appTokenInforList.iterator();

                    while(var5.hasNext()) {
                        AppTokenInfor myinfor = (AppTokenInfor)var5.next();
                        if(myinfor.getAppKey().compareTo(appKey) == 0) {
                            delinfor = myinfor;
                            break;
                        }
                    }

                    if(delinfor != null) {
                        this.appTokenInforList.remove(delinfor);
                        this.appTokenInforList.add(infor);
                        this.saveAll();
                    }

                }
            }
        }
    }

    private void readAll() {
        ObjectInputStream ois = null;
        this.lock.readLock().lock();

        try {
            ois = new ObjectInputStream(new FileInputStream(accessTokenLogName));
            this.appTokenInforList = (List)ois.readObject();
        } catch (StreamCorruptedException var17) {
            var17.printStackTrace();
        } catch (FileNotFoundException var18) {
            var18.printStackTrace();
        } catch (IOException var19) {
            var19.printStackTrace();
        } catch (ClassNotFoundException var20) {
            var20.printStackTrace();
        } finally {
            if(this.appTokenInforList == null) {
                this.appTokenInforList = new ArrayList();
            }

            if(ois != null) {
                try {
                    ois.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

            this.lock.readLock().unlock();
        }

    }

    private void saveAll() {
        if(this.appTokenInforList == null) {
            this.appTokenInforList = new ArrayList();
        } else {
            this.lock.writeLock().lock();
            File file = new File(accessTokenLogName);
            if(!file.exists()) {
                File oos = file.getParentFile();
                if(oos != null && !oos.exists()) {
                    oos.mkdirs();
                }

                file = null;
            }

            ObjectOutputStream oos1 = null;

            try {
                oos1 = new ObjectOutputStream(new FileOutputStream(accessTokenLogName));
                oos1.writeObject(this.appTokenInforList);
            } catch (FileNotFoundException var14) {
                var14.printStackTrace();
            } catch (IOException var15) {
                var15.printStackTrace();
            } finally {
                if(oos1 != null) {
                    try {
                        oos1.close();
                    } catch (IOException var13) {
                        var13.printStackTrace();
                    }
                }

                this.lock.writeLock().unlock();
            }

        }
    }
}
