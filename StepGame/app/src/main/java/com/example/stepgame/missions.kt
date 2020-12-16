package com.example.stepgame

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_missions.*
import java.util.*
import kotlin.math.round

class missions : Fragment(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var cal = Calendar.getInstance()
    private var currentDate = cal.get(Calendar.DAY_OF_YEAR)
    private var curretWeek = cal.get(Calendar.WEEK_OF_YEAR)
    private var curretMonth = cal.get(Calendar.MONTH)
    private var weekSteps = 0f
    private var monthSteps = 0f

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return inflater.inflate(R.layout.fragment_missions, container, false)
    }

    override fun onResume() {
        super.onResume()
        loadData()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(activity, "No step sensor on this device", Toast.LENGTH_LONG).show()
        } else {

            val context: Context = this.context!!
            var db = DataBaseHandler(context)
            val data = db.readData()
            var stps = previousTotalSteps.toInt()
            db.close()

            tvdaily.text = ("$stps")

            Daily.apply {
                setProgressWithAnimation((stps.toFloat()))
            }
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        totalSteps = event!!.values[0]
        loadData()
        val context: Context = this.context!!
        var db = DataBaseHandler(context)
        val data = db.readData()
        var xpneed = 500 + data[0].level*data[0].level*500
        progressBar2.max = xpneed
        level2.text = "Level: " +data[0].level
        if(running) {
            progressBar2.run {
                totalSteps = event!!.values[0]
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
                if (currentSteps >= 2500) {
                    if (currentSteps == 2500){
                        var user = User(
                            data[0].userName,
                            data[0].age,
                            data[0].units,
                            data[0].gender,
                            data[0].weight,
                            data[0].stepdist,
                            data[0].totalsteps,
                            data[0].level,
                            data[0].exp + 500,
                            data[0].dail + 1,
                            data[0].week,
                            data[0].month
                        )
                        db.updateData(user)
                            }
                    tv_totalMax2.text = ""
                    tvdaily.text = "Completed"
                    tvdaily.setTextColor(Color.parseColor("#03C04A"))
                    textView6.setTextColor(Color.parseColor("#03C04A"))
                }
                else{
                    tvdaily.text = ("$currentSteps")
                }
                if (weekSteps.toInt() >= 15000){
                    if (weekSteps.toInt() == 15000){
                        var user = User(
                            data[0].userName,
                            data[0].age,
                            data[0].units,
                            data[0].gender,
                            data[0].weight,
                            data[0].stepdist,
                            data[0].totalsteps,
                            data[0].level,
                            data[0].exp + 2500,
                            data[0].dail,
                            data[0].week + 1,
                            data[0].month

                        )
                        weeklypr.apply {
                            setProgressWithAnimation((weekSteps))
                        }
                        db.updateData(user)
                    }
                    textView13.text = ""
                    weeklytv.text = "Completed"
                    weeklytv.setTextColor(Color.parseColor("#03C04A"))
                    textView12.setTextColor(Color.parseColor("#03C04A"))
                }
                else{
                    weeklytv.text = weekSteps.toInt().toString()
                    weeklypr.apply {
                        setProgressWithAnimation((weekSteps))
                    }
                }
                if (monthSteps.toInt() >= 80000){
                    if (monthSteps.toInt() == 80000){
                        var user = User(
                            data[0].userName,
                            data[0].age,
                            data[0].units,
                            data[0].gender,
                            data[0].weight,
                            data[0].stepdist,
                            data[0].totalsteps,
                            data[0].level,
                            data[0].exp + 5000,
                            data[0].dail,
                            data[0].week,
                            data[0].month + 1

                        )
                        montlypr.apply {
                            setProgressWithAnimation((monthSteps))
                        }
                        db.updateData(user)
                    }
                    textView15.text = ""
                    monthlytv.text = "Completed"
                    monthlytv.setTextColor(Color.parseColor("#03C04A"))
                    textView10.setTextColor(Color.parseColor("#03C04A"))
                }
                else{
                    monthlytv.text = monthSteps.toInt().toString()
                    montlypr.apply {
                        setProgressWithAnimation((monthSteps))
                    }
                }
                if (data[0].totalsteps.toInt() + data[0].exp >= xpneed) {
                    var user = User(
                        data[0].userName,
                        data[0].age,
                        data[0].units,
                        data[0].gender,
                        data[0].weight,
                        data[0].stepdist,
                        data[0].totalsteps,
                        data[0].level + 1,
                        data[0].exp,
                        data[0].dail,
                        data[0].week,
                        data[0].month
                    )
                    db.updateData(user)
                    var xpneed = 500 + data[0].level*750
                    progressBar.max = xpneed
                    level.text = "Level: " +data[0].level
                }
                var cyrr = data[0].exp + data[0].totalsteps
                progrss2.text = cyrr.toInt().toString() + " / " + xpneed.toString()
                progressBar2.setProgress(cyrr.toInt())

                Daily.apply {
                    setProgressWithAnimation((currentSteps.toFloat()))
                }
            }
        }
        db.close()
    }

    private fun loadData() {

        val sharedPreferences = this.activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val oldSteps = sharedPreferences!!.getFloat("OLD_DATA", 0f)
        val frst = sharedPreferences!!.getBoolean("FL", true)
        val savedNumber = sharedPreferences!!.getFloat("key1", 0f)
        val savedMonthN = sharedPreferences!!.getFloat("key2", 0f)
        val savedWeekN = sharedPreferences!!.getFloat("key3", 0f)
        val savedDate = sharedPreferences?.getInt("DATeKEY", 0)
        val savedMonth = sharedPreferences?.getInt("month", 0)
        val savedWeek = sharedPreferences?.getInt("week", 0)
        if (frst) {
            with(sharedPreferences?.edit()) {
                this?.putFloat("OLD_DATA", totalSteps)
                this?.putBoolean("FL", false)
                this?.apply()
            }
            val context: Context = this.context!!
            var db = DataBaseHandler(context)
            val data = db.readData()
            var user = User(
                data[0].userName,
                data[0].age,
                data[0].units,
                data[0].gender,
                data[0].weight,
                data[0].stepdist,
                0f,
                data[0].level,
                data[0].exp,
                data[0].dail,
                data[0].week,
                data[0].month
            )
            db.updateData(user)
            db.close()
        } else {
            if(curretWeek != savedWeek){
                weekSteps = 0f;
                with(sharedPreferences?.edit()) {
                    this?.putInt("week", curretWeek)
                    this?.putFloat("key3", weekSteps)
                    this?.apply()
                }
            }
            else{
                weekSteps = weekSteps + 1
                with(sharedPreferences?.edit()) {
                    this?.putFloat("key3", weekSteps)
                    this?.apply()
                }
            }
            if(curretMonth != savedMonth){
                monthSteps = 0f;
                with(sharedPreferences?.edit()) {
                    this?.putInt("month", curretMonth)
                    this?.putFloat("key2", monthSteps)
                    this?.apply()
                }
            }
            else{
                monthSteps = monthSteps + 1
                with(sharedPreferences?.edit()) {
                    this?.putFloat("key2", monthSteps)
                    this?.apply()
                }
            }
            if (currentDate != savedDate!!.toInt()) {
                val context: Context = this.context!!
                var db = DataBaseHandler(context)
                val data = db.readData()
                var user = User(
                    data[0].userName,
                    data[0].age,
                    data[0].units,
                    data[0].gender,
                    data[0].weight,
                    data[0].stepdist,
                    totalSteps - oldSteps,
                    data[0].level,
                    data[0].exp,
                    data[0].dail,
                    data[0].week,
                    data[0].month
                )
                db.updateData(user)
                db.close()
                var dbs = DataBaseHandler(context)
                val datas = db.readData()

                var xs = datas[0].totalsteps
                previousTotalSteps = (datas[0].totalsteps + oldSteps)
                dbs.close()
                with(sharedPreferences?.edit()) {
                    this?.putInt("DATeKEY", currentDate)
                    this?.putFloat("key1", previousTotalSteps)
                    this?.apply()
                }
            } else {
                weekSteps = totalSteps - previousTotalSteps
                monthSteps = totalSteps - previousTotalSteps
                previousTotalSteps = savedNumber
                val context: Context = this.context!!
                var db = DataBaseHandler(context)
                val data = db.readData()
                var user = User(
                    data[0].userName,
                    data[0].age,
                    data[0].units,
                    data[0].gender,
                    data[0].weight,
                    data[0].stepdist,
                    totalSteps - previousTotalSteps,
                    data[0].level,
                    data[0].exp,
                    data[0].dail,
                    data[0].week,
                    data[0].month
                )

                db.updateData(user)
                db.close()
            }
        }
    }
}