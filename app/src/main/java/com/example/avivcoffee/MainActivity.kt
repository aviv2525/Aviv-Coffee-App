package com.example.avivcoffee

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat


import java.util.Calendar
import kotlin.text.*

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val images = listOf(R.drawable.background1, R.drawable.background2,R.drawable.background3, R.drawable.background4)
    private var currentImageIndex = 0

    private lateinit var reservationBtn: Button
    private lateinit var reserveSeatsBtn: Button

    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var selectedGuests: Int = 1
    private var selectedSmokeArea: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        val mainPage = findViewById<LinearLayout>(R.id.main_layout)
        // background change every 5 second :)
        val changeBackgroundRunnable = object : Runnable {
            override fun run() {
                currentImageIndex = (currentImageIndex + 1) % images.size
                mainPage.setBackgroundResource(images[currentImageIndex])
                handler.postDelayed(this, 5000)
            }
        }
        handler.post(changeBackgroundRunnable)

        reserveSeatsBtn = findViewById(R.id.ReserveSeatsBtm)
        reservationBtn = findViewById(R.id.ReservationBtn)

        val menuButton = findViewById<Button>(R.id.MenuBtn)

        menuButton.setOnClickListener {
            val rotateScaleAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_scale)
            it.startAnimation(rotateScaleAnim)
            val intent = Intent(this, MenuPageActivity::class.java)
            startActivity(intent)        }

        reserveSeatsBtn.setOnClickListener {
            val bounceFade: Animation = AnimationUtils.loadAnimation(this, R.anim.click_anim)
            reserveSeatsBtn.startAnimation(bounceFade)
            showDatePicker()
        }

        reservationBtn.setOnClickListener {
            val bounceFade: Animation = AnimationUtils.loadAnimation(this, R.anim.click_anim)
            reservationBtn.startAnimation(bounceFade)
            showReservationDetails()
        }

        val ContactUs: Button = findViewById(R.id.Connect_usBtn)
        ContactUs.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.contact_us))
            builder.setMessage(getString(R.string.mobile_phone_052_8749406))

            builder.setNegativeButtonIcon(getDrawable(R.drawable.baseline_info_24))
            builder.setNegativeButton(getString(R.string.linkedin)) { _, _ ->
                val linkedInUrl = "https://www.linkedin.com/in/aviv-malul-46a064216/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
                startActivity(intent)
            }

            builder.setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        val locationButton: Button = findViewById(R.id.LocationBtn)
        locationButton.setOnClickListener {
            openLocationInMap()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            showGuestPicker()
        }, year, month, day)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun showGuestPicker() {
        val guests = arrayOf("1", "2", "3", "4", "5", "6", "7+")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Number of Guests")
        builder.setSingleChoiceItems(guests, 1) { _, which ->
            selectedGuests = if (which == guests.size - 1) 10 else which + 1
        }



        val radioGroup = RadioGroup(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(20, 10, 20, 10)
        }

        val smokeButton = RadioButton(this).apply {
            text = context.getString(R.string.smoke)
            textSize = 16f
            setPadding(30,0,30,0)
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(context, R.drawable.radio_button_square)
            id = View.generateViewId()
        }

        val non_smokeButton = RadioButton(this).apply {
            text = context.getString(R.string.non_smoke)
            textSize = 16f
            setPadding(30, 0, 30, 0)
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(context, R.drawable.radio_button_square)

            id = View.generateViewId()
        }


        radioGroup.addView(smokeButton)
        radioGroup.addView(non_smokeButton)

        builder.setView(radioGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
        selectedSmokeArea = when (radioGroup.checkedRadioButtonId) {
            smokeButton.id -> getString(R.string.smoke)
            non_smokeButton.id -> getString(R.string.non_smoke)
            else ->  getString(R.string.everywhere)
            }
        }
        builder.setPositiveButton(getString(R.string.next)) { dialog, _ ->
            dialog.dismiss()
            showTimePicker()
            //showPaymentMethod()

        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            if (hour > selectedHour ||
                (hour == selectedHour && selectedMinute < minute) ) {
                AlertDialog.Builder(this).setTitle(getString(R.string.invalid_time_msg))
                    .setMessage(getString(R.string.not_possible_to_order))
                    .setPositiveButton(getString(R.string.OK)) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
            else{
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            }
        }, hour
            , minute, true)


        timePickerDialog.show()
    }

    // All the details include Smoking area
    // Payment method replace with smoking area ( in my opinion more important )
    private fun showReservationDetails() {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            val smokeMessage = if (selectedSmokeArea.isNotEmpty()) selectedSmokeArea else "Everywhere"
            val message = getString(R.string.reservation_message,selectedDate,selectedTime,selectedGuests) + getString(R.string.smoking_area, smokeMessage)

            val spannableMessage = SpannableString(message)
            spannableMessage.setSpan(RelativeSizeSpan(1.2f), 0, message.length, 0)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.reservation_details))
                .setMessage(spannableMessage)
                .setPositiveButton(getString(R.string.OK), null)
                .show()
        } else {
            Toast.makeText(this, getString(R.string.empty_reservation_details), Toast.LENGTH_SHORT).show()
        }
    }

    // option for the future, maybe this place will open and have a real location one day
    private fun openLocationInMap() {
    Toast.makeText(this,
        getString(R.string.location_will_be_provided_soon), Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


}
