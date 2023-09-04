package com.example.log

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        findViewById<Button>(R.id.button).setOnClickListener {
            Logger.e("zzzzzzzzzzzzzzzzzzzzz")
            Logger.json("{\"glossary\": {\"title\": \"example glossary\", \"GlossDiv\": {\"title\": \"S\",\"GlossList\": {\"GlossEntry\": {\"ID\": \"SGML\",\"SortAs\": \"SGML\",\"GlossTerm\": \"Standard Generalized Markup Language\",\"Acronym\": \"SGML\",\"Abbrev\": \"ISO 8879:1986\",\"GlossDef\": {\"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\": [\"GML\", \"XML\"]},\"GlossSee\": \"markup\"}}}}}")
        }
    }
}