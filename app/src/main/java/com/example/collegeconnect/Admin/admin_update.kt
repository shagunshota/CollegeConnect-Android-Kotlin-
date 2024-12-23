package com.example.collegeconnect.Admin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_dashboard
import com.example.collegeconnect.databinding.ActivityAdminUpdateBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class admin_update : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_update)

        binding = DataBindingUtil.setContentView( this,R.layout.activity_admin_update)
        changeStatusBarColor()
        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, Admin_dashboard::class.java))
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setupUI()
//        setupNameValidation()
        setupSubmitButton()
//        binding.adminregistration.adetbranch.setOnClickListener {
//            branchDropdown(it)
//        }
        binding.toolbar.setNavigationOnClickListener {

            startActivity(Intent(this, Admin_dashboard::class.java))

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

//    private fun branchDropdown(view: View) {
//        val popupMenu = PopupMenu(this, view)
//        val options = resources.getStringArray(R.array.select_branch)
//
//        options.forEach { option ->
//            popupMenu.menu.add(option)
//        }
//
//        popupMenu.setOnMenuItemClickListener { menuItem ->
//            binding.adminregistration.adetbranch.setText(menuItem.title)
//            true
//        }
//
//        popupMenu.show()
//    }
    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

    }
    private fun setupUI() {
        val noSpaceInputFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.toString().contains(" ")) {
//                if (binding.adminregistration.adetname.hasFocus()) {
//                    binding.adminregistration.adname.error = getString(R.string.space_name)
//                }
                ""
            } else {
                null
            }
        }

//        binding.adminregistration.adetname.filters = arrayOf(noSpaceInputFilter)
//        binding.adminregistration.adetname.addTextChangedListener(createTextWatcher(binding.adminregistration.adetname))
        binding.adminregistration.adetemail.addTextChangedListener(createEmailTextWatcher())
//        binding.adminregistration.adetnumber.filters = arrayOf(InputFilter.LengthFilter(10))
//        binding.adminregistration.adetnumber.addTextChangedListener(createTextWatcher(binding.adminregistration.adetnumber))
        binding.adminregistration.adetpassword.filters = arrayOf(InputFilter.LengthFilter(12))
        binding.adminregistration.adetpassword.addTextChangedListener(createPasswordTextWatcher())
        binding.adminregistration.adetconfirmpass.addTextChangedListener(createConfirmPasswordTextWatcher())

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
                    binding.adminregistration.ademail.error = valid_email
                } else {
                    binding.adminregistration.ademail.error = null
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
                    binding.adminregistration.adpassword.error = valid_password
                } else {
                    binding.adminregistration.adpassword.error = null
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
                val pass = binding.adminregistration.adetpassword.text.toString().trim()
                if (TextUtils.isEmpty(confirmPass) || confirmPass != pass) {
                    binding.adminregistration.adetconfirmpass.error = match_pass
                } else {
                    binding.adminregistration.adetconfirmpass.error = null
                }
            }
        }
    }
    private fun setupSubmitButton() {

        binding.adminregistration.tsubmitbtn.setOnClickListener{
            val email = binding.adminregistration.adetemail.text.toString()
            val password = binding.adminregistration.adetpassword.text.toString()
//            val username = binding.adminregistration.adetname.text.toString()
//            val number = binding.adminregistration.adetnumber.text.toString()

            if (areFieldsValid()) {
                val updatedUser = Users(
//                    username = binding.adminregistration.adetname.text.toString(),
                    email = binding.adminregistration.adetemail.text.toString(),
                    password = binding.adminregistration.adetpassword.text.toString(),
//                    number = binding.adminregistration.adetnumber.text.toString()



                )
                updateAdmin(email,updatedUser)

            }
        }
    }

    private fun updateAdmin(email: String, updatedUser:Users) {
        val database = FirebaseDatabase.getInstance().getReference("Admin")


        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {

                        userSnapshot.ref.setValue(updatedUser).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(this@admin_update, getString(R.string.data_updated), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@admin_update, getString(R.string.fail_update), Toast.LENGTH_SHORT).show()
                            }

//                            binding.adminregistration.adetname.text?.clear()
                            binding.adminregistration.adetemail.text?.clear()
//                            binding.adminregistration.adetnumber.text?.clear()
                            binding.adminregistration.adetpassword.text?.clear()
                        }


                    }
                } else {
                    Toast.makeText(this@admin_update, getString(R.string.user_with_email), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@admin_update, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    data class Users(
        val username: String =" ",
        val email: String =" ",
        val password: String =" " ,
        val number: String  =" "
    )


//
//    private fun setupNameValidation() {
//        val nameEditText: TextInputEditText = binding.adminregistration.adetname
//
//        nameEditText.addTextChangedListener(object : TextWatcher {
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s != null && s.any { it.isDigit() }) {
//                    binding.adminregistration.adname.error = getString(R.string.valid_name)
//
//                    // Remove any digit characters
//                    val filteredText = s.filter { !it.isDigit() }
//                    nameEditText.setText(filteredText)
//
//                    nameEditText.setSelection(filteredText.length)
//
//                } else {
//                    binding.adminregistration.adname.error = null
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
//        val name = binding.adminregistration.adetname.text.toString().trim()
//        if (TextUtils.isEmpty(name)) {
//            binding.adminregistration.adname.error = getString(R.string.empty_name)
//
//        } else if (!isValidname(name)) {
//            binding.adminregistration.adname.error = getString(R.string.valid_name)
//            isValid = false
//        } else {
//            binding.adminregistration.adname.error = null // Clear error if valid
//        }


//        for email
        if (TextUtils.isEmpty(
                binding.adminregistration.adetemail.text.toString().trim()) ) {
            binding.adminregistration.ademail.error = getString(R.string.empty_email)

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.adminregistration.adetemail.text.toString().trim()).matches()){
            binding.adminregistration.ademail.error = valid_email
            isValid = false
        }
        else{
            binding.adminregistration.ademail.error=null
        }


//        for contact
//        if (TextUtils.isEmpty(binding.adminregistration.adetnumber.text.toString().trim())) {
//            binding.adminregistration.adnumber.error = getString(R.string.empty_number)
//            isValid = false
//        }
//        else{
//            binding.adminregistration.adnumber.error=null
//        }
//
//
////        for branch
//        if (binding.adminregistration.adetbranch.text.isNullOrEmpty()) {
//            binding.adminregistration.adbranch.error =getString(R.string.empty_branch)
//            isValid = false
//        }
//        else{
//            binding.adminregistration.adbranch.error=null
//
//        }


//        for password
        if (TextUtils.isEmpty(binding.adminregistration.adetpassword.text.toString().trim())) {
            binding.adminregistration.adpassword.error = getString(R.string.empty_pass)
            isValid = false
        }
        else if (!isValidPassword(binding.adminregistration.adetpassword.text.toString().trim())){
            binding.adminregistration.adpassword.error=valid_password

        }

        else{
            binding.adminregistration.adetpassword.error=null
        }


//        for confirm password
        if (TextUtils.isEmpty(binding.adminregistration.adetconfirmpass.text.toString().trim()) || binding.adminregistration.adetconfirmpass.text.toString().trim() != binding.adminregistration.adetpassword.text.toString().trim()) {
            binding.adminregistration.adconfirmpass.error = match_pass
            isValid = false
        }
        else{
            binding.adminregistration.adconfirmpass.error =null
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