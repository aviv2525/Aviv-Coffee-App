package com.example.avivcoffee

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        val mainPage = findViewById<LinearLayout>(R.id.main_layout)

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
            val intent = Intent(this, MenuPageActivity::class.java)
            startActivity(intent)        }


        reserveSeatsBtn.setOnClickListener {
            showDatePicker()
        }



        reservationBtn.setOnClickListener {
            showReservationDetails()
        }

        val locationButton: Button = findViewById(R.id.LocationBtn)
        locationButton.setOnClickListener { openLocationInMap() }

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

        datePickerDialog.show()
    }

    private fun showGuestPicker() {
        val guests = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10+")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Number of Guests")
        builder.setSingleChoiceItems(guests, 0) { _, which ->
            selectedGuests = if (which == guests.size - 1) 10 else which + 1
        }
        builder.setPositiveButton("Next") { dialog, _ ->
            dialog.dismiss()
            showTimePicker()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun showReservationDetails() {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            val message = getString(R.string.reservation_message,selectedDate,selectedTime,selectedGuests)
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

    private fun openLocationInMap() {
        val gmmIntentUri = Uri.parse("geo:0,0?q=32.0853,34.7818(TLV Cafe)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    fun openLocationInMap(view: View) {}
}
