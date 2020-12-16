package com.example.stepgame

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_achievements.*

class Achievements : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_achievements, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context: Context = this.context!!
        var db = DataBaseHandler(context)
        val data = db.readData()
        val step = data[0].totalsteps
        Log.d("dafak", "$step")

        if (step > 0){
            popo.text = "✔"
            popo.setTextColor(Color.parseColor("#03C04A"))
        }
        if (step >= 100000){
            anhievment3.text = "✔"
            anhievment3.setTextColor(Color.parseColor("#03C04A"))
        }
        if (step >= 500000){
            textView17.text = "✔"
            textView17.setTextColor(Color.parseColor("#03C04A"))
        }
        if (step >= 1000000){
            textView19.text = "✔"
            textView19.setTextColor(Color.parseColor("#03C04A"))
        }
        if (data[0].dail > 0){
            textView21.text = "✔"
            textView21.setTextColor(Color.parseColor("#03C04A"))
        }
        if (data[0].dail > 4){
            textView24.text = "✔"
            textView24.setTextColor(Color.parseColor("#03C04A"))
        }
        if (data[0].dail > 9){
            textView145.text = "✔"
            textView145.setTextColor(Color.parseColor("#03C04A"))
        }
        if (data[0].week > 0){
            textView23.text = "✔"
            textView23.setTextColor(Color.parseColor("#03C04A"))
        }
        if (data[0].week > 4){
            dasd.text = "✔"
            dasd.setTextColor(Color.parseColor("#03C04A"))
        }
        if (data[0].month > 0){
            textView25.text = "✔"
            textView25.setTextColor(Color.parseColor("#03C04A"))
        }
        if (data[0].month > 2){
            textView26.text = "✔"
            textView26.setTextColor(Color.parseColor("#03C04A"))
        }

        db.close()
    }

}