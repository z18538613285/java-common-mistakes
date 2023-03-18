package org.geekbang.time.commonmistakes.apidesign.apiasyncsyncmode;

import lombok.Data;
import lombok.RequiredArgsConstructor;
//syncQueryUploadTask接口出参
@Data
@RequiredArgsConstructor
public class SyncQueryUploadTaskResponse {
    //任务ID
    private final String taskId;
    //原始文件下载URL
    private String downloadUrl;
    //缩略图下载URL
    private String thumbnailDownloadUrl;
}
