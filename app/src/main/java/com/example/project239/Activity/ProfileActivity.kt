
package com.example.project239.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.project239.Helper.setupBottomNavigation
import com.example.project239.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var isEditing = false

    private val prefs by lazy {
        getSharedPreferences(
            "CoffeeShopProfile",
            MODE_PRIVATE
        )
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            binding.profileImage.setImageURI(it)
            prefs.edit().putString("profile_image", it.toString()).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadProfile()

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        binding.saveBtn.setOnClickListener {
            saveProfile()
        }

        binding.profileImage.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        setupBottomNavigation(this, binding.bottomMenu, "profile")
    }

    override fun onResume() {
        super.onResume()
        setupBottomNavigation(this, binding.bottomMenu, "profile")
    }

    private fun loadProfile() {

        val name = prefs.getString("name", "") ?: ""
        val email = prefs.getString("email", "") ?: ""
        val phone = prefs.getString("phone", "") ?: ""
        val address = prefs.getString("address", "") ?: ""
        val imageUriString = prefs.getString("profile_image", null)

        if (imageUriString != null) {
            try {
                binding.profileImage.setImageURI(Uri.parse(imageUriString))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.nameInput.setText(name)
        binding.emailInput.setText(email)
        binding.phoneInput.setText(phone)
        binding.addressInput.setText(address)

        updateHeader(name, email)
        
        toggleEditMode(name.isEmpty())
    }

    private fun toggleEditMode(enable: Boolean) {
        isEditing = enable

        binding.nameInput.isEnabled = enable
        binding.emailInput.isEnabled = enable
        binding.phoneInput.isEnabled = enable
        binding.addressInput.isEnabled = enable
        binding.profileImage.isClickable = enable

        if (enable) {
            binding.saveBtn.text = "Save Profile"
        } else {
            binding.saveBtn.text = "Edit Profile"
        }
    }

    private fun saveProfile() {
        if (!isEditing) {
            toggleEditMode(true)
            return
        }

        val name = binding.nameInput.text.toString().trim()
        val email = binding.emailInput.text.toString().trim()
        val phone = binding.phoneInput.text.toString().trim()
        val address = binding.addressInput.text.toString().trim()

        if (name.isEmpty()) {
            binding.nameInput.error = "Enter your name"
            binding.nameInput.requestFocus()
            return
        }

        if (email.isNotEmpty() &&
            !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            binding.emailInput.error = "Enter a valid email"
            binding.emailInput.requestFocus()
            return
        }

        prefs.edit()
            .putString("name", name)
            .putString("email", email)
            .putString("phone", phone)
            .putString("address", address)
            .apply()

        updateHeader(name, email)
        toggleEditMode(false)

        Toast.makeText(
            this,
            "Profile Updated Successfully",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateHeader(
        name: String,
        email: String
    ) {

        binding.displayNameTxt.text =
            name.ifEmpty { "Coffee Lover" }

        binding.displayEmailTxt.text =
            email.ifEmpty { "No email added" }
    }
}
