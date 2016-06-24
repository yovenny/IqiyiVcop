//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iqiyi.sdk.android.vcop.keeper;

import android.os.Environment;
import com.iqiyi.sdk.android.vcop.api.VCOPClient;
import com.iqiyi.sdk.android.vcop.qichuan.UploadInfor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class UploadInforKeeper {
    private static final String UploadLogName;
    private List<UploadInfor> uploadList = new ArrayList();
    private static UploadInforKeeper instance;

    static {
        UploadLogName = Environment.getExternalStorageDirectory().getPath() + "/iqiyisdk/vcop" + "/" + VCOPClient.app_key + "/vcop.log";
        instance = null;
    }

    private UploadInforKeeper() {
    }

    public static synchronized UploadInforKeeper getInstance() {
        if(instance == null) {
            instance = new UploadInforKeeper();
            instance.readAll();
        }

        return instance;
    }

    public synchronized List<UploadInfor> getUploadInforList() {
        this.readAll();
        return this.uploadList;
    }

    private void saveAll() {
        if(this.uploadList == null) {
            this.uploadList = new ArrayList();
        } else {
            File file = new File(UploadLogName);
            if(!file.exists()) {
                File oos = file.getParentFile();
                if(oos != null && !oos.exists()) {
                    oos.mkdirs();
                }

                file = null;
            }

            ObjectOutputStream oos1 = null;

            try {
                oos1 = new ObjectOutputStream(new FileOutputStream(UploadLogName));
                oos1.writeObject(this.uploadList);
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

            }

        }
    }

    private void readAll() {
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(new FileInputStream(UploadLogName));
            if(this.uploadList != null) {
                this.uploadList.clear();
            }

            this.uploadList = (List)ois.readObject();
        } catch (StreamCorruptedException var17) {
            var17.printStackTrace();
        } catch (FileNotFoundException var18) {
            var18.printStackTrace();
        } catch (IOException var19) {
            var19.printStackTrace();
        } catch (ClassNotFoundException var20) {
            var20.printStackTrace();
        } finally {
            if(this.uploadList == null) {
                this.uploadList = new ArrayList();
            }

            if(ois != null) {
                try {
                    ois.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

        }

    }

    public synchronized List<UploadInfor> getAllInfor() {
        this.readAll();
        ArrayList bak = new ArrayList();
        HashMap mymap = new HashMap();
        UploadInfor infor = null;
        Iterator iter = this.uploadList.iterator();

        List entry;
        UploadInfor list;
        while(iter.hasNext() && this.uploadList.size() != 0) {
            infor = (UploadInfor)iter.next();
            if(!mymap.containsKey(infor.getFileiId())) {
                ArrayList entry1 = new ArrayList();
                entry1.add(infor);
                mymap.put(infor.getFileiId(), entry1);
                entry = null;
            } else {
                entry = (List)mymap.get(infor.getFileiId());
                if(entry != null) {
                    entry.add(infor);
                    list = (UploadInfor)entry.get(0);
                    double sum = 0.0D;

                    UploadInfor tmp;
                    for(Iterator var10 = entry.iterator(); var10.hasNext(); sum += tmp.getProgress()) {
                        tmp = (UploadInfor)var10.next();
                    }

                    list.setAllProgress(sum);
                }
            }
        }

        for(iter = mymap.entrySet().iterator(); iter.hasNext(); list = null) {
            Entry entry2 = (Entry)iter.next();
            List list1 = (List)entry2.getValue();
            if(list1 != null && list1.size() > 0) {
                bak.add((UploadInfor)list1.get(0));
            }

            entry = null;
        }

        mymap.clear();
        mymap = null;
        return bak;
    }

    public synchronized boolean isHasUploadInfor(String filePath) {
        if(filePath == null) {
            return false;
        } else if(this.uploadList == null) {
            return false;
        } else {
            UploadInfor infor = null;
            Iterator iterator = this.uploadList.iterator();

            while(iterator.hasNext() && this.uploadList.size() != 0) {
                infor = (UploadInfor)iterator.next();
                if(infor != null && infor.getFilePath().compareTo(filePath) == 0) {
                    return true;
                }
            }

            return false;
        }
    }

    public synchronized boolean isHasUploadInforByfileId(String fileid) {
        if(fileid == null) {
            return false;
        } else if(this.uploadList == null) {
            return false;
        } else {
            UploadInfor infor = null;
            Iterator iterator = this.uploadList.iterator();

            while(iterator.hasNext() && this.uploadList.size() != 0) {
                infor = (UploadInfor)iterator.next();
                if(infor != null && infor.getFileiId().compareTo(fileid) == 0) {
                    return true;
                }
            }

            return false;
        }
    }

    public synchronized void SaveInfor(UploadInfor infor) {
        if(infor != null) {
            if(this.getInforByfileId(infor.getFileiId()) != null) {
                this.updateInfor(infor, infor.getFileiId(), infor.getThreadId());
            } else {
                this.uploadList.add(infor);
                this.saveAll();
            }
        }
    }

    public synchronized void SaveInforByThreadId(UploadInfor infor) {
        if(infor != null) {
            if(this.getInforByThreadId(infor.getThreadId(), infor.getFileiId()) != null) {
                this.updateInfor(infor, infor.getFileiId(), infor.getThreadId());
            } else {
                this.uploadList.add(infor);
                this.saveAll();
            }
        }
    }

    public synchronized UploadInfor getInforByfilePath(String filePath) {
        if(filePath != null && filePath != "") {
            if(this.uploadList == null) {
                return null;
            } else {
                UploadInfor infor = null;
                Iterator iterator = this.uploadList.iterator();

                while(iterator.hasNext() && this.uploadList.size() != 0) {
                    infor = (UploadInfor)iterator.next();
                    if(infor != null && infor.getFilePath().compareTo(filePath) == 0) {
                        return infor;
                    }
                }

                return null;
            }
        } else {
            return null;
        }
    }

    public synchronized UploadInfor getInforByfileId(String fildId) {
        if(fildId != null && fildId != "") {
            if(this.uploadList == null) {
                return null;
            } else {
                UploadInfor infor = null;
                Iterator iterator = this.uploadList.iterator();

                while(iterator.hasNext() && this.uploadList.size() != 0) {
                    infor = (UploadInfor)iterator.next();
                    if(infor != null && infor.getFileiId().compareTo(fildId) == 0) {
                        return infor;
                    }
                }

                return null;
            }
        } else {
            return null;
        }
    }

    public synchronized List<UploadInfor> getInforByfileIdList(String[] fileIds) {
        if(fileIds != null && fileIds.length >= 1) {
            if(this.uploadList == null) {
                return null;
            } else {
                ArrayList uploadInforList = new ArrayList();
                UploadInfor infor = null;
                Iterator iterator = this.uploadList.iterator();

                while(iterator.hasNext() && this.uploadList.size() != 0) {
                    infor = (UploadInfor)iterator.next();
                    if(infor != null) {
                        String[] var8 = fileIds;
                        int var7 = fileIds.length;

                        for(int var6 = 0; var6 < var7; ++var6) {
                            String id = var8[var6];
                            if(infor.getFileiId().equals(id)) {
                                uploadInforList.add(infor);
                                break;
                            }
                        }
                    }
                }

                return uploadInforList;
            }
        } else {
            return null;
        }
    }

    public synchronized List<UploadInfor> getInforByfileIdList(String fildId) {
        if(fildId != null && fildId != "") {
            if(this.uploadList == null) {
                return null;
            } else {
                ArrayList uploadInforList = new ArrayList();
                UploadInfor infor = null;
                Iterator iterator = this.uploadList.iterator();

                while(iterator.hasNext() && this.uploadList.size() != 0) {
                    infor = (UploadInfor)iterator.next();
                    if(infor != null && infor.getFileiId().compareTo(fildId) == 0) {
                        uploadInforList.add(infor);
                    }
                }

                return uploadInforList;
            }
        } else {
            return null;
        }
    }

    public synchronized UploadInfor getInforByThreadId(int threadId, String fileId) {
        if(this.uploadList == null) {
            return null;
        } else {
            UploadInfor infor = null;
            Iterator iterator = this.uploadList.iterator();

            while(iterator.hasNext() && this.uploadList.size() != 0) {
                infor = (UploadInfor)iterator.next();
                if(infor != null && infor.getThreadId() == threadId && infor.getFileiId().compareTo(fileId) == 0) {
                    return infor;
                }
            }

            return null;
        }
    }

    private void updateInfor(UploadInfor infor, String fileId, int threadId) {
        if(this.uploadList != null) {
            if(infor != null) {
                if(fileId != null && fileId != "") {
                    int index = 0;
                    UploadInfor myinfor = null;
                    Iterator iterator = this.uploadList.iterator();

                    while(iterator.hasNext() && this.uploadList.size() != 0) {
                        myinfor = (UploadInfor)iterator.next();
                        if(myinfor != null) {
                            if(myinfor.getFileiId().compareTo(fileId) == 0 && myinfor.getThreadId() == threadId) {
                                this.uploadList.set(index, infor);
                                this.saveAll();
                                break;
                            }

                            ++index;
                        }
                    }

                }
            }
        }
    }

    public synchronized void deleteInforList(String fileId) {
        if(this.uploadList != null) {
            if(fileId != null && fileId != "") {
                UploadInfor myinfor = null;
                Iterator iterator = this.uploadList.iterator();

                while(iterator.hasNext()) {
                    myinfor = (UploadInfor)iterator.next();
                    if(myinfor != null && myinfor.getFileiId().compareTo(fileId) == 0) {
                        iterator.remove();
                    }
                }

                this.saveAll();
            }
        }
    }

    public synchronized void deleteInforList(String[] fileIds) {
        if(this.uploadList != null) {
            if(fileIds != null && fileIds.length >= 1) {
                UploadInfor myinfor = null;
                Iterator iterator = this.uploadList.iterator();

                while(true) {
                    do {
                        if(!iterator.hasNext()) {
                            this.saveAll();
                            return;
                        }

                        myinfor = (UploadInfor)iterator.next();
                    } while(myinfor == null);

                    String[] var7 = fileIds;
                    int var6 = fileIds.length;

                    for(int var5 = 0; var5 < var6; ++var5) {
                        String id = var7[var5];
                        if(myinfor.getFileiId().compareTo(id) == 0) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }
}
