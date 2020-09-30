package com.example.gitstarscounter.di.module

import com.example.gitstarscounter.data.repository.remote.GithubApiService
import com.omega_r.base.remote.adapters.ImageAdapter
import com.omega_r.libs.omegatypes.image.Image
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
class GitApiModule {
    @Singleton
    @Provides
    fun gitApiService(retrofit: Retrofit): GithubApiService {
        return retrofit.create(GithubApiService::class.java)
    }

    @Singleton
    @Provides
    fun retrofit(moshiConvectorFactory: MoshiConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(moshiConvectorFactory)
            .build()
    }

    @Singleton
    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())//etc
            .add(Image::class.java, ImageAdapter().nullSafe())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun moshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }
}
