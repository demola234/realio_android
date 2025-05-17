package com.realio.app.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.realio.app.core.network.ApiLoggingInterceptor
import com.realio.app.feature.authentication.data.api.AuthApi
import com.realio.app.feature.authentication.data.datasource.local.TokenStorage
import com.realio.app.feature.authentication.data.datasource.local.TokenStorageImpl
import com.realio.app.feature.authentication.data.datasource.remote.AuthDataSource
import com.realio.app.feature.authentication.data.datasource.remote.AuthDataSourceImpl
import com.realio.app.feature.authentication.data.repository.AuthRepositoryImpl
import com.realio.app.feature.authentication.domain.repository.AuthRepository
import com.realio.app.feature.authentication.domain.usecases.GetUserDetailsUseCase
import com.realio.app.feature.authentication.domain.usecases.LoginUseCase
import com.realio.app.feature.authentication.domain.usecases.LogoutUseCase
import com.realio.app.feature.authentication.domain.usecases.OAuthLoginUseCase
import com.realio.app.feature.authentication.domain.usecases.RegisterUseCase
import com.realio.app.feature.authentication.domain.usecases.ResendOtpUseCase
import com.realio.app.feature.authentication.domain.usecases.VerifyOtpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(ApiLoggingInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                    HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideAuthDataSource(authApi: AuthApi): AuthDataSource {
        return AuthDataSourceImpl(authApi)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<androidx.datastore.preferences.core.Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("auth_preferences")
        }
    }

    @Provides
    @Singleton
    fun provideTokenStorage(dataStore: DataStore<androidx.datastore.preferences.core.Preferences>): TokenStorage {
        return TokenStorageImpl(dataStore)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataSource: AuthDataSource,
        tokenStorage: TokenStorage
    ): AuthRepository {
        return AuthRepositoryImpl(authDataSource, tokenStorage)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Provides
    fun provideVerifyOtpUseCase(authRepository: AuthRepository): VerifyOtpUseCase {
        return VerifyOtpUseCase(authRepository)
    }

    @Provides
    fun provideResendOtpUseCase(authRepository: AuthRepository): ResendOtpUseCase {
        return ResendOtpUseCase(authRepository)
    }

    @Provides
    fun provideOAuthLoginUseCase(authRepository: AuthRepository): OAuthLoginUseCase {
        return OAuthLoginUseCase(authRepository)
    }

    @Provides
    fun provideGetUserDetailsUseCase(authRepository: AuthRepository): GetUserDetailsUseCase {
        return GetUserDetailsUseCase(authRepository)
    }
}