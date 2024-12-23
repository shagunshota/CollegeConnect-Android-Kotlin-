package com.example.collegeconnect.Student

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.Models.Student
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivityStudentRegistrationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import kotlin.random.Random

class Student_Registration : AppCompatActivity() {
    private lateinit var binding: ActivityStudentRegistrationBinding
    private lateinit var auth : FirebaseAuth
    private var isImageSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_registration)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_registration)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        auth = FirebaseAuth.getInstance()

        val etdb: TextInputEditText = binding.etdb
        etdb.setOnClickListener { showDatePickerDialog() }
        setupUI()
        setupNameValidation()
        changeStatusBarColor()
        setupImageSelector()
        setupSubmitButton()
        binding.back.setOnClickListener {
            startActivity(Intent(this, Login_Activity::class.java))
            finish()
        }
        binding.etsemester.setOnClickListener {
            semDropdown(it)
        }

        binding.etbranch.setOnClickListener {
            branchDropdown(it)
        }
        binding.etgender.setOnClickListener {
            genderDropdown(it)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etdb.setText(date)
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun semDropdown(view: View) {
        val popupMenu = PopupMenu(this, view)
        val options = resources.getStringArray(R.array.select_sem)

        options.forEach { option ->
            popupMenu.menu.add(option)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.etsemester.setText(menuItem.title)
            binding.etsemester.error = null
            true
        }
        popupMenu.show()
    }
    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
    }


        private fun genderDropdown(view: View) {
        val popupMenu = PopupMenu(this, view)
        val options = resources.getStringArray(R.array.gender)

        options.forEach { option ->
            popupMenu.menu.add(option)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.etgender.setText(menuItem.title)
            binding.etgender.error = null
            true
        }

        popupMenu.show()
    }

    private fun branchDropdown(view: View) {
        val popupMenu = PopupMenu(this, view)
        val options = resources.getStringArray(R.array.select_branch)

        options.forEach { option ->
            popupMenu.menu.add(option)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.etbranch.setText(menuItem.title)
            true
        }

        popupMenu.show()
    }

    private fun setupUI() {
        // Prevent spaces in input fields
        val noSpaceInputFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.toString().contains(" ")) {
                // Set error on respective TextInputLayout
                if (binding.etname.hasFocus()) {
                    binding.name.error = getString(R.string.space_name)
                } else if (binding.etnumber.hasFocus()) {
                    binding.number.error = getString(R.string.space_number)
                } else if (binding.etrollno.hasFocus()) {
                    binding.rollno.error = getString(R.string.space_roono)
                } else if (binding.etsession.hasFocus()) {
                    binding.session.error = getString(R.string.space_session)
                }
                ""
            } else {
                null
            }
        }
        binding.etname.filters = arrayOf(noSpaceInputFilter)
        binding.etnumber.filters = arrayOf(noSpaceInputFilter)
        binding.etrollno.filters = arrayOf(noSpaceInputFilter)
        binding.etsession.filters = arrayOf(noSpaceInputFilter)

        binding.etname.addTextChangedListener(createTextWatcher(binding.etname))
        binding.etemail.addTextChangedListener(createEmailTextWatcher())
        binding.etnumber.filters = arrayOf(InputFilter.LengthFilter(10))
        binding.etnumber.addTextChangedListener(createTextWatcher(binding.etnumber))
        binding.etrollno.filters = arrayOf(InputFilter.LengthFilter(12))
        binding.etrollno.addTextChangedListener(createTextWatcher(binding.etrollno))
        binding.etsession.filters = arrayOf(InputFilter.LengthFilter(9))
        binding.etsession.addTextChangedListener(createTextWatcher(binding.etsession))
        binding.etpassword.filters = arrayOf(InputFilter.LengthFilter(12))
        binding.etpassword.addTextChangedListener(createPasswordTextWatcher())
        binding.etconfirmpass.addTextChangedListener(createConfirmPasswordTextWatcher())

    }

    private fun createTextWatcher(textInputLayout: TextInputEditText): TextWatcher {
        val required = getString(R.string.required)
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p: Editable?) {
                if (TextUtils.isEmpty(p.toString().trim())) {
//
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
                    binding.email.error = valid_email
                } else {
                    binding.email.error = null
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
                    binding.password.error = valid_password
                } else {
                    binding.password.error = null
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
                val pass = binding.etpassword.text.toString().trim()
                if (TextUtils.isEmpty(confirmPass) || confirmPass != pass) {
                    binding.etconfirmpass.error = match_pass
                } else {
                    binding.etconfirmpass.error = null
                }
            }
        }
    }

    private fun setupNameValidation() {
        val nameEditText: TextInputEditText = binding.etname

        nameEditText.addTextChangedListener(object : TextWatcher {

            private val handler = Handler(Looper.getMainLooper())
            private var errorRunnable: Runnable? = null
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.any { it.isDigit() }) {
                    binding.name.error = getString(R.string.valid_name)


                    val filteredText = s.filter { !it.isDigit() }
                    nameEditText.setText(filteredText)

                    nameEditText.setSelection(filteredText.length)

                } else {
                    binding.name.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }


    private fun setupImageSelector() {
        binding.Bselectimage.setOnClickListener {
            openGallery()
            binding.errorTextView.visibility = View.GONE
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let {
                val fileName = getFileName(it)
                binding.imageFileNameTextView.text = fileName
                binding.imageFileNameTextView.visibility = View.VISIBLE
                binding.errorTextView.visibility = View.GONE
                isImageSelected = true
            }
        } else {

            isImageSelected = false
        }
    }
    private fun getFileName(uri: Uri?): String {
        var fileName = "Unknown"
        val cursor = uri?.let { contentResolver.query(it, null, null, null, null) }
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }
    private fun setupSubmitButton() {

        binding.submitbtn.setOnClickListener {
            val username = binding.etname.text.toString()
            val email = binding.etemail.text.toString()
            val number = binding.etnumber.text.toString()
            val rollno = binding.etrollno.text.toString()
            val dob = binding.etdb.text.toString()
            val gender = binding.etgender.text.toString()
            val session= binding.etsession.text.toString()
            val semester = binding.etsemester.text.toString()
            val branch = binding.etbranch.text.toString()
            val password = binding.etpassword.text.toString()
            val image = binding.imageFileNameTextView.text.toString()
            if (areFieldsValid()) {
                registerStudent( username ,email,number,rollno,dob,gender, session, semester, branch,password, image)
            }

        }
    }


    private fun registerStudent(username: String, email: String, number: String, rollno: String, dob: String, gender: String, session: String, semester: String, branch: String, password: String, image: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val database =
                        FirebaseDatabase.getInstance().getReference("Student").child(userId)


                    val user = Users(username,email,number,rollno,dob, gender, session,semester,branch,password,image)
                    database.setValue(user).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            Toast.makeText(this, getString(R.string.registration_sucess), Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, Login_Activity::class.java)

                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, getString(R.string.error_saving_user_data), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    data class Users(
        val username: String,
        val email: String,
        val number: String,
        val rollno: String,
        val dob: String,
        val gender: String,
        val session: String,
        val semester: String,
        val branch: String,
        val password: String,
        val image: String
    ) {
        constructor() : this("", "", "", "", "", "", "", "", "", "", "")

    }

   


    private fun areFieldsValid(): Boolean {

        val match_pass = getString(R.string.match_pass)
        val valid_email = getString(R.string.valid_email)
        val valid_password = getString(R.string.valid_pass)


        var isValid = true

//        for name
        val name = binding.etname.text.toString().trim()
        if (TextUtils.isEmpty(name)) {
            binding.name.error = getString(R.string.empty_name)

        } else if (!isValidname(name)) {
            binding.name.error = getString(R.string.valid_name)
            isValid = false
        } else {
            binding.name.error = null // Clear error if valid
        }


//        for email
        if (TextUtils.isEmpty(
                binding.etemail.text.toString().trim()) ) {
            binding.email.error = getString(R.string.empty_email)

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etemail.text.toString().trim()).matches()){
            binding.email.error = valid_email
            isValid = false
        }
        else{
            binding.email.error=null
        }


//        for contact
        if (TextUtils.isEmpty(binding.etnumber.text.toString().trim())) {
            binding.number.error = getString(R.string.empty_number)
            isValid = false
        }
        else{
            binding.number.error=null
        }



        //        for rollno
        if (TextUtils.isEmpty(binding.etrollno.text.toString().trim())) {
            binding.rollno.error = getString(R.string.empty_rollno)
            isValid = false
        }
        else{
            binding.rollno.error= null

        }


//        for dob
        if (TextUtils.isEmpty(binding.etdb.text.toString().trim())) {
            binding.dob.error = getString(R.string.empty_dob)
            isValid = false
        }
        else{
            binding.etdb.error=null
        }



//        for gender
        if (binding.etgender.text.isNullOrEmpty()) {
            binding.gender.error = getString(R.string.empty_gender)
            isValid = false
        }
        else{
            binding.gender.error =null

        }

        //        for session
        if (TextUtils.isEmpty(binding.etsession.text.toString().trim())) {
            binding.session.error = getString(R.string.empty_session)
            isValid = false
        }
        else{
            binding.session.error=null

        }



//        for semester
        if (binding.etsemester.text.isNullOrEmpty()) {
            binding.semester.error = getString(R.string.empty_semester)
            isValid = false
        }
        else {
            binding.etsemester.error = null
        }

//
//        for branch
        if (binding.etbranch.text.isNullOrEmpty()) {
            binding.branch.error =getString(R.string.empty_branch)
            isValid = false
        }
        else{
            binding.branch.error=null

        }





//        for password
        if (TextUtils.isEmpty(binding.etpassword.text.toString().trim())) {
            binding.password.error = getString(R.string.empty_pass)
            isValid = false
        }
        else if (!isValidPassword(binding.etpassword.text.toString().trim())){
            binding.password.error=valid_password

        }

        else{
            binding.etpassword.error=null
        }


//        for confirm password
        if (TextUtils.isEmpty(binding.etconfirmpass.text.toString().trim()) || binding.etconfirmpass.text.toString().trim() != binding.etpassword.text.toString().trim()) {
            binding.confirmpass.error = match_pass
            isValid = false
        }
        else{
            binding.confirmpass.error =null
        }

//        for image
        if (!isImageSelected) {
            binding.errorTextView.text = getString(R.string.img_required) // Use a resource string for the error message
            binding.errorTextView.visibility = View.VISIBLE // Show error message
            isValid = false
        } else {
            binding.errorTextView.visibility = View.GONE // Hide error message if valid
            // Proceed with further actions
        }
        return isValid
    }

    private fun isValidname(etname: String): Boolean {
        // Regex to ensure at least one lowercase and one uppercase letter, and no digits
        val nameRegex = "^[a-zA-Z]+$"
        return etname.matches(nameRegex.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,12}\$"
        return password.matches(passwordRegex.toRegex())
    }





}


