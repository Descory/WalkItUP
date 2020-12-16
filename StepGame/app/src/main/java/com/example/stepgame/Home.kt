package com.example.stepgame

import android.animation.ObjectAnimator
import android.content.Context
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
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.*;
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*
import kotlin.math.round

class Home : Fragment(), SensorEventListener {

    private var username: String? = ""
    private var sensorManager: SensorManager? = null
    private var cal = Calendar.getInstance()
    private var currentDate = cal.get(Calendar.DAY_OF_YEAR)

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return inflater.inflate(R.layout.fragment_home, container, false)
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
            val step = data[0].stepdist
            val unit = data[0].units
            var dist = 0
            var cal = 0f
            var stps = previousTotalSteps.toInt() -data[0].totalsteps .toInt()
                    db.close()

            tv_stepsTaken.text = ("$stps")

            dist = step * stps
            cal = stps * 0.03f
            if (unit == 0) {
                if (dist >= 100000) {
                    var cm = dist % 100
                    var km = dist / 100000
                    var m = dist / 100 - (km * 1000)
                    distanceWalked.text = km.toString() + " km " + m.toString() + " m " + cm.toString() + " cm"
                } else if (dist >= 100) {
                    var cm = dist % 100
                    var m = dist / 100
                    distanceWalked.text = m.toString() + " m " + cm.toString() + " cm"
                } else {
                    distanceWalked.text = dist.toString() + " cm"
                }
            } else {
                if (dist >= 5280) {
                    var mi = dist / 5280
                    var ft = dist % 5280
                    distanceWalked.text = mi.toString() + " mi " + ft.toString() + " ft"
                } else {
                    distanceWalked.text = dist.toString() + " ft"
                }
            }
            var calor = round(cal * 100) / 100
            caloriesBurned.text = calor.toString() + " Kcal"

            circularProgressBar.apply {
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
        val step = data[0].stepdist
        val unit = data[0].units
        var xpneed = 500 + data[0].level*data[0].level*500
        progressBar.max = xpneed
        level.text = "Level: " +data[0].level
        var dist = 0
        var cal = 0f
        if(running) {
            progressBar.run {
                totalSteps = event!!.values[0]
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
                tv_stepsTaken.text = ("$currentSteps")

                dist = step * currentSteps
                cal = currentSteps * 0.03f
                if (unit == 0) {
                    if (dist >= 100000) {
                        var cm = dist % 100
                        var km = dist / 100000
                        var m = dist / 100 - (km * 1000)
                        distanceWalked.text =
                            km.toString() + " km " + m.toString() + " m " + cm.toString() + " cm"
                    } else if (dist >= 100) {
                        var cm = dist % 100
                        var m = dist / 100
                        distanceWalked.text = m.toString() + " m " + cm.toString() + " cm"
                    } else {
                        distanceWalked.text = dist.toString() + " cm"
                    }
                } else {
                    if (dist >= 5280) {
                        var mi = dist / 5280
                        var ft = dist % 5280
                        distanceWalked.text = mi.toString() + " mi " + ft.toString() + " ft"
                    } else {
                        distanceWalked.text = dist.toString() + " ft"
                    }
                }
                var calor = round(cal * 100) / 100
                caloriesBurned.text = calor.toString() + " Kcal"
                if (data[0].totalsteps >= xpneed) {
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
                progrss.text = cyrr.toInt().toString() + " / " + xpneed.toString()
                progressBar.setProgress(cyrr.toInt())

                circularProgressBar.apply {
                    setProgressWithAnimation((currentSteps.toFloat()))
                }
            }
        }
        db.close()
    }

    private fun loadData() {

        val sharedPreferences= this.activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val oldSteps= sharedPreferences!!.getFloat("OLD_DATA",0f)
        val frst = sharedPreferences!!.getBoolean("FL",true)
        val savedNumber= sharedPreferences!!.getFloat("key1",0f)
        val savedDate = sharedPreferences?.getInt("DATeKEY", 0)
        if(frst)
        {
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
        }
        else {
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
                    totalSteps-previousTotalSteps,
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
    companion object {

        fun newInstance(name: String) = Home().apply {
            arguments = Bundle().apply {
                putString("usersname", name)
            }
        }
    }

}