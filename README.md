## 此为爱奇艺视频托管androidsdk源码.(拷贝自libiqiyiandroidvcopsdk.jar)

**修复部分问题.
- 去除httpClient,更改为httpUrlConnection.
- 更改RandomAccessFile ~~rwd~~ 为 r 只读
- 更改threadCount为2,trackError判读错误的情况.


### Issue
- 重复上传同一视频文件,后续上传审核不通过的情况.
获取视频接口  http://openapi.iqiyi.com/api/file/fullStatus?access_token=ACCESS_TOKEN&file_id=FILE_ID 

如果该视频是提示“重复上传“，调用该接口时可以返回原来已上传的fileid。

比如我上传了两个相同的视频，具体信息如下：
```java
    UID：1198012180
    4b3cb6b2695c49c1835fb3a7d685b1ff （这个是被判断为重复上传的）
    2446a63ab8e0475ca7e0d41a102dbfe2 （这个是已经发布了的）
    该接口返回的内容如下：
    {
        data: {
            is_repeat: 1,
            fileIdBefore: "2446a63ab8e0475ca7e0d41a102dbfe2"
        },
        code: "A00007"
    }
```
即只管上传,需要播放的时候,根据fileId请求*视频接口*是否存在fileIdBefore(能够播放的fileId).


### 非官方(侵删)