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

import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivityStudentUploadBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.example.collegeconnect.databinding.ActivityStudentRegistrationBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class student_upload : AppCompatActivity() {
    private var isImageSelected = false
    private lateinit var dbref: DatabaseReference
    private lateinit var binding : ActivityStudentUploadBinding
    private lateinit var studentRegistrationBinding: ActivityStudentRegistrationBinding
    private lateinit var etsemester : TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_upload)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_student_upload)
//
        changeStatusBarColor()

        binding.toolbar.setNavigationOnClickListener {

            startActivity(Intent(this,Student_dashboard::class.java))
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        val role = intent.getStringExtra("role")
        dbref = FirebaseDatabase.getInstance().getReference("Student")

        val etdb: TextInputEditText = binding.studentregistration.etdb
        etdb.setOnClickListener { showDatePickerDialog() }

        setupUI()
        setupNameValidation()
        binding.studentregistration.submitbtn.setOnClickListener{

            val email =binding.studentregistration.etemail.text.toString()


            val updatedUser = Users(
                    username = binding.studentregistration.etname.text.toString(),
            email = binding.studentregistration.etemail.text.toString(),
            number = binding.studentregistration.etnumber.text.toString(),
            rollno = binding.studentregistration.etrollno.text.toString(),
            dob = binding.studentregistration.etdb.text.toString() ,
            gender = binding.studentregistration.etgender.text.toString(),
            session = binding.studentregistration.etsession.text.toString(),
            semester = binding.studentregistration.etsemester.text.toString(),
            branch = binding.studentregistration.etbranch.text.toString(),
            password = binding.studentregistration.etpassword.text.toString(),
            image = binding.studentregistration.imageFileNameTextView.text.toString()
            )

            updateStudentData(email,updatedUser)

        }

        setupImageSelector()

        binding.studentregistration.etsemester.setOnClickListener {
            semDropdown(it)
        }

        binding.studentregistration.etbranch.setOnClickListener {
            branchDropdown(it)
        }
        binding.studentregistration.etgender.setOnClickListener {
            genderDropdown(it)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

private fun updateStudentData(
    email: String,
    updatedUser:Users
) {
    val database = FirebaseDatabase.getInstance().getReference("Student")


    database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                for (userSnapshot in snapshot.children) {

                    userSnapshot.ref.setValue(updatedUser).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(this@student_upload, getString(R.string.data_updated), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@student_upload, getString(R.string.fail_update), Toast.LENGTH_SHORT).show()
                        }
                    }
                    binding.studentregistration.etname.text?.clear()
                    binding.studentregistration.etemail.text?.clear()
                    binding.studentregistration.etnumber.text?.clear()
                    binding.studentregistration.etrollno.text?.clear()
                    binding.studentregistration.etdb.text?.clear()
                    binding.studentregistration.etgender.text?.clear()
                    binding.studentregistration.etsession.text?.clear()
                    binding.studentregistration.etsemester.text?.clear()
                    binding.studentregistration.etbranch.text?.clear()
                    binding.studentregistration.etpassword.text?.clear()
                    binding.studentregistration.etconfirmpass.text?.clear()
                    binding.studentregistration.imageFileNameTextView.text = ""



                }
            } else {
                Toast.makeText(this@student_upload, getString(R.string.user_with_email), Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(this@student_upload, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
    data class Users(
        val username: String =" ",
        val email: String = " ",
        val number: String = " ",
        val rollno: String =" ",
        val dob: String =" ",
        val gender: String =" ",
        val session: String= "",
        val semester: String =" ",
        val branch: String =" ",
        val password: String =" ",
        val image: String = ""
    ) {

    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.studentregistration.etdb.setText(date)
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
            binding.studentregistration.etsemester.setText(menuItem.title)
            binding.studentregistration.etsemester.error = null
            true
        }
        popupMenu.show()
    }

    private fun genderDropdown(view : View){
        val popupMenu = PopupMenu(this, view)
        val options = resources.getStringArray(R.array.gender)

        options.forEach { option ->
            popupMenu.menu.add(option)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.studentregistration.etgender.setText(menuItem.title)
            binding.studentregistration.etgender.error=null
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
            binding.studentregistration.etbranch.setText(menuItem.title)
            true
        }

        popupMenu.show()
    }

    private fun setupUI() {
        // Prevent spaces in input fields
        val noSpaceInputFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.toString().contains(" ")) {
                // Set error on respective TextInputLayout
                if (binding.studentregistration.etname.hasFocus()) {
                    binding.studentregistration.name.error = getString(R.string.space_name)
                } else if (binding.studentregistration.etnumber.hasFocus()) {
                    binding.studentregistration.number.error = getString(R.string.space_number)
                } else if (binding.studentregistration.etrollno.hasFocus()) {
                    binding.studentregistration.rollno.error = getString(R.string.space_roono)
                } else if (binding.studentregistration.etsession.hasFocus()) {
                    binding.studentregistration.session.error = getString(R.string.space_session)
                }
                ""
            } else {
                null
            }
        }
        binding.studentregistration.etname.filters = arrayOf(noSpaceInputFilter)
        binding.studentregistration.etnumber.filters = arrayOf(noSpaceInputFilter)
        binding.studentregistration.etrollno.filters = arrayOf(noSpaceInputFilter)
        binding.studentregistration.etsession.filters = arrayOf(noSpaceInputFilter)

        binding.studentregistration.etname.addTextChangedListener(createTextWatcher(binding.studentregistration.etname))
        binding.studentregistration.etemail.addTextChangedListener(createEmailTextWatcher())
        binding.studentregistration.etnumber.filters = arrayOf(InputFilter.LengthFilter(10))
        binding.studentregistration.etnumber.addTextChangedListener(createTextWatcher(binding.studentregistration.etnumber))
        binding.studentregistration.etrollno.filters = arrayOf(InputFilter.LengthFilter(12))
        binding.studentregistration.etrollno.addTextChangedListener(createTextWatcher(binding.studentregistration.etrollno))
        binding.studentregistration.etsession.filters = arrayOf(InputFilter.LengthFilter(9))
        binding.studentregistration.etsession.addTextChangedListener(createTextWatcher(binding.studentregistration.etsession))
        binding.studentregistration.etpassword.filters = arrayOf(InputFilter.LengthFilter(12))
        binding.studentregistration.etpassword.addTextChangedListener(createPasswordTextWatcher())
        binding.studentregistration.etconfirmpass.addTextChangedListener(createConfirmPasswordTextWatcher())

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
                    binding.studentregistration.email.error = valid_email
                } else {
                    binding.studentregistration.email.error = null
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
                    binding.studentregistration.password.error = valid_password
                } else {
                    binding.studentregistration.password.error = null
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
                val pass = binding.studentregistration.etpassword.text.toString().trim()
                if (TextUtils.isEmpty(confirmPass) || confirmPass != pass) {
                    binding.studentregistration.etconfirmpass.error = match_pass
                } else {
                    binding.studentregistration.etconfirmpass.error = null
                }
            }
        }
    }

    private fun setupNameValidation() {
        val nameEditText: TextInputEditText = binding.studentregistration.etname

        nameEditText.addTextChangedListener(object : TextWatcher {

            private val handler = Handler(Looper.getMainLooper())
            private var errorRunnable: Runnable? = null
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.any { it.isDigit() }) {
                    binding.studentregistration.name.error = getString(R.string.valid_name)

                    val filteredText = s.filter { !it.isDigit() }
                    nameEditText.setText(filteredText)

                    nameEditText.setSelection(filteredText.length)

                } else {
                    binding.studentregistration.name.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }
    private fun setupImageSelector() {
        binding.studentregistration.Bselectimage.setOnClickListener {
            openGallery()
            binding.studentregistration.errorTextView.visibility = View.GONE
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
                binding.studentregistration.imageFileNameTextView.text = fileName
                binding.studentregistration.imageFileNameTextView.visibility = View.VISIBLE
                binding.studentregistration.errorTextView.visibility = View.GONE
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

        binding.studentregistration.submitbtn.setOnClickListener {
            if (areFieldsValid()) {
//                encryption()

                startActivity(intent)
                Toast.makeText(this,getString(R.string.data_updated), Toast.LENGTH_SHORT).show()

                val intent = Intent(applicationContext, Student_dashboard::class.java)
                startActivity(intent)
            }
        }
    }



    private fun areFieldsValid(): Boolean {

        val match_pass = getString(R.string.match_pass)
        val valid_email = getString(R.string.valid_email)
        val valid_password = getString(R.string.valid_pass)


        var isValid = true

//        for name
        val name = binding.studentregistration.etname.text.toString().trim()
        if (TextUtils.isEmpty(name)) {
            binding.studentregistration.name.error = getString(R.string.empty_name)

        } else if (!isValidname(name)) {
            binding.studentregistration.name.error = getString(R.string.valid_name)
            isValid = false
        } else {
            binding.studentregistration.name.error = null // Clear error if valid
        }


//        for email
        if (TextUtils.isEmpty(
                binding.studentregistration.etemail.text.toString().trim()) ) {
            binding.studentregistration.email.error = getString(R.string.empty_email)

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.studentregistration.etemail.text.toString().trim()).matches()){
            binding.studentregistration.email.error = valid_email
            isValid = false
        }
        else{
            binding.studentregistration.email.error=null
        }


//        for contact
        if (TextUtils.isEmpty(binding.studentregistration.etnumber.text.toString().trim())) {
            binding.studentregistration.number.error = getString(R.string.empty_number)
            isValid = false
        }
        else{
            binding.studentregistration.number.error=null
        }



        //        for rollno
        if (TextUtils.isEmpty(binding.studentregistration.etrollno.text.toString().trim())) {
            binding.studentregistration.rollno.error = getString(R.string.empty_rollno)
            isValid = false
        }
        else{
            binding.studentregistration.rollno.error= null

        }


//        for dob
        if (TextUtils.isEmpty(binding.studentregistration.etdb.text.toString().trim())) {
            binding.studentregistration.dob.error = getString(R.string.empty_dob)
            isValid = false
        }
        else{
            binding.studentregistration.etdb.error=null
        }



//        for gender
        if (binding.studentregistration.etgender.text.isNullOrEmpty()) {
            binding.studentregistration.gender.error = getString(R.string.empty_gender)
            isValid = false
        }
        else{
            binding.studentregistration.gender.error =null

        }

        //        for session
        if (TextUtils.isEmpty(binding.studentregistration.etsession.text.toString().trim())) {
            binding.studentregistration.session.error = getString(R.string.empty_session)
            isValid = false
        }
        else{
            binding.studentregistration.session.error=null

        }



//        for semester
        if (binding.studentregistration.etsemester.text.isNullOrEmpty()) {
            binding.studentregistration.semester.error = getString(R.string.empty_semester)
            isValid = false
        }
        else {
            binding.studentregistration.etsemester.error = null
        }

//
//        for branch
        if (binding.studentregistration.etbranch.text.isNullOrEmpty()) {
            binding.studentregistration.branch.error =getString(R.string.empty_branch)
            isValid = false
        }
        else{
            binding.studentregistration.branch.error=null

        }



//        for password
        if (TextUtils.isEmpty(binding.studentregistration.etpassword.text.toString().trim())) {
            binding.studentregistration.password.error = getString(R.string.empty_pass)
            isValid = false
        }
        else if (!isValidPassword(binding.studentregistration.etpassword.text.toString().trim())){
            binding.studentregistration.password.error=valid_password

        }

        else{
            binding.studentregistration.etpassword.error=null
        }


//        for confirm password
        if (TextUtils.isEmpty(binding.studentregistration.etconfirmpass.text.toString().trim()) || binding.studentregistration.etconfirmpass.text.toString().trim() != binding.studentregistration.etpassword.text.toString().trim()) {
            binding.studentregistration.confirmpass.error = match_pass
            isValid = false
        }
        else{
            binding.studentregistration.confirmpass.error =null
        }

//        for image
        if (!isImageSelected) {
            binding.studentregistration.errorTextView.text = getString(R.string.img_required)
            binding.studentregistration.errorTextView.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.studentregistration.errorTextView.visibility = View.GONE

        }
        return isValid
    }

    private fun isValidname(etname: String): Boolean {

        val nameRegex = "^[a-zA-Z]+$"
        return etname.matches(nameRegex.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,12}\$"
        return password.matches(passwordRegex.toRegex())
    }


}
