package com.example.news.component.aliyunoss

import android.content.Context
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.example.news.BuildConfig
import com.example.news.config.Config
import com.example.news.util.SuperUUIDUtil
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.StringUtils
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AliyunOSSService private constructor(val context: Context) {

    private var client: OSSClient

    /**
     * 上传单个文件
     * @return 上传成功返回文件名
     * 上传失败，抛出异常
     */
    suspend fun upload(data: String): String =
        withContext(Dispatchers.Default) {
            suspendCoroutine { continuation ->
                try {//上传
                    //OSS如果没有特殊需求建议不要分目录
                    //如果一定要分不要让目录名前面连续
                    //例如时间戳倒过来
                    //不然连续请求达到一定量级会有性能影响
                    //https://help.aliyun.com/document_detail/64945.html

                    val target =
                        String.format(
                            "news/uploads/%s%s.jpg",
                            SuperUUIDUtil.uuid,
                            BuildConfig.FLAVOR
                        )

                    //创建上传文件请求
                    //上传其他文件也是这样的
                    //他不关心文件具体内容
                    val request = PutObjectRequest(
                        Config.ALIYUN_OSS_BUCKET_NAME,
                        target,
                        data
                    )

                    //上传
                    val putObjectResult: PutObjectResult = client.putObject(request)

                    //上传成功
                    continuation.resume(target)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
        }

    fun upload(data: List<LocalMedia>): Flow<UploadResult<List<String>>> {
        return flow<UploadResult<List<String>>> {
//            emit(UploadResult.onProgress(0))
//            delay(1000)
//            emit(UploadResult.onProgress(3))
//            delay(1000)
//            emit(UploadResult.Success(Arrays.asList("1","2")))

            //创建结果数组
            val results = mutableListOf<String>()

            data.forEachIndexed { index, it ->
//                Log.d("TAG", "upload: "+it.realPath+","+it.compressPath)
                emit(UploadResult.onProgress(index))

                val target =
                    String.format("news/uploads/%s%s.jpg", SuperUUIDUtil.uuid, BuildConfig.FLAVOR)

                val request = PutObjectRequest(
                    Config.ALIYUN_OSS_BUCKET_NAME,
                    target,
                    if (StringUtils.isNotBlank(it.compressPath)) it.compressPath else it.realPath
                )
                val putObjectResult: PutObjectResult = client.putObject(request)
                results.add(target)
            }

            emit(UploadResult.Success(results))
        }.flowOn(Dispatchers.IO) //通过flowOn方法切换到io线程
    }

    init {
        //推荐使用OSSAuthCredentialsProvider
        //因为他有token过期可以及时更新

        //请勿泄漏该key
        //和非法使用
        val credentialProvider = OSSPlainTextAKSKCredentialProvider(
            Config.ALIYUN_AK,
            Config.ALIYUN_SK
        )

        //该配置类如果不设置
        //会有默认配置
        //具体可看该类
        var config = ClientConfiguration()

        //连接超时，默认15秒
        config.setConnectionTimeout(15 * 1000)

        //socket超时，默认15秒

        config.setSocketTimeout(15 * 1000)

        //最大并发请求数，默认5个
        config.setMaxConcurrentRequest(5)

        //失败后最大重试次数，默认2次

        config.setMaxErrorRetry(2)

        if (Config.DEBUG) {
            //这个开启会支持写入手机sd卡中的一份日志文件位置在SDCard_path\OSSLog\logs.csv
            OSSLog.enableLog()
        }

        //初始化oss客户端
        client = OSSClient(
            context, Config.BUCKET_ENDPOINT,
            credentialProvider
        )
    }

    companion object {
        private var instance: AliyunOSSService? = null

        fun getInstance(context: Context): AliyunOSSService {
            return instance ?: synchronized(this) {
                instance ?: AliyunOSSService(context).also { instance = it }
            }
        }
    }

}