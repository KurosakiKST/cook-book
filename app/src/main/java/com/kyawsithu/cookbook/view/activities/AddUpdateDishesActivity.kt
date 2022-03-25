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
import com.kyawsithu.cookbook.model.entities.CookBook
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
    private lateinit var binding : ActivityAddUpdateDishesBinding
    private var mImagePath : String = ""

    private lateinit var mCustomListDialog : Dialog

    private var mCookBookDetails : CookBook? = null

    private val mCookBookViewModel : CookBookViewModel by viewModels {
        CookBookViewModelFactory((application as CookBookApplication).repository)
    }

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_DISH_DETAILS))
        {
            mCookBookDetails = intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
        }

        mCookBookDetails?.let {
            if (it.id != 0)
            {
                mImagePath = it.image
                Glide.with(this@AddUpdateDishesActivity)
                        .load(mImagePath)
                        .centerCrop()
                        .into(binding.ivDishImage)

                binding.etTitle.setText(it.title)
                binding.etType.setText(it.type)
                binding.etCategory.setText(it.category)
                binding.etIngredients.setText(it.ingredients)
                binding.etCookingTime.setText(it.cookingTime)
                binding.etDirectionToCook.setText(it.directionToCook)

                binding.btnAddDish.text = resources.getString(R.string.lbl_update_dish)
            }
        }

        binding.ivAddDishImage.setOnClickListener(this)
        binding.etType.setOnClickListener(this)
        binding.etCategory.setOnClickListener(this)
        binding.etCookingTime.setOnClickListener(this)
        binding.btnAddDish.setOnClickListener(this)

    }

    private fun setupActionBar()
    {
        if (mCookBookDetails != null && mCookBookDetails !!.id != 0)
        {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_dish)
            }
        }
        else
        {
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_dish)
            }
        }
        setSupportActionBar(binding.toolbarAddUpdateDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddUpdateDishActivity.setNavigationOnClickListener {
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
                    val title = binding.etTitle.text.toString().trim { it <= ' ' }
                    val type = binding.etType.text.toString().trim { it <= ' ' }
                    val category = binding.etCategory.text.toString()
                            .trim { it <= ' ' }
                    val ingredients = binding.etIngredients.text.toString()
                            .trim { it <= ' ' }
                    val cookingTimeInMinutes = binding.etCookingTime.text.toString()
                            .trim { it <= ' ' }
                    val cookingDirection = binding.etDirectionToCook.text.toString()
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
                            var dishID = 0
                            var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL
                            var favouriteDish = false

                            mCookBookDetails?.let {
                                if (it.id != 0)
                                {
                                    dishID = it.id
                                    imageSource = it.imageSource
                                    favouriteDish = it.favouriteDish
                                }
                            }

                            val cookBookDetails = CookBook(
                                mImagePath,
                                imageSource,
                                title,
                                type,
                                category,
                                ingredients,
                                cookingTimeInMinutes,
                                cookingDirection,
                                favouriteDish,
                                dishID
                                                          )

                            if (dishID == 0)
                            {
                                mCookBookViewModel.insert(cookBookDetails)

                                Toast.makeText(this@AddUpdateDishesActivity,
                                    "Added Dish Successfully",
                                    Toast.LENGTH_SHORT).show()
                            }else{
                                mCookBookViewModel.update(cookBookDetails)
                                Toast.makeText(this@AddUpdateDishesActivity,
                                    "Updated Dish Successfully",
                                    Toast.LENGTH_SHORT).show()
                            }
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
                binding.etType.setText(item)
            }

            Constants.DISH_CATEGORY ->
            {
                mCustomListDialog.dismiss()
                binding.etCategory.setText(item)
            }

            Constants.DISH_COOKING_TIME ->
            {
                mCustomListDialog.dismiss()
                binding.etCookingTime.setText(item)
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
                            .into(binding.ivDishImage)

                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("ImagePath", mImagePath)

                    binding.ivAddDishImage.setImageDrawable(
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
                            .into(binding.ivDishImage)



                    binding.ivAddDishImage.setImageDrawable(
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