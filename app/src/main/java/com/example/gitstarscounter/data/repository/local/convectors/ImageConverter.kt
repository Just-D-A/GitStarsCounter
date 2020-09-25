package com.example.gitstarscounter.data.repository.local.convectors

import androidx.room.TypeConverter
import com.omega_r.libs.omegatypes.image.Image
import com.omega_r.libs.omegatypes.image.UrlImage
import com.omega_r.libs.omegatypes.image.from

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object ImageConverter {
    @JvmStatic
    @TypeConverter
    fun convertImageToString(image: Image): String {
        return (image as UrlImage).url
    }

    @JvmStatic
    @TypeConverter
    fun convertTimestampToDate(imageURL: String): Image {
        return Image.from(imageURL)
    }
}
