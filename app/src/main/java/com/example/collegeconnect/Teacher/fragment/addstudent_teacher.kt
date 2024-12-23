package com.example.collegeconnect.Teacher.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.collegeconnect.R

class addstudent_teacher : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {



                Column(modifier =Modifier.fillMaxSize().padding(5.dp)
                    .verticalScroll(rememberScrollState()),
                  ) {

                    val name = remember{mutableStateOf("")}
                    val email = remember{mutableStateOf("")}
                    val number = remember { mutableStateOf("") }
                    val rollno = remember { mutableStateOf("") }
                    val dob = remember { mutableStateOf("") }
                    val gender = remember { mutableStateOf("") }
                    val semester = remember { mutableStateOf("") }
                    val session = remember { mutableStateOf("") }
                    val password = remember { mutableStateOf("") }
                    val confirmpassword = remember { mutableStateOf("") }
                    val nerror = remember { mutableStateOf(" ") }


                    val nameerror = remember { mutableStateOf(false) }
                    val emailerror = remember { mutableStateOf(false) }
                    val numbererror = remember{ mutableStateOf(false) }
                    val rollnoerror= remember { mutableStateOf(false) }
                    val doberror = remember{ mutableStateOf(false)  }
                    val gendererror = remember{ mutableStateOf(false)  }
                    val semesterror = remember { mutableStateOf(false) }
                    val sessionerror = remember{ mutableStateOf(false)  }
                    val passworderror = remember{ mutableStateOf(false)  }
                    val confirmpasswordrror = remember{ mutableStateOf(false)  }

                    val errormsg = remember { mutableStateOf("") }

                    fun Context.getXmlColor(colorResId: Int): Color {
                        val colorInt = ContextCompat.getColor(this, colorResId)
                        return Color(colorInt)
                    }


                    Row(modifier = Modifier.fillMaxSize(),) {
                        Column (modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally){

                            Spacer(modifier = Modifier.height(7.dp))
                            Text(text = getString(R.string.add_student), fontSize =50.sp, fontWeight = FontWeight.Bold)

                        }

                    }
                    LaunchedEffect(name.value) {
                        nameerror.value = name.value.length > 20
                        errormsg.value = if (nameerror.value) {
                            context.getString(R.string.empty_name)
                        } else {
                            ""
                        }
                    }
                                Spacer(modifier = Modifier.height(5.dp))

                                OutlinedTextField(
                                    value = name.value,
                                    onValueChange = { name.value = it },
                                    label = { Text(context.getString(R.string.name)) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.person),
                                            contentDescription = "Name Icon"
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions.Default,
                                    isError = nameerror.value
                                )


                                if (nameerror.value) {
                                    Text(
                                        text =getString(R.string.empty_name),
                                        color = context.getXmlColor(R.color.red),
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .padding(start = 8.dp, top = 2.dp)
                                            .fillMaxWidth()
                                    )
                                }


                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = {email.value = it},
                        label = { Text(getString(R.string.email)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.email),
                                contentDescription = "email icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        isError = emailerror.value

                        )
                    if (emailerror.value) {
                        Text(
                            text = getString(R.string.empty_email),
                            color = context.getXmlColor(R.color.red),
                            modifier = Modifier.padding(start = 8.dp).fillMaxSize()
                        )
                    }




                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = number.value,
                        onValueChange = {number.value= it},
                        label = { Text(getString(R.string.number)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.call),
                                contentDescription = "number icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = numbererror.value
                        )
                    if (numbererror.value) {
                        Text(
                            text = getString(R.string.empty_number),
                            color = context.getXmlColor(R.color.red),
                            modifier = Modifier.padding(start = 8.dp).fillMaxSize()
                        )
                    }
                    LaunchedEffect(number.value) {

                        numbererror.value = (number.value.length>0)&&(number.value.length<10)
                        errormsg.value = context.getString(R.string.number_digits)

                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = rollno.value,
                        onValueChange = {rollno.value=it},
                        label = { Text(getString(R.string.rollno)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.rollno),
                                contentDescription = "rollno icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        isError = rollnoerror.value
                        )
                    if (rollnoerror.value) {
                        Text(
                            text = getString(R.string.empty_rollno),
                            color = context.getXmlColor(R.color.red),
                            modifier = Modifier.padding(start = 8.dp).fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = dob.value,
                        onValueChange = {dob.value=it},
                        label = { Text(getString(R.string.dob)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.calender),
                                contentDescription = "dob icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        isError = doberror.value
                        )
                    if (doberror.value) {
                        Text(
                            text = getString(R.string.empty_dob),
                            color = context.getXmlColor(R.color.red),
                            modifier = Modifier.padding(start = 8.dp).fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(

                        value = gender.value,
                        onValueChange = {gender.value = it},
                        label = { Text(getString(R.string.gender)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.gender),
                                contentDescription = "gender Icon"
                            ) },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        isError = gendererror.value
                        )
                    if (gendererror.value) {
                        Text(
                            text = getString(R.string.empty_gender),
                            color = context.getXmlColor(R.color.red),
                            modifier = Modifier.padding(start = 8.dp).fillMaxSize()
                        )
                    }


                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = session.value,
                        onValueChange = {session.value= it},
                        label = { Text(getString(R.string.session)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.session),
                                contentDescription = "session icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        isError = sessionerror.value
                    )
                    if (sessionerror.value) {
                        Text(
                            text = getString(R.string.empty_session),
                            color = context.getXmlColor(R.color.red),
                            modifier = Modifier.padding(start = 8.dp).fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = semester.value,
                        onValueChange = {semester.value=it},
                        label = { Text(getString(R.string.semester)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.semester),
                                contentDescription = "semester icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        isError = sessionerror.value
                    )
                    if (semesterror.value) {
                        Text(
                            text = getString(R.string.empty_semester),
                            color = context.getXmlColor(R.color.red),
                            modifier = Modifier.padding(start = 8.dp).fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = {password.value= it},
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.password),
                                contentDescription = "password icon"
                            )
                        },
                        label = { Text(getString(R.string.password)) },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        isError = passworderror.value
                    )
                    if (passworderror.value) {
                        Text(
                            text = getString(R.string.empty_pass),
                            color = context.getXmlColor(R.color.red),
                            modifier = Modifier.padding(start = 8.dp).fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = confirmpassword.value,
                        onValueChange = {confirmpassword.value= it},
                        label = { Text(getString(R.string.confirm_pass)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.password),
                                contentDescription = "confirmpassword Icon"
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        isError =confirmpasswordrror.value

                    )
                    if (confirmpasswordrror.value) {
                        Text(
                            text = getString(R.string.retype_pass),
                            color = context.getXmlColor(R.color.red),
                            modifier = Modifier.padding(start = 8.dp).fillMaxSize()
                        )
                    }
                    val ButtonColors = ButtonDefaults.buttonColors(
                        containerColor = context.getXmlColor(R.color.green)
                    )


                    Row {
                        Column(modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.height(5.dp))
                            Button(onClick = {
                                    nameerror.value = name.value.isNullOrEmpty()
                                    emailerror.value = email.value.isNullOrEmpty()
                                    numbererror.value = number.value.isNullOrEmpty()
                                    rollnoerror.value = rollno.value.isNullOrEmpty()
                                    doberror.value = dob.value.isNullOrEmpty()
                                    gendererror.value = gender.value.isNullOrEmpty()
                                    sessionerror.value = session.value.isNullOrEmpty()
                                    semesterror.value = semester.value.isNullOrEmpty()
                                    passworderror.value = password.value.isNullOrEmpty()
                                    confirmpasswordrror.value =
                                        confirmpassword.value.isNullOrEmpty()


                                },
                                colors = ButtonColors,
                              ) {
                                Text(
                                    text = getString(R.string.add),
                                    fontSize = (30.sp),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(2.dp)
                                )


                            }

                        }
                    }



                }

            }

        }
        return view
    }
}