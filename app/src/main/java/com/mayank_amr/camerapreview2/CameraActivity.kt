package com.mayank_amr.camerapreview2

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mayank_amr.camerapreview2.IdConstant.Ids
import com.mayank_amr.camerapreview2.IdConstant.KEY_ID_HEIGHT
import com.mayank_amr.camerapreview2.IdConstant.KEY_ID_WIDTH
import com.mayank_amr.camerapreview2.IdConstant.changeId
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var overlayView: OverlayWithRectangle
    private var idWidth: Float = 0.0f
    private var idHeight: Float = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        supportActionBar?.hide()

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        val bundle = intent.extras
        if (bundle != null) {
            idWidth = bundle.getFloat(KEY_ID_WIDTH)
            idHeight = bundle.getFloat(KEY_ID_HEIGHT)
        }

        overlayView = OverlayWithRectangle(this, idWidth, idHeight)
        framecontainer.addView(overlayView)


        // Set up the listener for take photo button
        camera_capture_button.setOnClickListener {
            takePhoto()
        }


        ids_spinner_camera.adapter = ArrayAdapter(this, R.layout.drop_down_iten, changeId)
        ids_spinner_camera.onItemSelectedListener = this


//        flash_light_button.setOnClickListener {
//            // turn on/off flash light.
//        }

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case..
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has been taken..
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            /*
             * https://developer.android.com/training/camerax/configuration
             */
//            val imageCapture = ImageCapture.Builder()
//            .setFlashMode(...)
//            .setTargetAspectRatio(...)
//            .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                //cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(KEY_ID_WIDTH, idWidth)
        outState.putFloat(KEY_ID_HEIGHT, idWidth)
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            1 -> updateScannerViewRatio(IdConstant.AADHR_WIDTH, IdConstant.AADHAR_HEIGHT) //Aadhar
            2 -> updateScannerViewRatio(IdConstant.PASSBOOK_WIDTH, IdConstant.PASSBOOK_HEIGHT) //Bank
            3 -> updateScannerViewRatio(IdConstant.PASSPORT_WIDTH, IdConstant.PASSPORT_HEIGHT)// Passport
            4 -> updateScannerViewRatio(IdConstant.OTHER_ID_WIDTH, IdConstant.OTHER_ID_HEIGHT)// Other
        }
    }

    private fun updateScannerViewRatio(width: Float, height: Float) {
        framecontainer.removeView(overlayView)
        overlayView = OverlayWithRectangle(this, width, height)
        framecontainer.addView(overlayView)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}

