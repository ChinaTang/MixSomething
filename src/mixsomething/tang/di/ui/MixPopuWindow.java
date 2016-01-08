package mixsomething.tang.di.ui;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;


/**
 * 获得PopuWindow的ContentView
 * 用作下拉框，需获取button的宽度
 * 所以传入的ID组件中需要有ListView组件
 * popuwindow中有button，之类抢夺焦掉之类的组件时要注意，可能点击下拉框或者，点击下拉框之外的地方不会让下拉框消失
 * 传入设置好的已经setAdapter的listView
 */
public class MixPopuWindow {
	
	private View mContentView;
	
	private Context context;
	
	private PopupWindow popupWindow;
	
	private ListView listView;
	
	
	
	public MixPopuWindow(View contentView, ListView listView, Context context){
		mContentView = contentView;
		this.listView = listView;
		this.context = context;
	}

	public PopupWindow CreateMixPopuWindow(int width, int length, int backGroundResouseID){
		popupWindow = new PopupWindow(mContentView, width, length);
		//防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(backGroundResouseID));
		//设置popuwindow中的listView 可以获得焦点
		listView.setFocusable(true);
		//设置点击popuwindow以外的区域有效
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		//下拉弹出
		popupWindow.showAsDropDown(mContentView);
		return popupWindow;
	}
	
	
	

}
