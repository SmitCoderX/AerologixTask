package com.smitcoderx.task.aerologixtask.UI

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.smitcoderx.task.aerologixtask.Model.Data
import com.smitcoderx.task.aerologixtask.UI.Adapter.MainItemAdapter
import com.smitcoderx.task.aerologixtask.Utils.AerologixApplication
import com.smitcoderx.task.aerologixtask.Utils.Constants.TAG
import com.smitcoderx.task.aerologixtask.Utils.Convertor
import com.smitcoderx.task.aerologixtask.Utils.Resources
import com.smitcoderx.task.aerologixtask.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelProviderFactory(
            application as AerologixApplication,
            (application as AerologixApplication).repository
        )
    }
    private val permission = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
    private lateinit var itemAdapter: MainItemAdapter
    private val data = mutableListOf<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission(100)

        itemAdapter = MainItemAdapter()

        binding.rvItemList.apply {
            setHasFixedSize(false)
            adapter = itemAdapter
        }
        loadData()
        binding.swipeRefresh.setOnRefreshListener {
            val job = mainViewModel.refreshed()
            binding.swipeRefresh.isRefreshing = true
            if (job.isCompleted) {
                mainViewModel.apiData.observe(this) {
                    itemAdapter.differ.submitList(it)
                    binding.swipeRefresh.isRefreshing = false
                }
            }

            /*itemAdapter.differ.submitList(data)
            itemAdapter.notifyDataSetChanged()*/
        }



        binding.fabSave.setOnClickListener {
            if (data.isNotEmpty()) {
                val status = storeFile("Employees")
                if (status) {
                    binding.fabAdd.collapse()
                    Snackbar.make(binding.cooMain, "File Saved", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.cooMain, "Something Went Wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            } else {
                Snackbar.make(binding.cooMain, "No Data to Store", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun loadData() {
        mainViewModel.data.observe(this) { result ->
            result.data?.let { data.addAll(it) }
            handleUI(result)
        }
    }

    private fun handleUI(result: Resources<List<Data>>) {
        binding.apply {
            itemAdapter.differ.submitList(result.data)

            progress.isVisible = result is Resources.Loading && result.data.isNullOrEmpty()
            if (result is Resources.Error && result.data.isNullOrEmpty()) {
                Snackbar.make(binding.cooMain, result.message.toString(), Snackbar.LENGTH_SHORT)
                    .show()
                Log.d(TAG, "onCreate: ${result.message}")
            }
        }
    }

    private fun checkPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission[0]
            ) == PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(
                this,
                permission[1]
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(
                this,
                permission.toTypedArray(), requestCode
            )
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i(TAG, "Permission: Granted")
        } else {
            Log.i(TAG, "Permission: Denied")
        }
    }

    private fun storeFile(name: String): Boolean {
        var status = false
        val item = Convertor().toDataJson(data)
        Log.d(TAG, "storeFile: $item")
        val resolver = applicationContext.contentResolver
        val values = ContentValues()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
            uri?.let { it ->
                resolver.openOutputStream(it).use {
                    // Write file
                    it?.write(item.toByteArray(Charset.defaultCharset()))
                    it?.close()
                }
                status = true
            }
        } else {
            val rootPath = "/storage/emulated/0/Download/"
            val myFile = File(rootPath, name)
            val outputStream: FileOutputStream
            try {
                if (myFile.createNewFile()) {
                    outputStream = FileOutputStream(myFile, true)
                    outputStream.write(item.toByteArray())
                    outputStream.flush()
                    outputStream.close()
                    status = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                status = true
            }
        }
        return status
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

}