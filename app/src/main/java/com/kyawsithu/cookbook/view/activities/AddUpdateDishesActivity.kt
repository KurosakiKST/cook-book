package com.kyawsithu.cookbook.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.kyawsithu.cookbook.R
import com.kyawsithu.cookbook.databinding.ActivityAddUpdateDishesBinding
import com.kyawsithu.cookbook.databinding.DialogCustomImageSelectionBinding
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.kyawsithu.cookbook.application.CookBookApplication
import com.kyawsithu.cookbook.databinding.DialogCustomListBinding
import com.kyawsithu.cookbook.model.entites.CookBook
import com.kyawsithu.cookbook.utils.Constants
import com.kyawsithu.cookbook.view.adapters.CustomListItemAdapter
import com.kyawsithu.cookbook.viewmodel.CookBookViewModel
import com.kyawsithu.cookbook.viewmodel.CookBookViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishesActivity : AppCompatActivity(), View.OnClickListener
{
    private lateinit var addUpdateDishesBinding : ActivityAddUpdateDishesBinding
    private var mImagePath : String = ""

    private lateinit var mCustomListDialog : Dialog

    private val mCookBookViewModel : CookBookViewModel by viewModels {
        CookBookViewModelFactory((application as CookBookApplication).repository)
    }

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        addUpdateDishesBinding = ActivityAddUpdateDishesBinding.inflate(layoutInflater)
        setContentView(addUpdateDishesBinding.root)

        setupActionBar()

        addUpdateDishesBinding.ivAddDishImage.setOnClickListener(this)
        addUpdateDishesBinding.etType.setOnClickListener(this)
        addUpdateDishesBinding.etCategory.setOnClickListener(this)
        addUpdateDishesBinding.etCookingTime.setOnClickListener(this)
        addUpdateDishesBinding.btnAddDish.setOnClickListener(this)

    }

    private fun setupActionBar()
    {
        setSupportActionBar(addUpdateDishesBinding.toolbarAddUpdateDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addUpdateDishesBinding.toolbarAddUpdateDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(view : View?)
    {
        if (view != null)
        {
            when (view.id)
            {
                R.id.iv_add_dish_image ->
                {
                    customImageSelectionDialog()
                    return
                }

                R.id.et_type ->
                {
                    customItemsListsDialog(resources.getString(R.string.title_select_dish_type),
                        Constants.dishType(),
                        Constants.DISH_TYPE)
                    return
                }

                R.id.et_category ->
                {
                    customItemsListsDialog(resources.getString(R.string.title_select_dish_category),
                        Constants.dishCategory(),
                        Constants.DISH_CATEGORY)
                    return
                }

                R.id.et_cooking_time ->
                {
                    customItemsListsDialog(resources.getString(R.string.title_select_dish_cooking_time),
                        Constants.dishCookingTime(),
                        Constants.DISH_COOKING_TIME)
                    return
                }

                R.id.btn_add_dish ->
                {
                    val title = addUpdateDishesBinding.etTitle.text.toString().trim { it <= ' ' }
                    val type = addUpdateDishesBinding.etType.text.toString().trim { it <= ' ' }
                    val category = addUpdateDishesBinding.etCategory.text.toString()
                            .trim { it <= ' ' }
                    val ingredients = addUpdateDishesBinding.etIngredients.text.toString()
                            .trim { it <= ' ' }
                    val cookingTimeInMinutes = addUpdateDishesBinding.etCookingTime.text.toString()
                            .trim { it <= ' ' }
                    val cookingDirection = addUpdateDishesBinding.etDirectionToCook.text.toString()
                            .trim { it <= ' ' }

                    when
                    {
                        TextUtils.isEmpty(mImagePath) ->
                        {
                            Toast.makeText(this@AddUpdateDishesActivity,
                                resources.getString(R.string.err_msg_select_dish_image),
                                Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(title) ->
                        {
                            Toast.makeText(this@AddUpdateDishesActivity,
                                resources.getString(R.string.err_msg_enter_dish_title),
                                Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(type) ->
                        {
                            Toast.makeText(this@AddUpdateDishesActivity,
                                resources.getString(R.string.err_msg_select_dish_type),
                                Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(category) ->
                        {
                            Toast.makeText(this@AddUpdateDishesActivity,
                                resources.getString(R.string.err_msg_select_dish_category),
                                Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(ingredients) ->
                        {
                            Toast.makeText(this@AddUpdateDishesActivity,
                                resources.getString(R.string.err_msg_enter_dish_ingredients),
                                Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(cookingTimeInMinutes) ->
                        {
                            Toast.makeText(this@AddUpdateDishesActivity,
                                resources.getString(R.string.err_msg_select_dish_cooking_time),
                                Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(cookingDirection) ->
                        {
                            Toast.makeText(this@AddUpdateDishesActivity,
                                resources.getString(R.string.err_msg_enter_dish_cooking_directions),
                                Toast.LENGTH_SHORT).show()
                        }

                        else ->
                        {
                            val cookBookDetails = CookBook(
                                mImagePath,
                                Constants.DISH_IMAGE_SOURCE_LOCAL,
                                title,
                                type,
                                category,
                                ingredients,
                                cookingTimeInMinutes,
                                cookingDirection,
                                false
                                                          )

                            mCookBookViewModel.insert(cookBookDetails)

                            Toast.makeText(this@AddUpdateDishesActivity,
                                "Added Dish Successfully",
                                Toast.LENGTH_SHORT).show()
                            Log.i("DishInfo", cookBookDetails.toString())
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun customImageSelectionDialog()
    {
        val dialog = Dialog(this)
        val binding : DialogCustomImageSelectionBinding =
                DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
                                                    ).withListener(object : MultiplePermissionsListener
            {
                override fun onPermissionsChecked(report : MultiplePermissionsReport?)
                {
                    report?.let {
                        if (report.areAllPermissionsGranted())
                        {
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(cameraIntent, CAMERA)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                        permissions : MutableList<PermissionRequest>?,
                        token : PermissionToken?
                                                               )
                {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                   ).withListener(object : PermissionListener
            {

                override fun onPermissionGranted(p0 : PermissionGrantedResponse?)
                {
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                              )
                    startActivityForResult(galleryIntent, GALLERY)
                }

                override fun onPermissionDenied(p0 : PermissionDeniedResponse?)
                {
                    Toast.makeText(
                        this@AddUpdateDishesActivity,
                        "You have denied storage permission",
                        Toast.LENGTH_SHORT
                                  ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                        p0 : PermissionRequest?,
                        p1 : PermissionToken?
                                                               )
                {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        dialog.show()
    }

    fun selectedListItem(item : String, selection : String)
    {
        when (selection)
        {
            Constants.DISH_TYPE ->
            {
                mCustomListDialog.dismiss()
                addUpdateDishesBinding.etType.setText(item)
            }

            Constants.DISH_CATEGORY ->
            {
                mCustomListDialog.dismiss()
                addUpdateDishesBinding.etCategory.setText(item)
            }

            Constants.DISH_COOKING_TIME ->
            {
                mCustomListDialog.dismiss()
                addUpdateDishesBinding.etCookingTime.setText(item)
            }
        }
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == CAMERA)
            {
                data?.extras?.let {
                    val thumbnail : Bitmap = data.extras !!.get("data") as Bitmap
                    //addUpdateDishesBinding.ivDishImage.setImageBitmap(thumbnail)
                    Glide.with(this)
                            .load(thumbnail)
                            .centerCrop()
                            .into(addUpdateDishesBinding.ivDishImage)

                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("ImagePath", mImagePath)

                    addUpdateDishesBinding.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))
                }
            }
            else if (requestCode == GALLERY)
            {
                data?.let {
                    val selectedPhotoURI = data.data
                    //addUpdateDishesBinding.ivDishImage.setImageURI(selectedPhotoURI)
                    Glide.with(this)
                            .load(selectedPhotoURI)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object : RequestListener<Drawable>
                            {
                                override fun onLoadFailed(e : GlideException?, model : Any?, target : Target<Drawable>?, isFirstResource : Boolean) : Boolean
                                {
                                    Log.e("TAG", "Error loading image", e)
                                    return false
                                }

                                override fun onResourceReady(resource : Drawable?, model : Any?, target : Target<Drawable>?, dataSource : DataSource?, isFirstResource : Boolean) : Boolean
                                {
                                    resource?.let {
                                        val bitmap : Bitmap = resource.toBitmap()
                                        mImagePath = saveImageToInternalStorage(bitmap)
                                        Log.i("ImagePath", mImagePath)
                                    }
                                    return false
                                }

                            })
                            .into(addUpdateDishesBinding.ivDishImage)



                    addUpdateDishesBinding.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))
                }
            }
            else if (requestCode == Activity.RESULT_CANCELED)
            {
                Log.e("Cancel", "Image choosing cancel")
            }
        }
    }

    private fun showRationalDialogForPermissions()
    {
        AlertDialog.Builder(this)
                .setMessage(
                    "You haven't gave permissions required for this feature. " +
                    "It can be enabled under Application Setting"
                           )
                .setPositiveButton("Go to Setting") { _, _ ->
                    try
                    {
                        val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    catch (e : ActivityNotFoundException)
                    {
                        e.printStackTrace()
                    }
                }.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }.show()
    }

    private fun saveImageToInternalStorage(bitmap : Bitmap) : String
    {
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try
        {
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }
        catch (e : IOException)
        {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun customItemsListsDialog(title : String, itemsList : List<String>, selection : String)
    {
        mCustomListDialog = Dialog(this)
        val binding : DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adapter = CustomListItemAdapter(this, itemsList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    companion object
    {
        private const val CAMERA = 1
        private const val GALLERY = 2

        private const val IMAGE_DIRECTORY = "CookingRecipe"
    }
}