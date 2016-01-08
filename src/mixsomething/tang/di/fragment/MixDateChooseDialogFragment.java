package mixsomething.tang.di.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by tangdi on 2016/1/8.
 *对于图形界面的编辑可以通过重写Fragment的onCreateView方法
 * 也可以通过重写DialogFragment的onCreateDialog
 * 对于界面UI控制推荐使用Fragment，这样在后期UI更改时可以减少代码的更改量
 * 1.若不适用默认的dialog则需要在获得实例前实现接口CustomerDialog
 */
public class MixDateChooseDialogFragment extends DialogFragment{

    private static final String TAG = "====MixDateChooseDialogFragment===";

    /**
     * @return
     */
    interface CustomerDialog{
        Dialog getCustomerDialog();
    }

    private CustomerDialog customerDialog;

    private Date mDate;

    /**
     * 是否选择默认的dialog标志
     */
    private static boolean choiseDialogFlag = true;

    /**
     * 设置dialog的界面初始时间是多少
     */
    public static final String SETDATA = "setDate";

    /**
     * 是否使用已经定义好的dialog
     * @return
     */
    public Dialog chooseDialog(boolean choise, CustomerDialog customerDialog){

            choiseDialogFlag = choise;
            return customerDialog.getCustomerDialog();
    }



    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    /**
     * 完成dialog的显示和数据传递需要设置目标Fragment，并设置请求参数
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if(choiseDialogFlag){
            return defaultDialog();
        }else{
            return chooseDialog(false, customerDialog);
        }
    }

    /**
     * 默认的dateDialog
     * 完成dialog的显示和数据传递需要设置目标Fragment，并设置请求参数
     * @return 返回一个默认的dialog
     */
    private Dialog defaultDialog(){
        mDate = (Date)getArguments().getSerializable(SETDATA);
        DatePicker datePicker = new DatePicker(getActivity());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate = new GregorianCalendar(year, month, day).getTime();
                getArguments().putSerializable(SETDATA, mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("选择时间")
                .setView(datePicker)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    public MixDateChooseDialogFragment getMixDateChooseDialogFragment(Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable(SETDATA, date);
        MixDateChooseDialogFragment fragment = new MixDateChooseDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(SETDATA, mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        Log.d(TAG, "数据返回成功");
    }

}
