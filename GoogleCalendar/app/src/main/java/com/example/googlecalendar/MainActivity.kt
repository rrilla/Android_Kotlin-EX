package com.example.googlecalendar

import android.accounts.AccountManager
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.googlecalendar.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.api.services.calendar.Calendar
//import com.google.api.client.util.ExponentialBackOff

import android.app.ProgressDialog
import android.renderscript.Element

import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
//import com.google.api.client.json.gson.GsonFactory
import java.util.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    val TAG = "SignInActivity";
    lateinit var binding: ActivityMainBinding

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    //  gso에서 지정한 옵션을 사용하여 GoogleSignInClient 빌드.
    private val gsc by lazy {
        GoogleSignIn.getClient(this, gso)
    }

    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                task.getResult(ApiException::class.java)?.let { account->
//                    viewModel.saveToken(account.idToken ?: throw Exception())
                    Log.e("han", "tostring : ${account.email} ")
                } ?: throw Exception()
            } catch (e: ApiException) {
                Log.w(TAG, "signInResult:failed code=" + e.statusCode);
                //updateUI(null)
            }
        }
    }

    //  Google Calendar API에 접근하기 위해 사용되는 구글 캘린더 API 서비스 객체
//    lateinit var mService: Calendar
//    private val mService: Calendar by lazy {
//        Calendar.Builder
//            Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signIn()
    }

    fun signIn() {
        loginLauncher.launch(gsc.signInIntent)
        Log.e("han", "signIn 실행")
    }

    override fun onStart() {
        super.onStart()

        // 사용자가 이미 로그인한 경우 기존 Google 로그인 계정을 확인합니다.
        // GoogleSignInAccount는 Null이 아닙니다.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val data = """
            email - ${account?.email}
            1- ${account?.account}
            2- ${account?.displayName}
            3- ${account?.id}
            4- ${account?.idToken}
            5- ${account?.familyName}
            6- ${account?.photoUrl}
            7- ${account?.serverAuthCode}
            8- ${account?.grantedScopes}
        """.trimIndent()
        Log.e("onStart", data)
    }

}