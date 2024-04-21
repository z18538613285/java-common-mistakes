package org.geekbang.time.commonmistakes.apidesign.apiasyncsyncmode;

import lombok.Data;
import lombok.RequiredArgsConstructor;

//syncQueryUploadTask接口入参
@Data
@RequiredArgsConstructor
public class SyncQueryUploadTaskRequest {
    //使用上传文件任务ID查询上传结果
    private final String taskId;
}
