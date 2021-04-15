package com.lingyu.customViewDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}