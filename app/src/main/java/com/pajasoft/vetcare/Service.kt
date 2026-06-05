package com.pajasoft.vetcare

import android.annotation.SuppressLint
import android.content.Context
import com.pajasoft.vetcare.Data.local.TokenManager
import com.pajasoft.vetcare.Data.remote.RetrofitClient
import com.pajasoft.vetcare.Data.repository.VetRepository

object ServiceLocator {

    @SuppressLint("StaticFieldLeak")
    lateinit var tokenManager: TokenManager
        private set
    lateinit var repository: VetRepository
        private set

    fun init(context: Context) {
        tokenManager = TokenManager(context.applicationContext)
        val api = RetrofitClient.create(tokenManager)
        repository = VetRepository(api, tokenManager)
    }
}