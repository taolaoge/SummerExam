package com.example.summerexam.extensions

import com.example.summerexam.network.DECRYPT_KEY
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */

fun String.decrypt():String {
    val raw = DECRYPT_KEY.toByteArray()
    val secretKeySpec = SecretKeySpec(raw, "AES")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    val encrypted = Base64.getDecoder().decode(this)
    val original = cipher.doFinal(encrypted)
    return String(original)
}