package com.example.collegeconnect.Admin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.Data
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Login.Login_Activity


import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivityAdminRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import java.util.Date
import kotlin.random.Random

class Admin_register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityAdminRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_register)
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_admin_register)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setupUI()
//        setupNameValidation()
        setupSubmitButton()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }



    private fun setupUI() {
        val noSpaceInputFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.toString().contains(" ")) {
//                if (binding.adetname.hasFocus()) {
//                    binding.adname.error = getString(R.string.space_name)
//                }
                ""
            } else {
                null
            }
        }
//
//        binding.adetname.filters = arrayOf(noSpaceInputFilter)
//        binding.adetname.addTextChangedListener(createTextWatcher(binding.adetname))
        binding.adetemail.addTextChangedListener(createEmailTextWatcher())
//        binding.adetnumber.filters = arrayOf(InputFilter.LengthFilter(10))
//        binding.adetnumber.addTextChangedListener(createTextWatcher(binding.adetnumber))
        binding.adetpassword.filters = arrayOf(InputFilter.LengthFilter(12))
        binding.adetpassword.addTextChangedListener(createPasswordTextWatcher())
        binding.adetconfirmpass.addTextChangedListener(createConfirmPasswordTextWatcher())

    }
    private fun createTextWatcher(textInputLayout: TextInputEditText): TextWatcher {
        val required = getString(R.string.required)
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p: Editable?) {
                if (TextUtils.isEmpty(p.toString().trim())) {
                } else {
                    textInputLayout.error = null
                }
            }
        }
    }
    private fun createEmailTextWatcher(): TextWatcher {
        val valid_email = getString(R.string.valid_email)
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p: Editable?) {
                val email = p.toString().trim()
                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.ademail.error = valid_email
                } else {
                    binding.ademail.error = null
                }
            }
        }
    }
    private fun createPasswordTextWatcher(): TextWatcher {
        val valid_password = getString(R.string.valid_pass)
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val pass = s.toString().trim()
                if (TextUtils.isEmpty(pass) || !isValidPassword(pass)) {
                    binding.adpassword.error = valid_password
                } else {
                    binding.adpassword.error = null
                }
            }



            private fun isValidPassword(password: String): Boolean {
                val passwordRegex =
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,12}\$"
                return password.matches(passwordRegex.toRegex())
            }
        }
    }

    private fun createConfirmPasswordTextWatcher(): TextWatcher {
        val match_pass = getString(R.string.match_pass)
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val confirmPass = s.toString().trim()
                val pass = binding.adetpassword.text.toString().trim()
                if (TextUtils.isEmpty(confirmPass) || confirmPass != pass) {
                    binding.adetconfirmpass.error = match_pass
                } else {
                    binding.adetconfirmpass.error = null
                }
            }
        }
    }

    private fun setupSubmitButton() {

        binding.tsubmitbtn.setOnClickListener{
            val email = binding.adetemail.text.toString()
            val password = binding.adetpassword.text.toString()
//            val username = binding.adetname.text.toString()
//            val number = binding.adetnumber.text.toString()
//            val branch = binding .adetbranch.text.toString()

            if (areFieldsValid()) {
//                registerAdmin(email, password, username, number,branch)
                registerAdmin(email, password)
            }
        }
    }
private fun registerAdmin(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val uniqueId = generateUniqueId()


                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                val database = FirebaseDatabase.getInstance().getReference("Admin").child(userId)

                val user = User( email,password ,uniqueId)
                database.setValue(user).addOnCompleteListener { dbTask ->
                    if (dbTask.isSuccessful) {
                        Toast.makeText(this, getString(R.string.registration_sucess), Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, Login_Activity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.error_saving_user_data), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}


    private fun generateUniqueId(): String {
        return Random.nextInt(100000, 999999).toString()

    }

    data class User(

        val email: String = " ",
        val password: String=" ",

        val uniqueId: String =" "


    )



//    private fun setupNameValidation() {
////        val nameEditText: TextInputEditText = binding.adetname
//
//        nameEditText.addTextChangedListener(object : TextWatcher {
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s != null && s.any { it.isDigit() }) {
//                    binding.adname.error = getString(R.string.valid_name)
//
//                    val filteredText = s.filter { !it.isDigit() }
//                    nameEditText.setText(filteredText)
//
//                    nameEditText.setSelection(filteredText.length)
//
//                } else {
//                    binding.adname.error = null
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//
//    }

    private fun isValidname(etname: String): Boolean {

        val nameRegex = "^[a-zA-Z]+$"
        return etname.matches(nameRegex.toRegex())
    }
    private fun areFieldsValid(): Boolean {

        val match_pass = getString(R.string.match_pass)
        val valid_email = getString(R.string.valid_email)
        val valid_password = getString(R.string.valid_pass)

        var isValid = true

//        for name
//        val name = binding.adetname.text.toString().trim()
//        if (TextUtils.isEmpty(name)) {
//            binding.adname.error = getString(R.string.empty_name)
//
//        } else if (!isValidname(name)) {
//            binding.adname.error = getString(R.string.valid_name)
//            isValid = false
//        } else {
//            binding.adname.error = null // Clear error if valid
//        }


//        for email
        if (TextUtils.isEmpty(
                binding.adetemail.text.toString().trim()) ) {
            binding.ademail.error = getString(R.string.empty_email)

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.adetemail.text.toString().trim()).matches()){
            binding.ademail.error = valid_email
            isValid = false
        }
        else{
            binding.ademail.error=null
        }


//        for contact
//        if (TextUtils.isEmpty(binding.adetnumber.text.toString().trim())) {
//            binding.adnumber.error = getString(R.string.empty_number)
//            isValid = false
//        }
//        else{
//            binding.adnumber.error=null
//        }


//        for branch
//        if (binding.adetbranch.text.isNullOrEmpty()) {
//            binding.adbranch.error =getString(R.string.empty_branch)
//            isValid = false
//        }
//        else{
//            binding.adbranch.error=null
//
//        }

//        for password
        if (TextUtils.isEmpty(binding.adetpassword.text.toString().trim())) {
            binding.adpassword.error = getString(R.string.empty_pass)
            isValid = false
        }
        else if (!isValidPassword(binding.adetpassword.text.toString().trim())){
            binding.adpassword.error=valid_password

        }

        else{
            binding.adetpassword.error=null
        }


//        for confirm password
        if (TextUtils.isEmpty(binding.adetconfirmpass.text.toString().trim()) || binding.adetconfirmpass.text.toString().trim() != binding.adetpassword.text.toString().trim()) {
            binding.adconfirmpass.error = match_pass
            isValid = false
        }
        else{
            binding.adconfirmpass.error =null
        }

//        for image

        return isValid
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,12}\$"
        return password.matches(passwordRegex.toRegex())
    }





}