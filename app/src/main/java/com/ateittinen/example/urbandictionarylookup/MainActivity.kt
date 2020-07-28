package com.ateittinen.example.urbandictionarylookup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add (R.id.layoutForFragment, LookupFragment()).commit()
    }
}