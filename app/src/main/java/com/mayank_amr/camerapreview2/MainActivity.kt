package com.mayank_amr.camerapreview2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.mayank_amr.camerapreview2.IdConstant.AADHAR_HEIGHT
import com.mayank_amr.camerapreview2.IdConstant.AADHR_WIDTH
import com.mayank_amr.camerapreview2.IdConstant.Ids
import com.mayank_amr.camerapreview2.IdConstant.KEY_ID_HEIGHT
import com.mayank_amr.camerapreview2.IdConstant.KEY_ID_WIDTH
import com.mayank_amr.camerapreview2.IdConstant.OTHER_ID_HEIGHT
import com.mayank_amr.camerapreview2.IdConstant.OTHER_ID_WIDTH
import com.mayank_amr.camerapreview2.IdConstant.PASSBOOK_HEIGHT
import com.mayank_amr.camerapreview2.IdConstant.PASSBOOK_WIDTH
import com.mayank_amr.camerapreview2.IdConstant.PASSPORT_HEIGHT
import com.mayank_amr.camerapreview2.IdConstant.PASSPORT_WIDTH
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        ids_spinner_main.adapter = ArrayAdapter(this, R.layout.drop_down_iten, Ids)
        ids_spinner_main.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            1 -> startCameraActivity(AADHR_WIDTH, AADHAR_HEIGHT) //Aadhar
            2 -> startCameraActivity(PASSBOOK_WIDTH, PASSBOOK_HEIGHT) //Bank
            3 -> startCameraActivity(PASSPORT_WIDTH, PASSPORT_HEIGHT)// Passport
            4 -> startCameraActivity(OTHER_ID_WIDTH, OTHER_ID_HEIGHT)// Other
        }
    }

    private fun startCameraActivity(width: Float, height: Float) {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra(KEY_ID_WIDTH, width)
        intent.putExtra(KEY_ID_HEIGHT, height)
        startActivity(intent)
        //finish()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}