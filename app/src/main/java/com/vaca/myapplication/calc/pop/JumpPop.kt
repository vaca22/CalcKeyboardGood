package com.vaca.myapplication.calc.pop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.vaca.myapplication.R
import kotlinx.coroutines.NonCancellable.cancel


class JumpPop(
    mContext: Context,
    currentPage:Int,
    total:Int
) : PopupWindow() {
    val view = LayoutInflater.from(mContext).inflate(R.layout.pop_jump, null)
    val page:TextView=view.findViewById(R.id.input_num)

     init {

         isOutsideTouchable = true
         contentView = view
         height = RelativeLayout.LayoutParams.MATCH_PARENT
         width = RelativeLayout.LayoutParams.MATCH_PARENT
         isFocusable = true
         val dw = ColorDrawable(-0x80000000)
         setBackgroundDrawable(dw)
         this.animationStyle = R.style.take_photo_anim


         val title: TextView = view.findViewById(R.id.title)
         title.text ="当前页：$currentPage   \n(1-$total)"

     }






    fun changePage(i:Int){
        if(i==-1){
            page.text=""
            return
        }
        page.text=i.toString()
    }


}