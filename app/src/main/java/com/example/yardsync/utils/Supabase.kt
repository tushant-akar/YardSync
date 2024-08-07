package com.example.yardsync.utils

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlin.time.Duration.Companion.seconds

object Supabase {
    val client = createSupabaseClient(
        supabaseUrl = "https://yzrvmkpvfywqiajqzbke.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl6cnZta3B2Znl3cWlhanF6YmtlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjI4OTI0MjAsImV4cCI6MjAzODQ2ODQyMH0.PKWu0t_ZgjQ8YFD5q1afa0JukTodr-6fJaVUFf1PDbY"
    ) {
        install(Postgrest)
        install(Storage) {
            transferTimeout = 90.seconds
        }
        install(Auth)
    }
}