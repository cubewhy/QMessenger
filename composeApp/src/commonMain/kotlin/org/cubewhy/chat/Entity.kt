package org.cubewhy.chat

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlin.random.Random

// https://github.com/cubewhy/QMsgBackend/tree/master/src/main/java/org/cubewhy/chat/entity

@Serializable
data class RestBean<T>(
    val code: Int,
    val data: T?,
    val message: String
)

@Serializable
data class Authorize(
    val username: String,
    val token: String,
    val email: String,
    val roles: Set<Role>,
    val expire: Long
)

@Serializable
data class Role(
    val id: Long,
    val name: String,
    val description: String,
    val permissions: Set<Permission>
)

enum class Permission(val type: Type) {
    // servlet admin rights
    DASHBOARD(Type.SERVLET), // 访问后端的仪表盘
    MANAGE_USER(Type.SERVLET), // 管理所有用户
    MANAGE_ROLES(Type.CHANNEL_AND_SERVLET), // 管理身份组
    MANAGE_FILES(Type.CHANNEL_AND_SERVLET), // 管理用户上传的文件
    REGISTER_INVITE(Type.SERVLET), // 生成注册邀请

    // servlet admin & channel admin rights
    MANAGE_CHANNEL(Type.CHANNEL_AND_SERVLET), // 管理频道
    DISBAND_CHANNEL(Type.CHANNEL_AND_SERVLET), // 解散频道
    KICK_USERS(Type.CHANNEL_AND_SERVLET), // 频道内为踢出成员,服务器内为注销账户

    // channel admin rights
    PROCESS_JOIN_REQUEST(Type.CHANNEL), // 处理加频道请求

    // user permissions
    SEND_MESSAGE(Type.CHANNEL_AND_SERVLET), // 发送消息
    CREATE_CHANNEL(Type.SERVLET), // 创建频道
    JOIN_CHANNEL(Type.SERVLET), // 加入频道
    VIEW_CHANNEL(Type.CHANNEL), // 查看消息
    SEND_CHANNEL_INVITE(Type.CHANNEL), // 发送加频道邀请
    UPLOAD_FILES(Type.CHANNEL_AND_SERVLET), // 上传文件
    DOWNLOAD_FILES(Type.CHANNEL_AND_SERVLET); // 下载文件

    enum class Type {
        CHANNEL, // 群组权限
        SERVLET, // (全局) 服务器权限
        CHANNEL_AND_SERVLET // 重合
    }
}

@Serializable
data class CheckStatus(
    val serverName: String,
    val timestamp: Long,
    val impl: String,
    val motd: Motd? = null
)

@Serializable
data class Motd(
    val title: String,
    val text: String
)

@Serializable
data class RegisterInfo(
    val username: String,
    val password: String,
    val email: String,

    val nickname: String,
    val bio: String,

    val inviteCode: String?
)

@Serializable
data class Account(
    val id: Long,
    val username: String,
    val nickname: String,
    val avatarHash: String?,
    val email: String,
    val bio: String?,

    val registerTime: Long,
    val updatedTime: Long,
    val roles: List<String>
)

@Serializable
data class UpdateFirebaseToken(
    val token: String
)

@Serializable
data class WebsocketResponse<T>(
    val method: String,
    val data: T?
) {
    companion object {
        const val NEW_MESSAGE: String = "nmsg"
    }
}

@Serializable
data class WebsocketRequest<T>(
    val method: String,
    val data: T?
) {

    companion object {
        const val SEND_MESSAGE: String = "smsg"
    }
}

@Serializable
data class Channel(
    val id: Long,

    val name: String,
    var title: String?,
    var description: String,

    val iconHash: String? = null,
    var publicChannel: Boolean,
    val decentralized: Boolean,

    val createdAt: Long,
    val memberCount: Long
)

@Serializable
data class ChannelDTO(
    val name: String,
    val title: String,
    val description: String,
    val iconHash: String? = null,

    val publicChannel: Boolean = false,
    val decentralized: Boolean = false
)

@Serializable
data class ChatMessage(
    val id: Long,
    val channel: Channel,
    val sender: Sender,
    val shortContent: String,
    val content: List<JsonObject>,
    val timestamp: Long,
) {
    companion object {
        const val TEXT: String = "t"
    }
}

@Serializable
data class Sender(
    val id: Long,
    val nickname: String,
    val username: String
)

fun generateColorFromStringLength(username: String): Long {
    // 获取字符串长度
    val length = username.length

    // 计算随机颜色的种子值
    val seed = length * 1234567L

    // 使用种子值生成随机颜色
    val random = Random(seed)
    val red = random.nextInt(255)
    val green = random.nextInt(255)
    val blue = random.nextInt(255)

    // 将RGB颜色转换为整数形式
    return ((red shl 16) or (green shl 8) or blue).toLong()
}

@Serializable
data class ChatMessageDTO(
    val channel: Long,
    val shortContent: String,
    val content: List<JsonElement>
)

object MessageType {
    const val TEXT = "t"
}

@Serializable
abstract class BaseMessage {
    abstract val data: String
    abstract val type: String
}

@Serializable
class TextMessage(override val data: String, override var type: String) : BaseMessage()

@Serializable
data class ChannelConfInfo(
    var nickname: String,
    val permissions: Set<Permission>
)

@Serializable
data class UpdateChannelNickname(
    val nickname: String
)

@Serializable
data class UpdateChannelDescription(
    val description: String
)

@Serializable
data class UpdateChannelVisible(
    val visible: Boolean
)

@Serializable
data class UpdateChannelTitle(
    val title: String
)