package com.fkurt.smscatch

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fkurt.smscatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.RECEIVE_SMS,android.Manifest.permission.SEND_SMS),
            111)
        }
        else
        {
            receiveMsg()
        }

        binding.btnSend.setOnClickListener {
            val sms:SmsManager=SmsManager.getDefault()
            sms.sendTextMessage(binding.editMobileNo.text.toString(),"ME",binding.editEnterText.text.toString(),null,null)
        }

    }

    private fun receiveMsg() {
        var br=object:BroadcastReceiver()
        {
            @SuppressLint("ObsoleteSdkInt")
            override fun onReceive(p0: Context?, p1: Intent?) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    for(sms:SmsMessage in Telephony.Sms.Intents.getMessagesFromIntent(p1))
                    {
                        binding.editMobileNo.setText(sms.originatingAddress)
                        binding.editEnterText.setText(sms.displayMessageBody)
                    }

                }
            }

        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            receiveMsg()
    }
}