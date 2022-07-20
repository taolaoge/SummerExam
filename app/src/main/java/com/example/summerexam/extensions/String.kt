package com.example.summerexam.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.summerexam.network.DECRYPT_KEY
import okio.ByteString.Companion.decodeBase64
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */

fun String.decrypt1():String{
    val raw = DECRYPT_KEY.toByteArray()
    val secretKeySpec = SecretKeySpec(raw, "AES")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    val encrypted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val new = this.split(",")
        Base64.getDecoder().decode(new[0].replace("ftp://","").trim())
    } else {
        return ""
    }
    val original = cipher.doFinal(encrypted)
    val s=String(original)
    return s
}

fun String.decrypt():String {
    if (this == "") return ""
    val new = this.split(",")
    val newString = new[0].replace("ftp://","")
    val raw = DECRYPT_KEY.toByteArray()
    val secretKeySpec = SecretKeySpec(raw, "AES")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    val encrypted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Base64.getDecoder().decode(newString)
    } else {
        return ""
    }
    val original = cipher.doFinal(encrypted)
    return String(original)
}